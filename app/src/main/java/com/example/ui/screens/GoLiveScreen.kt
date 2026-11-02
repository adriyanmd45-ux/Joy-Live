package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.camera.CameraPreview
import com.example.model.ChatMessage
import com.example.model.FloatingHeart
import com.example.model.Language
import com.example.model.StreamCategory
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.components.LiveChatList
import com.example.ui.theme.JoyCyan
import com.example.ui.theme.JoyGold
import com.example.ui.theme.JoyPinkPrimary
import com.example.ui.theme.JoyPurple

@Composable
fun GoLiveScreen(
    isHostingLive: Boolean,
    streamTitle: String,
    streamCategory: StreamCategory,
    durationSeconds: Long,
    viewersCount: Int,
    diamondsEarned: Long,
    isFrontCamera: Boolean,
    isTorchOn: Boolean,
    isMicMuted: Boolean,
    beautyFilterLevel: Float,
    chatMessages: List<ChatMessage>,
    floatingHearts: List<FloatingHeart>,
    currentLanguage: Language,
    showSummaryDialog: Boolean,
    onStartLive: (String, StreamCategory) -> Unit,
    onEndLive: () -> Unit,
    onDismissSummary: () -> Unit,
    onToggleCameraFacing: () -> Unit,
    onToggleTorch: () -> Unit,
    onToggleMic: () -> Unit,
    onBeautyFilterChange: (Float) -> Unit,
    onSendComment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBn = currentLanguage == Language.BN

    var titleInput by remember { mutableStateOf(streamTitle) }
    var selectedCategory by remember { mutableStateOf(streamCategory) }
    var showBeautySlider by remember { mutableStateOf(false) }

    // Check Camera & Audio Permissions
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] == true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("go_live_screen")
    ) {
        // Camera Preview Feed
        CameraPreview(
            isFrontCamera = isFrontCamera,
            isTorchOn = isTorchOn,
            beautyFilterLevel = beautyFilterLevel,
            modifier = Modifier.fillMaxSize()
        )

        // Floating Hearts Canvas
        FloatingHeartsCanvas(
            hearts = floatingHearts,
            modifier = Modifier.fillMaxSize()
        )

        if (!isHostingLive) {
            // Setup Screen Overlay (Before going live)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Setup Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isBn) "🎥 জয় লাইভ স্টুডিও" else "🎥 Joy Live Studio",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = onToggleCameraFacing,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                                .testTag("flip_camera_setup_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraIos,
                                contentDescription = "Flip Camera",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = onToggleTorch,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                                .testTag("toggle_torch_setup_button")
                        ) {
                            Icon(
                                imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Torch",
                                tint = if (isTorchOn) JoyGold else Color.White
                            )
                        }
                    }
                }

                // Center / Bottom Setup Form
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(18.dp)
                ) {
                    Text(
                        text = if (isBn) "লাইভের বিষয় ও ক্যাটাগরি নির্ধারণ করুন" else "Set Broadcast Title & Category",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        placeholder = {
                            Text(
                                text = if (isBn) "লাইভের নাম লিখুন (যেমন: সন্ধ্যার গানের আড্ডা)..." else "Stream title...",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = JoyPinkPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("stream_title_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isBn) "ক্যাটাগরি:" else "Category:",
                        color = JoyCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(StreamCategory.values().filter { it != StreamCategory.ALL }) { cat ->
                            val isSel = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSel) JoyPinkPrimary else Color.White.copy(alpha = 0.15f)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("setup_category_${cat.id}")
                            ) {
                                Text(
                                    text = if (isBn) cat.titleBn else cat.titleEn,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Start Live Button
                    Button(
                        onClick = {
                            if (!hasCameraPermission) {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.RECORD_AUDIO
                                    )
                                )
                            }
                            onStartLive(titleInput, selectedCategory)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = JoyPinkPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_broadcasting_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "🔴 লাইভ শুরু করুন" else "🔴 Start Live Stream",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            // Currently Broadcasting Live HUD
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Live Telemetry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Timer & Viewer Info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.65f), shape = RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFFFF2D55), shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val minutes = durationSeconds / 60
                        val secs = durationSeconds % 60
                        Text(
                            text = String.format("%02d:%02d", minutes, secs),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = JoyCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$viewersCount",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "$diamondsEarned 💎",
                            color = JoyGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // End Live Button
                    Button(
                        onClick = onEndLive,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("end_live_stream_button")
                    ) {
                        Text(
                            text = if (isBn) "শেষ করুন" else "End",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Beauty Filter Slider (if open)
                if (showBeautySlider) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.8f))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (isBn) "✨ বিউটি ফিল্টার: ${(beautyFilterLevel * 100).toInt()}%" else "✨ Beauty Filter: ${(beautyFilterLevel * 100).toInt()}%",
                            color = JoyPinkPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Slider(
                            value = beautyFilterLevel,
                            onValueChange = onBeautyFilterChange,
                            colors = SliderDefaults.colors(
                                thumbColor = JoyPinkPrimary,
                                activeTrackColor = JoyPinkPrimary
                            ),
                            modifier = Modifier.testTag("beauty_filter_slider")
                        )
                    }
                }

                // Bottom: Chat + Host Controls Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    // Live Viewer Chat Stream
                    LiveChatList(
                        messages = chatMessages,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(180.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Host Hardware Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onToggleCameraFacing,
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                                .testTag("host_flip_camera_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraIos,
                                contentDescription = "Flip Camera",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = onToggleTorch,
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                                .testTag("host_toggle_torch_button")
                        ) {
                            Icon(
                                imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Torch",
                                tint = if (isTorchOn) JoyGold else Color.White
                            )
                        }

                        IconButton(
                            onClick = onToggleMic,
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                                .testTag("host_toggle_mic_button")
                        ) {
                            Icon(
                                imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Mic",
                                tint = if (isMicMuted) Color(0xFFEF4444) else JoyCyan
                            )
                        }

                        IconButton(
                            onClick = { showBeautySlider = !showBeautySlider },
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                                .testTag("host_toggle_beauty_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Beauty Filter",
                                tint = JoyPinkPrimary
                            )
                        }
                    }
                }
            }
        }

        // Live Ended Achievement Summary Modal
        if (showSummaryDialog) {
            AlertDialog(
                onDismissRequest = onDismissSummary,
                containerColor = Color(0xFF1B172E),
                title = {
                    Text(
                        text = if (isBn) "🎉 লাইভ সফলভাবে সম্পন্ন হয়েছে!" else "🎉 Live Stream Completed!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (isBn) "আপনার আজকের লাইভ পরিসংখ্যান:" else "Your Broadcast Summary:",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$viewersCount", color = JoyCyan, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(text = if (isBn) "মোট দর্শক" else "Total Views", color = Color.Gray, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$diamondsEarned 💎", color = JoyGold, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(text = if (isBn) "ডায়মন্ড আয়" else "Diamonds", color = Color.Gray, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val mins = durationSeconds / 60
                                val s = durationSeconds % 60
                                Text(text = "${mins}m ${s}s", color = JoyPinkPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(text = if (isBn) "সময়কাল" else "Duration", color = Color.Gray, fontSize = 11.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onDismissSummary,
                        colors = ButtonDefaults.buttonColors(containerColor = JoyPinkPrimary),
                        modifier = Modifier.testTag("dismiss_summary_button")
                    ) {
                        Text(
                            text = if (isBn) "ঠিক আছে" else "Done",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}
