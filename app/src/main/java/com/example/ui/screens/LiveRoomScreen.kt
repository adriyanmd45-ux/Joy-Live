package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.FloatingHeart
import com.example.model.GiftItem
import com.example.model.Language
import com.example.model.LiveStream
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.components.LiveChatList
import com.example.ui.theme.JoyCyan
import com.example.ui.theme.JoyGold
import com.example.ui.theme.JoyPinkPrimary
import com.example.ui.theme.JoyPurple

@Composable
fun LiveRoomScreen(
    stream: LiveStream,
    chatMessages: List<ChatMessage>,
    floatingHearts: List<FloatingHeart>,
    currentLanguage: Language,
    lastGiftSent: GiftItem?,
    lastGiftSender: String,
    onClose: () -> Unit,
    onTapScreen: (Float) -> Unit,
    onSendComment: (String) -> Unit,
    onOpenGiftShop: () -> Unit,
    onToggleFollow: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val isBn = currentLanguage == Language.BN
    var commentInput by remember { mutableStateOf("") }

    // Heart Button Scale Animation
    var heartTapped by remember { mutableStateOf(false) }
    val heartScale by animateFloatAsState(
        targetValue = if (heartTapped) 1.35f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        finishedListener = { heartTapped = false },
        label = "heart_pulse"
    )

    // Animated Live Stage Pulse
    val infiniteTransition = rememberInfiniteTransition(label = "stage_anim")
    val stageGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        val ratio = (offset.x / size.width).coerceIn(0.1f, 0.9f)
                        onTapScreen(ratio)
                    }
                )
            }
            .testTag("live_room_screen")
    ) {
        // Stage Visualizer / Background Video Simulator
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            stream.gradientColors.first().copy(alpha = 0.75f),
                            Color(0xFF0F0B1E),
                            Color.Black
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Broadcaster Stage Center Avatar with pulsating soundwaves
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .scale(stageGlow)
                    .background(
                        Brush.radialGradient(
                            listOf(JoyPinkPrimary.copy(alpha = 0.45f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .background(
                            Brush.linearGradient(listOf(JoyPinkPrimary, JoyCyan)),
                            shape = CircleShape
                        )
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(stream.avatarColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stream.hostName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 44.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stream.hostName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = if (isBn) stream.titleBn else stream.titleEn,
                    color = JoyCyan,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }

        // Floating Hearts Canvas (Renders over video stage)
        FloatingHeartsCanvas(
            hearts = floatingHearts,
            modifier = Modifier.fillMaxSize()
        )

        // Floating Gift Toast Banner
        if (lastGiftSent != null && lastGiftSender.isNotBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp, top = 100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(JoyPinkPrimary, Color(0xFF7928CA).copy(alpha = 0.95f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("gift_animation_banner")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = lastGiftSent.iconEmoji,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = lastGiftSender,
                            color = JoyGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (isBn) "${lastGiftSent.nameBn} উপহার পাঠালেন!" else "Sent ${lastGiftSent.nameEn}!",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Top Broadcaster HUD
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Host pill: Avatar + Name + Follow
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(24.dp))
                        .padding(start = 4.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(stream.avatarColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stream.hostName.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = stream.hostName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "${stream.diamondsEarned} 💎",
                            color = JoyGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    // Follow Button
                    Box(
                        modifier = Modifier
                            .background(
                                if (stream.isFollowing) Color(0xFF333333) else JoyPinkPrimary,
                                shape = CircleShape
                            )
                            .clickable { onToggleFollow() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("follow_host_button")
                    ) {
                        Text(
                            text = if (stream.isFollowing) (if (isBn) "ফলোয়িং" else "Following") else (if (isBn) "ফলো" else "Follow"),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Right: Viewers Count + Close Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFFF2D55), shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${stream.viewerCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                            .testTag("close_live_room_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Live Room",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Bottom HUD: Chat Stream + Actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Live Chat Messages
            LiveChatList(
                messages = chatMessages,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(210.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action Row: Comment Bar + Gift + Share + Like Heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Comment Input Box
                TextField(
                    value = commentInput,
                    onValueChange = { commentInput = it },
                    placeholder = {
                        Text(
                            text = if (isBn) "মন্তব্য লিখুন..." else "Say something...",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Black.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.5f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(22.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (commentInput.isNotBlank()) {
                                onSendComment(commentInput)
                                commentInput = ""
                            }
                        }
                    ),
                    trailingIcon = {
                        if (commentInput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    onSendComment(commentInput)
                                    commentInput = ""
                                },
                                modifier = Modifier.testTag("send_comment_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = JoyCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("comment_input_field")
                )

                // Gift Shop Button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            Brush.linearGradient(listOf(JoyPinkPrimary, JoyPurple)),
                            shape = CircleShape
                        )
                        .clickable { onOpenGiftShop() }
                        .testTag("open_gift_shop_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎁",
                        fontSize = 22.sp
                    )
                }

                // Share Button
                IconButton(
                    onClick = onShare,
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                        .testTag("share_live_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Heart Like Pulse Button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .scale(heartScale)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFFFF2D55), Color(0xFFFF5E7E))),
                            shape = CircleShape
                        )
                        .clickable {
                            heartTapped = true
                            onTapScreen(0.85f)
                        }
                        .testTag("like_heart_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like Heart",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
