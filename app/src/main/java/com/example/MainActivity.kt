package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Language
import com.example.ui.components.GiftShopModal
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.HomeScreenTab
import com.example.viewmodel.JoyLiveViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: JoyLiveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            // Toast feedback
            LaunchedEffect(uiState.toastMessage) {
                uiState.toastMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            JoyLiveTheme(darkTheme = uiState.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // If a live room is opened, render fullscreen LiveRoomScreen
                    if (uiState.activeLiveStream != null) {
                        LiveRoomScreen(
                            stream = uiState.activeLiveStream!!,
                            chatMessages = uiState.chatMessages,
                            floatingHearts = uiState.floatingHearts,
                            currentLanguage = uiState.currentLanguage,
                            lastGiftSent = uiState.lastGiftSent,
                            lastGiftSender = uiState.lastGiftSender,
                            onClose = { viewModel.closeLiveRoom() },
                            onTapScreen = { xRatio -> viewModel.tapHeart(xRatio) },
                            onSendComment = { text -> viewModel.sendUserComment(text) },
                            onOpenGiftShop = { viewModel.openGiftShop(true) },
                            onToggleFollow = { viewModel.toggleFollow(uiState.activeLiveStream!!.id) },
                            onShare = {
                                Toast.makeText(
                                    context,
                                    if (uiState.currentLanguage == Language.BN)
                                        "লাইভ স্ট্রিম লিংক কপি করা হয়েছে!"
                                    else
                                        "Live stream link copied!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )

                        // Gift Shop Modal overlay
                        if (uiState.showGiftShop) {
                            GiftShopModal(
                                userCoins = uiState.userProfile.coinBalance,
                                currentLanguage = uiState.currentLanguage,
                                onDismiss = { viewModel.openGiftShop(false) },
                                onSendGift = { gift -> viewModel.sendGift(gift) },
                                onOpenRecharge = {
                                    viewModel.openGiftShop(false)
                                    viewModel.openRechargeModal(true)
                                }
                            )
                        }
                    } else {
                        // Main App Screen with Navigation
                        MainAppContent(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: JoyLiveViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isBn = uiState.currentLanguage == Language.BN

    Scaffold(
        topBar = {
            // Hide top bar when in active camera broadcast mode
            if (uiState.currentTab != HomeScreenTab.GO_LIVE || !uiState.isHostingLive) {
                JoyLiveTopBar(
                    isBn = isBn,
                    currentLanguage = uiState.currentLanguage,
                    coinBalance = uiState.userProfile.coinBalance,
                    isDarkMode = uiState.isDarkMode,
                    onCoinClick = { viewModel.selectTab(HomeScreenTab.WALLET) },
                    onToggleLanguage = {
                        val nextLang = if (uiState.currentLanguage == Language.BN) Language.EN else Language.BN
                        viewModel.setLanguage(nextLang)
                    },
                    onToggleTheme = { viewModel.toggleTheme() }
                )
            }
        },
        bottomBar = {
            // Hide bottom bar when currently broadcasting live
            if (uiState.currentTab != HomeScreenTab.GO_LIVE || !uiState.isHostingLive) {
                JoyLiveBottomNav(
                    currentTab = uiState.currentTab,
                    isBn = isBn,
                    onSelectTab = { viewModel.selectTab(it) }
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                HomeScreenTab.LIVE_FEED -> {
                    LiveFeedScreen(
                        liveStreams = uiState.liveStreams,
                        selectedCategory = uiState.selectedCategory,
                        currentLanguage = uiState.currentLanguage,
                        onSelectCategory = { viewModel.setCategory(it) },
                        onOpenStream = { viewModel.openLiveRoom(it) },
                        onGoLiveClick = { viewModel.selectTab(HomeScreenTab.GO_LIVE) }
                    )
                }

                HomeScreenTab.PARTY_ROOMS -> {
                    AudioPartyScreen(
                        partyRooms = uiState.partyRooms,
                        currentLanguage = uiState.currentLanguage,
                        activeSeatRoomId = uiState.activeSeatRoomId,
                        activeSeatIndex = uiState.activeSeatIndex,
                        onTakeSeat = { roomId, seatIdx -> viewModel.takeSeat(roomId, seatIdx) },
                        onLeaveSeat = { roomId -> viewModel.leaveSeat(roomId) }
                    )
                }

                HomeScreenTab.GO_LIVE -> {
                    GoLiveScreen(
                        isHostingLive = uiState.isHostingLive,
                        streamTitle = uiState.hostStreamTitle,
                        streamCategory = uiState.hostCategory,
                        durationSeconds = uiState.hostStreamDurationSeconds,
                        viewersCount = uiState.hostViewersCount,
                        diamondsEarned = uiState.hostDiamondsEarned,
                        isFrontCamera = uiState.isFrontCamera,
                        isTorchOn = uiState.isTorchOn,
                        isMicMuted = uiState.isMicMuted,
                        beautyFilterLevel = uiState.beautyFilterLevel,
                        chatMessages = uiState.chatMessages,
                        floatingHearts = uiState.floatingHearts,
                        currentLanguage = uiState.currentLanguage,
                        showSummaryDialog = uiState.showSummaryDialog,
                        onStartLive = { title, cat -> viewModel.startHostLive(title, cat) },
                        onEndLive = { viewModel.endHostLive() },
                        onDismissSummary = { viewModel.dismissSummary() },
                        onToggleCameraFacing = { viewModel.toggleCameraFacing() },
                        onToggleTorch = { viewModel.toggleTorch() },
                        onToggleMic = { viewModel.toggleMic() },
                        onBeautyFilterChange = { viewModel.setBeautyFilter(it) },
                        onSendComment = { viewModel.sendUserComment(it) }
                    )
                }

                HomeScreenTab.WALLET -> {
                    WalletScreen(
                        userProfile = uiState.userProfile,
                        currentLanguage = uiState.currentLanguage,
                        onClaimDailyBonus = { viewModel.claimDailyBonus() },
                        onRecharge = { pkg, method -> viewModel.rechargeCoins(pkg, method) }
                    )
                }

                HomeScreenTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = uiState.userProfile,
                        currentLanguage = uiState.currentLanguage,
                        isDarkMode = uiState.isDarkMode,
                        onSetLanguage = { viewModel.setLanguage(it) },
                        onToggleTheme = { viewModel.toggleTheme() },
                        onClaimDailyBonus = { viewModel.claimDailyBonus() }
                    )
                }
            }
        }
    }
}

@Composable
fun JoyLiveTopBar(
    isBn: Boolean,
    currentLanguage: Language,
    coinBalance: Int,
    isDarkMode: Boolean,
    onCoinClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleTheme: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Branding: Logo + Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("app_branding_row")
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        Brush.linearGradient(listOf(JoyPinkPrimary, JoyCyan)),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "▶",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (isBn) "জয় লাইভ" else "JOY LIVE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = JoyPinkPrimary,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = if (isBn) "লাইভ সিস্টেম হাব" else "Live Broadcast",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Actions: Coins + Language + Theme
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Coin Badge Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onCoinClick() }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("top_bar_coin_pill")
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = JoyGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$coinBalance",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Language Toggle (BN/EN)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(JoyPurple.copy(alpha = 0.2f))
                    .clickable { onToggleLanguage() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("top_bar_lang_toggle"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (currentLanguage == Language.BN) "বাংলা" else "EN",
                    color = JoyPinkPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Theme Toggle
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier
                    .size(34.dp)
                    .testTag("top_bar_theme_toggle")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun JoyLiveBottomNav(
    currentTab: HomeScreenTab,
    isBn: Boolean,
    onSelectTab: (HomeScreenTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.testTag("joy_live_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentTab == HomeScreenTab.LIVE_FEED,
            onClick = { onSelectTab(HomeScreenTab.LIVE_FEED) },
            icon = {
                Icon(
                    imageVector = Icons.Default.LiveTv,
                    contentDescription = "Live"
                )
            },
            label = {
                Text(
                    text = if (isBn) "লাইভ" else "Live",
                    fontWeight = if (currentTab == HomeScreenTab.LIVE_FEED) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = JoyPinkPrimary,
                selectedTextColor = JoyPinkPrimary,
                indicatorColor = JoyPinkPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_tab_feed")
        )

        NavigationBarItem(
            selected = currentTab == HomeScreenTab.PARTY_ROOMS,
            onClick = { onSelectTab(HomeScreenTab.PARTY_ROOMS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = "Party"
                )
            },
            label = {
                Text(
                    text = if (isBn) "পার্টি" else "Party",
                    fontWeight = if (currentTab == HomeScreenTab.PARTY_ROOMS) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = JoyPurple,
                selectedTextColor = JoyPurple,
                indicatorColor = JoyPurple.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_tab_party")
        )

        // Center GO LIVE Highlighted Action
        NavigationBarItem(
            selected = currentTab == HomeScreenTab.GO_LIVE,
            onClick = { onSelectTab(HomeScreenTab.GO_LIVE) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Brush.linearGradient(listOf(JoyPinkPrimary, JoyCyan)),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Go Live",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            label = {
                Text(
                    text = if (isBn) "গো লাইভ" else "Go Live",
                    fontWeight = FontWeight.Bold,
                    color = JoyPinkPrimary,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.testTag("nav_tab_golive")
        )

        NavigationBarItem(
            selected = currentTab == HomeScreenTab.WALLET,
            onClick = { onSelectTab(HomeScreenTab.WALLET) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet"
                )
            },
            label = {
                Text(
                    text = if (isBn) "ওয়ালেট" else "Wallet",
                    fontWeight = if (currentTab == HomeScreenTab.WALLET) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = JoyGold,
                selectedTextColor = JoyGold,
                indicatorColor = JoyGold.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_tab_wallet")
        )

        NavigationBarItem(
            selected = currentTab == HomeScreenTab.PROFILE,
            onClick = { onSelectTab(HomeScreenTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text(
                    text = if (isBn) "প্রোফাইল" else "Profile",
                    fontWeight = if (currentTab == HomeScreenTab.PROFILE) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = JoyCyan,
                selectedTextColor = JoyCyan,
                indicatorColor = JoyCyan.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_tab_profile")
        )
    }
}
