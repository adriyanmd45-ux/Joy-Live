package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JoyLiveRepository
import com.example.model.GiftItem
import com.example.model.Language
import com.example.ui.theme.JoyCyan
import com.example.ui.theme.JoyGold
import com.example.ui.theme.JoyPinkPrimary
import com.example.ui.theme.JoyPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftShopModal(
    userCoins: Int,
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onSendGift: (GiftItem) -> Unit,
    onOpenRecharge: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedGift by remember { mutableStateOf(JoyLiveRepository.giftCatalog.first()) }
    val isBangla = currentLanguage == Language.BN

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1B172E),
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.4f))
        },
        modifier = modifier.testTag("gift_shop_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header: Balance + Recharge Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF282245), shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Joy Coins",
                        tint = JoyGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$userCoins",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(JoyPinkPrimary, shape = CircleShape)
                            .clickable { onOpenRecharge() }
                            .testTag("recharge_quick_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Coins",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = if (isBangla) "🎁 জয় গিফট শপ" else "🎁 Joy Gift Shop",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6-Gift Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp)
            ) {
                items(JoyLiveRepository.giftCatalog) { gift ->
                    val isSelected = selectedGift.id == gift.id
                    val canAfford = userCoins >= gift.coinPrice

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) Color(0xFF342B57) else Color(0xFF231D3D)
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) gift.rarityColor else Color.White.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedGift = gift }
                            .padding(10.dp)
                            .testTag("gift_item_${gift.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = gift.iconEmoji,
                                fontSize = 34.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBangla) gift.nameBn else gift.nameEn,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = if (canAfford) JoyGold else Color.Gray,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${gift.coinPrice}",
                                    color = if (canAfford) JoyGold else Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Row: Send button
            val canAffordSelected = userCoins >= selectedGift.coinPrice
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBangla) "নির্বাচিত: ${selectedGift.nameBn} (${selectedGift.coinPrice} কয়েন)"
                        else "Selected: ${selectedGift.nameEn} (${selectedGift.coinPrice} coins)",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                    if (!canAffordSelected) {
                        Text(
                            text = if (isBangla) "পর্যাপ্ত কয়েন নেই! রিচার্জ করুন" else "Insufficient coins! Top up now",
                            color = JoyPinkPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Button(
                    onClick = {
                        if (canAffordSelected) {
                            onSendGift(selectedGift)
                        } else {
                            onOpenRecharge()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAffordSelected) JoyPinkPrimary else JoyGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("send_gift_confirm_button")
                ) {
                    Icon(
                        imageVector = if (canAffordSelected) Icons.Default.Send else Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = if (canAffordSelected) Color.White else Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (canAffordSelected) {
                            if (isBangla) "পাঠান" else "Send"
                        } else {
                            if (isBangla) "রিচার্জ করুন" else "Top Up"
                        },
                        color = if (canAffordSelected) Color.White else Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
