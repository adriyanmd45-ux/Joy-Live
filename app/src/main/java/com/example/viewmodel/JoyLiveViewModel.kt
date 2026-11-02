package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.JoyLiveRepository
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class HomeScreenTab(val id: String, val titleBn: String, val titleEn: String) {
    LIVE_FEED("feed", "লাইভ", "Live"),
    PARTY_ROOMS("party", "পার্টি", "Party"),
    GO_LIVE("go_live", "গো লাইভ", "Go Live"),
    WALLET("wallet", "ওয়ালেট", "Wallet"),
    PROFILE("profile", "প্রোফাইল", "Profile")
}

data class JoyLiveUiState(
    val currentTab: HomeScreenTab = HomeScreenTab.LIVE_FEED,
    val selectedCategory: StreamCategory = StreamCategory.ALL,
    val liveStreams: List<LiveStream> = JoyLiveRepository.sampleLiveStreams,
    val partyRooms: List<PartyRoom> = JoyLiveRepository.samplePartyRooms,
    val activeLiveStream: LiveStream? = null,
    val userProfile: UserProfile = UserProfile(),
    val chatMessages: List<ChatMessage> = emptyList(),
    val floatingHearts: List<FloatingHeart> = emptyList(),
    val isHostingLive: Boolean = false,
    val hostStreamTitle: String = "আমার লাইভ আড্ডা ও গল্পগুজব 🎉",
    val hostCategory: StreamCategory = StreamCategory.CHAT,
    val hostStreamDurationSeconds: Long = 0L,
    val hostViewersCount: Int = 124,
    val hostDiamondsEarned: Long = 85L,
    val isFrontCamera: Boolean = true,
    val isTorchOn: Boolean = false,
    val isMicMuted: Boolean = false,
    val beautyFilterLevel: Float = 0.6f,
    val showGiftShop: Boolean = false,
    val showRechargeModal: Boolean = false,
    val showSummaryDialog: Boolean = false,
    val currentLanguage: Language = Language.BN,
    val isDarkMode: Boolean = true,
    val lastGiftSent: GiftItem? = null,
    val lastGiftSender: String = "",
    val activeSeatRoomId: String? = null,
    val activeSeatIndex: Int? = null,
    val toastMessage: String? = null
)

class JoyLiveViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(JoyLiveUiState())
    val uiState: StateFlow<JoyLiveUiState> = _uiState.asStateFlow()

    private var simulatedChatJob: Job? = null
    private var hostTimerJob: Job? = null
    private var viewerCountJob: Job? = null

    init {
        // Initial welcome chat messages
        resetRoomChat()
    }

    private fun resetRoomChat() {
        val welcomeList = listOf(
            ChatMessage(
                id = "c_init_1",
                senderName = "JoyLive System",
                senderLevel = 99,
                text = if (_uiState.value.currentLanguage == Language.BN)
                    "জয় লাইভে স্বাগতম! সবাইকে সম্মান দিয়ে কথা বলুন।"
                else
                    "Welcome to Joy Live! Please follow community guidelines.",
                badgeColor = Color(0xFF00F2FE)
            ),
            ChatMessage(
                id = "c_init_2",
                senderName = "তন্ময়",
                senderLevel = 14,
                text = "হ্যালো আপু! লাইভ দেখার অপেক্ষায় ছিলাম ❤️"
            )
        )
        _uiState.update { it.copy(chatMessages = welcomeList) }
    }

    fun selectTab(tab: HomeScreenTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setCategory(category: StreamCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun openLiveRoom(stream: LiveStream) {
        _uiState.update {
            it.copy(
                activeLiveStream = stream,
                floatingHearts = emptyList()
            )
        }
        resetRoomChat()
        startSimulatedViewerActivity()
    }

    fun closeLiveRoom() {
        simulatedChatJob?.cancel()
        _uiState.update { it.copy(activeLiveStream = null, showGiftShop = false) }
    }

    fun setLanguage(language: Language) {
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleFollow(streamId: String) {
        _uiState.update { state ->
            val updated = state.liveStreams.map {
                if (it.id == streamId) it.copy(isFollowing = !it.isFollowing) else it
            }
            val active = state.activeLiveStream
            val newActive = if (active?.id == streamId) active.copy(isFollowing = !active.isFollowing) else active
            state.copy(liveStreams = updated, activeLiveStream = newActive)
        }
    }

    fun tapHeart(startXFraction: Float = 0.85f) {
        val heartColors = listOf(
            Color(0xFFFF2D55),
            Color(0xFFFF1493),
            Color(0xFF00F2FE),
            Color(0xFFFFD700),
            Color(0xFF00E676),
            Color(0xFF7928CA)
        )
        val newHeart = FloatingHeart(
            id = System.currentTimeMillis(),
            startXFraction = (startXFraction + (Random.nextFloat() * 0.15f - 0.075f)).coerceIn(0.1f, 0.95f),
            color = heartColors.random(),
            sizeDp = Random.nextInt(26, 42).toFloat()
        )

        _uiState.update { state ->
            val active = state.activeLiveStream
            val updatedActive = active?.copy(likeCount = active.likeCount + 1)
            val cleanHearts = state.floatingHearts.filter { System.currentTimeMillis() - it.id < 2600 }
            state.copy(
                floatingHearts = cleanHearts + newHeart,
                activeLiveStream = updatedActive
            )
        }
    }

    fun sendUserComment(text: String) {
        if (text.isBlank()) return
        val user = _uiState.value.userProfile
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = user.name,
            senderLevel = user.level,
            text = text.trim(),
            badgeColor = Color(0xFFFF2D55)
        )
        _uiState.update { it.copy(chatMessages = it.chatMessages + newMsg) }
    }

    fun openGiftShop(show: Boolean) {
        _uiState.update { it.copy(showGiftShop = show) }
    }

    fun openRechargeModal(show: Boolean) {
        _uiState.update { it.copy(showRechargeModal = show) }
    }

    fun sendGift(gift: GiftItem) {
        val profile = _uiState.value.userProfile
        if (profile.coinBalance < gift.coinPrice) {
            _uiState.update {
                it.copy(
                    showRechargeModal = true,
                    toastMessage = if (it.currentLanguage == Language.BN) "পর্যাপ্ত কয়েন নেই!" else "Not enough coins!"
                )
            }
            return
        }

        // Deduct coins & add diamond credit to host
        val updatedCoins = profile.coinBalance - gift.coinPrice
        val newProfile = profile.copy(coinBalance = updatedCoins)

        val giftAnnouncement = ChatMessage(
            id = "gift_${System.currentTimeMillis()}",
            senderName = profile.name,
            senderLevel = profile.level,
            text = if (_uiState.value.currentLanguage == Language.BN)
                "একটি ${gift.nameBn} উপহার পাঠিয়েছেন!"
            else
                "sent a ${gift.nameEn} gift!",
            isGiftAnnouncement = true,
            giftIcon = gift.iconEmoji,
            badgeColor = gift.rarityColor
        )

        // Spawn a burst of hearts as well
        tapHeart(0.5f)
        tapHeart(0.55f)

        _uiState.update { state ->
            val active = state.activeLiveStream
            val updatedActive = active?.copy(
                diamondsEarned = active.diamondsEarned + (gift.coinPrice / 2).coerceAtLeast(1)
            )
            state.copy(
                userProfile = newProfile,
                chatMessages = state.chatMessages + giftAnnouncement,
                showGiftShop = false,
                lastGiftSent = gift,
                lastGiftSender = profile.name,
                activeLiveStream = updatedActive,
                toastMessage = if (state.currentLanguage == Language.BN)
                    "${gift.nameBn} সফলভাবে পাঠানো হয়েছে!"
                else
                    "${gift.nameEn} sent successfully!"
            )
        }
    }

    fun claimDailyBonus() {
        val profile = _uiState.value.userProfile
        if (!profile.dailyBonusClaimed) {
            val updated = profile.copy(
                coinBalance = profile.coinBalance + 100,
                dailyBonusClaimed = true
            )
            _uiState.update {
                it.copy(
                    userProfile = updated,
                    toastMessage = if (it.currentLanguage == Language.BN) "১০০ ফ্রি কয়েন পেয়েছেন! 🎉" else "Claimed 100 Free Coins! 🎉"
                )
            }
        }
    }

    fun rechargeCoins(pkg: RechargePackage, paymentMethod: String) {
        val profile = _uiState.value.userProfile
        val totalCoinsAdded = pkg.coins + pkg.bonusCoins
        val updated = profile.copy(coinBalance = profile.coinBalance + totalCoinsAdded)
        _uiState.update {
            it.copy(
                userProfile = updated,
                showRechargeModal = false,
                toastMessage = if (it.currentLanguage == Language.BN)
                    "$paymentMethod এর মাধ্যমে $totalCoinsAdded কয়েন যোগ হয়েছে! 🪙"
                else
                    "$totalCoinsAdded coins added via $paymentMethod! 🪙"
            )
        }
    }

    // Go Live Controls
    fun startHostLive(title: String, category: StreamCategory) {
        _uiState.update {
            it.copy(
                isHostingLive = true,
                hostStreamTitle = title.ifBlank { "আমার লাইভ আড্ডা ও গল্পগুজব 🎉" },
                hostCategory = category,
                hostStreamDurationSeconds = 0L,
                hostViewersCount = 38,
                hostDiamondsEarned = 12L,
                currentTab = HomeScreenTab.GO_LIVE
            )
        }
        resetRoomChat()
        startHostTimer()
    }

    fun endHostLive() {
        hostTimerJob?.cancel()
        viewerCountJob?.cancel()
        _uiState.update {
            it.copy(
                isHostingLive = false,
                showSummaryDialog = true
            )
        }
    }

    fun dismissSummary() {
        _uiState.update {
            it.copy(
                showSummaryDialog = false,
                currentTab = HomeScreenTab.LIVE_FEED
            )
        }
    }

    fun toggleCameraFacing() {
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun toggleMic() {
        _uiState.update { it.copy(isMicMuted = !it.isMicMuted) }
    }

    fun setBeautyFilter(level: Float) {
        _uiState.update { it.copy(beautyFilterLevel = level) }
    }

    // Audio Party Seat Controls
    fun takeSeat(roomId: String, seatIndex: Int) {
        _uiState.update { state ->
            val updatedRooms = state.partyRooms.map { room ->
                if (room.id == roomId) {
                    val updatedSeats = room.seats.map { seat ->
                        if (seat.seatIndex == seatIndex) {
                            seat.copy(
                                occupantName = state.userProfile.name,
                                occupantLevel = state.userProfile.level,
                                isSpeaking = false
                            )
                        } else seat
                    }
                    room.copy(seats = updatedSeats)
                } else room
            }
            state.copy(
                partyRooms = updatedRooms,
                activeSeatRoomId = roomId,
                activeSeatIndex = seatIndex,
                toastMessage = if (state.currentLanguage == Language.BN) "আপনি সিট নম্বর ${seatIndex + 1}-এ বসেছেন 🎙️" else "You took seat #${seatIndex + 1} 🎙️"
            )
        }
    }

    fun leaveSeat(roomId: String) {
        _uiState.update { state ->
            val updatedRooms = state.partyRooms.map { room ->
                if (room.id == roomId) {
                    val updatedSeats = room.seats.map { seat ->
                        if (seat.occupantName == state.userProfile.name) {
                            seat.copy(occupantName = null, isSpeaking = false)
                        } else seat
                    }
                    room.copy(seats = updatedSeats)
                } else room
            }
            state.copy(
                partyRooms = updatedRooms,
                activeSeatRoomId = null,
                activeSeatIndex = null
            )
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun startHostTimer() {
        hostTimerJob?.cancel()
        hostTimerJob = viewModelScope.launch {
            while (_uiState.value.isHostingLive) {
                delay(1000)
                _uiState.update { it.copy(hostStreamDurationSeconds = it.hostStreamDurationSeconds + 1) }

                // Periodic viewer increments
                if (Random.nextInt(0, 3) == 0) {
                    _uiState.update {
                        it.copy(
                            hostViewersCount = it.hostViewersCount + Random.nextInt(1, 4),
                            hostDiamondsEarned = it.hostDiamondsEarned + Random.nextInt(0, 2)
                        )
                    }
                }
            }
        }
        startSimulatedViewerActivity()
    }

    private fun startSimulatedViewerActivity() {
        simulatedChatJob?.cancel()
        simulatedChatJob = viewModelScope.launch {
            val names = listOf("রাহুল", "তানজিলা", "নাঈম", "নিশাত", "রিয়াদ", "মেহেদী", "সুমাইয়া", "আকাশ", "স্মৃতি")
            while (true) {
                delay(Random.nextLong(2800, 6000))
                val isBn = _uiState.value.currentLanguage == Language.BN
                val comments = if (isBn) JoyLiveRepository.simulatedUserCommentsBn else JoyLiveRepository.simulatedUserCommentsEn
                val randomComment = comments.random()
                val randomName = names.random()
                val randomLevel = Random.nextInt(3, 40)

                val newMsg = ChatMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    senderName = randomName,
                    senderLevel = randomLevel,
                    text = randomComment
                )

                _uiState.update {
                    val list = if (it.chatMessages.size > 50) it.chatMessages.drop(15) else it.chatMessages
                    it.copy(chatMessages = list + newMsg)
                }

                // Random hearts from viewers
                if (Random.nextBoolean()) {
                    tapHeart(Random.nextFloat() * 0.4f + 0.5f)
                }
            }
        }
    }
}
