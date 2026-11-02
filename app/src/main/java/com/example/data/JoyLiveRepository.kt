package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.*

object JoyLiveRepository {

    val giftCatalog = listOf(
        GiftItem(
            id = "g_rose",
            nameBn = "গোলাপ",
            nameEn = "Rose",
            iconEmoji = "🌹",
            coinPrice = 5,
            rarityColor = Color(0xFFFF2D55),
            animationDescription = "A red rose blossoms on the screen"
        ),
        GiftItem(
            id = "g_heart",
            nameBn = "ভালোবাসা",
            nameEn = "Love Heart",
            iconEmoji = "💖",
            coinPrice = 20,
            rarityColor = Color(0xFFFF1493),
            animationDescription = "Beating heart waves"
        ),
        GiftItem(
            id = "g_panda",
            nameBn = "কিউট পান্ডা",
            nameEn = "Cute Panda",
            iconEmoji = "🐼",
            coinPrice = 80,
            rarityColor = Color(0xFF00F2FE),
            animationDescription = "Panda dancing"
        ),
        GiftItem(
            id = "g_car",
            nameBn = "সুপারকার",
            nameEn = "Supercar",
            iconEmoji = "🏎️",
            coinPrice = 350,
            rarityColor = Color(0xFFFF9800),
            animationDescription = "Neon supercar zooms across"
        ),
        GiftItem(
            id = "g_rocket",
            nameBn = "স্পেস রকেট",
            nameEn = "Joy Rocket",
            iconEmoji = "🚀",
            coinPrice = 900,
            rarityColor = Color(0xFF7928CA),
            animationDescription = "Rocket blasting off into space"
        ),
        GiftItem(
            id = "g_crown",
            nameBn = "জয় রাজমুকুট",
            nameEn = "Joy Crown",
            iconEmoji = "👑",
            coinPrice = 2000,
            rarityColor = Color(0xFFFFD700),
            animationDescription = "Golden Royal Crown sparkles"
        )
    )

    val sampleLiveStreams = listOf(
        LiveStream(
            id = "stream_1",
            hostName = "তাসফিয়া নূর (Tasfia)",
            hostUsername = "@tasfia_live",
            titleBn = "সন্ধ্যায় মন মাতানো বাংলা গান ও আড্ডা 🎵❤️",
            titleEn = "Mesmerizing evening Bangla songs & chat 🎵❤️",
            category = StreamCategory.MUSIC,
            viewerCount = 3840,
            likeCount = 45200,
            diamondsEarned = 1890,
            tags = listOf("গান", "বাংলা", "LiveSong"),
            avatarColor = Color(0xFFFF2D55),
            gradientColors = listOf(Color(0xFF6B11FF), Color(0xFFFF2D55)),
            country = "🇧🇩 Dhaka",
            level = 28
        ),
        LiveStream(
            id = "stream_2",
            hostName = "রাকিব গেমিং (Rakib)",
            hostUsername = "@rakib_pro",
            titleBn = "PUBG Mobile বাংলাদেশ টুর্নামেন্ট লাইভ! 🎮🔥",
            titleEn = "PUBG Mobile Bangladesh Tournament Live! 🎮🔥",
            category = StreamCategory.GAMING,
            viewerCount = 8920,
            likeCount = 92400,
            diamondsEarned = 5420,
            tags = listOf("Gaming", "PUBG", "Clutch"),
            avatarColor = Color(0xFF00C9FF),
            gradientColors = listOf(Color(0xFF00C9FF), Color(0xFF92FE9D)),
            country = "🇧🇩 Chittagong",
            level = 42
        ),
        LiveStream(
            id = "stream_3",
            hostName = "মায়াবিনী রিয়া (Riya)",
            hostUsername = "@riya_joy",
            titleBn = "আজকের টপিক: বন্ধুদের আড্ডা ও মজার কুইজ ☕✨",
            titleEn = "Today's Topic: Friends Hangout & Fun Quiz ☕✨",
            category = StreamCategory.CHAT,
            viewerCount = 2150,
            likeCount = 28600,
            diamondsEarned = 980,
            tags = listOf("আড্ডা", "Friends", "Vibes"),
            avatarColor = Color(0xFFFF5E7E),
            gradientColors = listOf(Color(0xFFFF5E7E), Color(0xFFFF9966)),
            country = "🇧🇩 Sylhet",
            level = 19
        ),
        LiveStream(
            id = "stream_4",
            hostName = "ডিজে সজিব (DJ Sojib)",
            hostUsername = "@dj_sojib_mix",
            titleBn = "ননস্টপ লাইভ রিমিক্স বিটস ও ডিজে শো 🎧💥",
            titleEn = "Nonstop Live Remix Beats & DJ Show 🎧💥",
            category = StreamCategory.TALENT,
            viewerCount = 4610,
            likeCount = 61200,
            diamondsEarned = 3100,
            tags = listOf("DJ", "Remix", "Party"),
            avatarColor = Color(0xFF7928CA),
            gradientColors = listOf(Color(0xFF8A2387), Color(0xFFE94057)),
            country = "🇧🇩 Dhaka",
            level = 35
        ),
        LiveStream(
            id = "stream_5",
            hostName = "ফারহান ট্রাভেলার (Farhan)",
            hostUsername = "@farhan_tour",
            titleBn = "কক্সবাজার সমুদ্র সৈকত থেকে সরাসরি সূর্যাস্ত 🌅🌊",
            titleEn = "Live Sunset directly from Cox's Bazar Beach 🌅🌊",
            category = StreamCategory.POPULAR,
            viewerCount = 5730,
            likeCount = 78900,
            diamondsEarned = 4250,
            tags = listOf("Travel", "Beach", "Sunset"),
            avatarColor = Color(0xFFFF8008),
            gradientColors = listOf(Color(0xFFFF8008), Color(0xFFFFC837)),
            country = "🇧🇩 Cox's Bazar",
            level = 25
        ),
        LiveStream(
            id = "stream_6",
            hostName = "অনন্যা ড্যান্স (Ananya)",
            hostUsername = "@ananya_dance",
            titleBn = "ক্লাসিক্যাল ও মডার্ন ফিউশন নাচ লাইভ পারফরম্যান্স 💃",
            titleEn = "Classical & Modern Fusion Dance Performance 💃",
            category = StreamCategory.TALENT,
            viewerCount = 3120,
            likeCount = 39800,
            diamondsEarned = 1640,
            tags = listOf("Dance", "Fusion", "Culture"),
            avatarColor = Color(0xFF11998E),
            gradientColors = listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
            country = "🇧🇩 Rajshahi",
            level = 18
        )
    )

    val samplePartyRooms = listOf(
        PartyRoom(
            id = "party_1",
            titleBn = "রাতজাগা বন্ধুদের গানের আড্ডা 🎙️",
            titleEn = "Midnight Singing Hangout 🎙️",
            hostName = "তৌহিদ (Tawhid)",
            hostLevel = 31,
            category = "গান ও কবিতা",
            listenersCount = 284,
            seats = listOf(
                PartySeat(0, "তৌহিদ (Host)", occupantLevel = 31, isMuted = false, isSpeaking = true, avatarColor = Color(0xFFFF2D55)),
                PartySeat(1, "সুমাইয়া", occupantLevel = 14, isMuted = false, isSpeaking = false, avatarColor = Color(0xFF00F2FE)),
                PartySeat(2, "আরিফ", occupantLevel = 22, isMuted = true, isSpeaking = false, avatarColor = Color(0xFFFFD700)),
                PartySeat(3, "মেহজাবিন", occupantLevel = 18, isMuted = false, isSpeaking = true, avatarColor = Color(0xFF00E676)),
                PartySeat(4, null),
                PartySeat(5, null)
            ),
            bannerColor = Color(0xFF4C1D95)
        ),
        PartyRoom(
            id = "party_2",
            titleBn = "বাংলাদেশ ক্রিকেট ফ্যান ক্লাব ডিসকাশন 🏏",
            titleEn = "Bangladesh Cricket Fan Club 🏏",
            hostName = "সাকিব ফ্যান",
            hostLevel = 24,
            category = "খেলাধুলা",
            listenersCount = 512,
            seats = listOf(
                PartySeat(0, "সাকিব ফ্যান", occupantLevel = 24, isMuted = false, isSpeaking = true, avatarColor = Color(0xFF00E676)),
                PartySeat(1, "তামিম ভাই", occupantLevel = 19, isMuted = false, isSpeaking = false, avatarColor = Color(0xFFFF9800)),
                PartySeat(2, "মুশফিক ফ্যান", occupantLevel = 16, isMuted = false, isSpeaking = false, avatarColor = Color(0xFF00F2FE)),
                PartySeat(3, null),
                PartySeat(4, null),
                PartySeat(5, null)
            ),
            bannerColor = Color(0xFF065F46)
        ),
        PartyRoom(
            id = "party_3",
            titleBn = "চিল আড্ডা ও মজার গল্পগুজব ☕",
            titleEn = "Chill Talk & Fun Stories ☕",
            hostName = "নীলাঞ্জনা",
            hostLevel = 27,
            category = "আড্ডা",
            listenersCount = 198,
            seats = listOf(
                PartySeat(0, "নীলাঞ্জনা", occupantLevel = 27, isMuted = false, isSpeaking = true, avatarColor = Color(0xFFFF5E7E)),
                PartySeat(1, "রাব্বি", occupantLevel = 12, isMuted = false, isSpeaking = false, avatarColor = Color(0xFF7928CA)),
                PartySeat(2, null),
                PartySeat(3, null),
                PartySeat(4, null),
                PartySeat(5, null)
            ),
            bannerColor = Color(0xFF831843)
        )
    )

    val simulatedUserCommentsBn = listOf(
        "অনেক সুন্দর হচ্ছে গানটা! ❤️",
        "জয় লাইভে তোমাকে দেখে খুব ভালো লাগলো!",
        "হ্যালো আপু, কেমন আছো?",
        "একটা রবীন্দ্রসঙ্গীত শোনাবেন প্লিজ? 🎶",
        "ভাইয়া বিগ ফ্যান! চিটাগং থেকে দেখছি 🔥",
        "অসাধারণ পারফরম্যান্স! চালিয়ে যান 👏",
        "আমার পক্ষ থেকে লাভ রিঅ্যাক্ট পাঠিয়ে দিলাম 💕",
        "লাইভটা বন্ধুদের সাথে শেয়ার করে দিয়েছি!",
        "সাউন্ড কোয়ালিটি অনেক পরিষ্কার আসছে 👌",
        "জয় লাইভের পরিবেশ সত্যিই দারুণ!"
    )

    val simulatedUserCommentsEn = listOf(
        "Amazing stream! Loving the vibe ❤️",
        "Hello from Joy Live community! 👋",
        "Awesome song selection today 🎵",
        "You are so talented! Keep it up 🔥",
        "Sending lots of love and support 💕",
        "Great audio and video quality 👌",
        "Shared this live with my squad!",
        "Big fan! Watching every day ⭐",
        "Can you give a shoutout please?",
        "Joy Live is truly the best! 🚀"
    )

    val rechargePackages = listOf(
        RechargePackage("pkg_50", coins = 500, bonusCoins = 50, priceBdt = 50),
        RechargePackage("pkg_100", coins = 1200, bonusCoins = 200, priceBdt = 100, isPopular = true),
        RechargePackage("pkg_250", coins = 3000, bonusCoins = 600, priceBdt = 250),
        RechargePackage("pkg_500", coins = 7000, bonusCoins = 1800, priceBdt = 500)
    )
}
