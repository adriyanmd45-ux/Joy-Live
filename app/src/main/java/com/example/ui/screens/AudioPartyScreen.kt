package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.PartyRoom
import com.example.model.PartySeat
import com.example.ui.theme.JoyCyan
import com.example.ui.theme.JoyGold
import com.example.ui.theme.JoyPinkPrimary
import com.example.ui.theme.JoyPurple

@Composable
fun AudioPartyScreen(
    partyRooms: List<PartyRoom>,
    currentLanguage: Language,
    activeSeatRoomId: String?,
    activeSeatIndex: Int?,
    onTakeSeat: (String, Int) -> Unit,
    onLeaveSeat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isBn = currentLanguage == Language.BN

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("audio_party_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Party Room Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBn) "🎙️ ভয়েস পার্টি রুম" else "🎙️ Voice Party Rooms",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isBn) "লাইভ বন্ধুদের সাথে কথা বলুন ও গান উপভোগ করুন" else "Hang out, sing and talk live with friends",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .background(JoyPinkPrimary.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isBn) "${partyRooms.size} টি রুম সক্রিয়" else "${partyRooms.size} Active Rooms",
                        color = JoyPinkPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // List of Party Rooms
        items(partyRooms, key = { it.id }) { room ->
            PartyRoomCard(
                room = room,
                isBn = isBn,
                isUserSeatedInThisRoom = activeSeatRoomId == room.id,
                onTakeSeat = { seatIdx -> onTakeSeat(room.id, seatIdx) },
                onLeaveSeat = { onLeaveSeat(room.id) }
            )
        }
    }
}

@Composable
fun PartyRoomCard(
    room: PartyRoom,
    isBn: Boolean,
    isUserSeatedInThisRoom: Boolean,
    onTakeSeat: (Int) -> Unit,
    onLeaveSeat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .testTag("party_room_${room.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Room Title & Listeners
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBn) room.titleBn else room.titleEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (isBn) "হোস্ট: ${room.hostName} • ${room.category}" else "Host: ${room.hostName} • ${room.category}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(listOf(JoyPurple, JoyPinkPrimary)),
                            shape = CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${room.listenersCount}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6-Seat Grid (2 rows of 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (seat in room.seats.take(3)) {
                    PartySeatView(seat = seat, isBn = isBn, onSeatClick = { onTakeSeat(seat.seatIndex) })
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (seat in room.seats.drop(3).take(3)) {
                    PartySeatView(seat = seat, isBn = isBn, onSeatClick = { onTakeSeat(seat.seatIndex) })
                }
            }

            // Room Action Row
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isUserSeatedInThisRoom) {
                    OutlinedButton(
                        onClick = onLeaveSeat,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("leave_seat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "সিট ছেড়ে দিন" else "Leave Seat",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PartySeatView(
    seat: PartySeat,
    isBn: Boolean,
    onSeatClick: () -> Unit
) {
    val isOccupied = seat.occupantName != null
    val infiniteTransition = rememberInfiniteTransition(label = "speaker_wave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speaking"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSeatClick() }
            .testTag("party_seat_${seat.seatIndex}")
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Soundwave ripple if speaking
            if (seat.isSpeaking) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .scale(waveScale)
                        .background(JoyCyan.copy(alpha = 0.35f), shape = CircleShape)
                )
            }

            // Avatar or Empty Seat Ring
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isOccupied) seat.avatarColor else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = if (seat.isSpeaking) 2.dp else 1.dp,
                        color = if (seat.isSpeaking) JoyCyan else Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isOccupied) {
                    Text(
                        text = seat.occupantName?.take(1) ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Empty Seat",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Mic Mute indicator badge
            if (isOccupied && seat.isMuted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .background(Color(0xFFEF4444), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MicOff,
                        contentDescription = "Muted",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isOccupied) seat.occupantName ?: "" else (if (isBn) "সিট ${seat.seatIndex + 1}" else "Seat ${seat.seatIndex + 1}"),
            fontSize = 11.sp,
            fontWeight = if (isOccupied) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isOccupied) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
