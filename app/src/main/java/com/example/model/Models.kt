package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

enum class Language(val code: String, val displayName: String) {
    BN("bn", "বাংলা"),
    EN("en", "English")
}

enum class StreamCategory(val id: String, val titleBn: String, val titleEn: String) {
    ALL("all", "সব", "All"),
    POPULAR("popular", "জনপ্রিয়", "Popular"),
    MUSIC("music", "সঙ্গীত", "Music"),
    CHAT("chat", "আড্ডা", "Hangout"),
    GAMING("gaming", "গেমিং", "Gaming"),
    TALENT("talent", "প্রতিভা", "Talent")
}

data class LiveStream(
    val id: String,
    val hostName: String,
    val hostUsername: String,
    val titleBn: String,
    val titleEn: String,
    val category: StreamCategory,
    val viewerCount: Int,
    val likeCount: Long,
    val diamondsEarned: Long,
    val isVerified: Boolean = true,
    val isFollowing: Boolean = false,
    val tags: List<String> = emptyList(),
    val avatarColor: Color = Color(0xFFFF2D55),
    val gradientColors: List<Color> = listOf(Color(0xFF7928CA), Color(0xFFFF2D55)),
    val country: String = "🇧🇩 Bangladesh",
    val level: Int = 12
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val senderLevel: Int,
    val text: String,
    val isGiftAnnouncement: Boolean = false,
    val giftIcon: String = "",
    val badgeColor: Color = Color(0xFFFF2D55),
    val timestamp: Long = System.currentTimeMillis()
)

data class GiftItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val iconEmoji: String,
    val coinPrice: Int,
    val rarityColor: Color,
    val animationDescription: String = ""
)

data class PartySeat(
    val seatIndex: Int,
    val occupantName: String? = null,
    val occupantLevel: Int = 1,
    val isMuted: Boolean = false,
    val isSpeaking: Boolean = false,
    val avatarColor: Color = Color(0xFF4FACFE)
)

data class PartyRoom(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val hostName: String,
    val hostLevel: Int,
    val category: String,
    val listenersCount: Int,
    val seats: List<PartySeat>,
    val bannerColor: Color = Color(0xFF7928CA)
)

data class UserProfile(
    val id: String = "JOY-98214",
    val name: String = "Joy User (জয় ইউজার)",
    val bioBn: String = "জয় লাইভে নতুন বন্ধু বানাতে এসেছি! লাইভ স্ট্রিম ও গান পছন্দ করি।",
    val bioEn: String = "Here to make new friends on Joy Live! Love streams & music.",
    val level: Int = 7,
    val coinBalance: Int = 1250,
    val diamondBalance: Int = 340,
    val followersCount: Int = 412,
    val followingCount: Int = 89,
    val totalLiveHours: Float = 14.5f,
    val dailyBonusClaimed: Boolean = false
)

data class FloatingHeart(
    val id: Long,
    val startXFraction: Float,
    val color: Color,
    val sizeDp: Float
)

data class RechargePackage(
    val id: String,
    val coins: Int,
    val bonusCoins: Int,
    val priceBdt: Int,
    val isPopular: Boolean = false
)
