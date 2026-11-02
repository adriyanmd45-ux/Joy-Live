package com.example.ui.screens

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.JoyLiveRepository
import com.example.model.Language
import com.example.model.RechargePackage
import com.example.model.UserProfile
import com.example.ui.theme.JoyCyan
import com.example.ui.theme.JoyGold
import com.example.ui.theme.JoyOrange
import com.example.ui.theme.JoyPinkPrimary
import com.example.ui.theme.JoyPurple

@Composable
fun WalletScreen(
    userProfile: UserProfile,
    currentLanguage: Language,
    onClaimDailyBonus: () -> Unit,
    onRecharge: (RechargePackage, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isBn = currentLanguage == Language.BN
    var selectedPackage by remember { mutableStateOf<RechargePackage?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("bKash (বিকাশ)") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("wallet_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2E1065), JoyPinkPrimary, Color(0xFF1E1B4B))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = if (isBn) "আমার জয় ওয়ালেট ব্যালেন্স" else "My Joy Wallet Balance",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Joy Coins
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(JoyGold.copy(alpha = 0.25f), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = JoyGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${userProfile.coinBalance}",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = if (isBn) "কয়েন ব্যালেন্স" else "Coins",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Joy Diamonds
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(JoyCyan.copy(alpha = 0.25f), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = JoyCyan,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${userProfile.diamondBalance}",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = if (isBn) "ডায়মন্ড আয়" else "Diamonds",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Check-in Bonus Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎁", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBn) "দৈনিক ফ্রি বোনাস" else "Daily Free Bonus",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isBn) "প্রতিদিন জয় লাইভে লগইন করে ১০০ ফ্রি কয়েন গ্রহণ করুন!" else "Claim 100 free coins every day on Joy Live!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = onClaimDailyBonus,
                        enabled = !userProfile.dailyBonusClaimed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = JoyGold,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("claim_daily_bonus_button")
                    ) {
                        Text(
                            text = if (userProfile.dailyBonusClaimed) {
                                if (isBn) "দাবি করা হয়েছে" else "Claimed"
                            } else {
                                if (isBn) "দাবি করুন" else "Claim 100"
                            },
                            color = if (userProfile.dailyBonusClaimed) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Recharge Packages Header
        item {
            Text(
                text = if (isBn) "🪙 কয়েন রিচার্জ প্যাকেজ" else "🪙 Coin Recharge Packages",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Package Cards
        items(JoyLiveRepository.rechargePackages) { pkg ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedPackage = pkg }
                    .border(
                        width = if (pkg.isPopular) 1.5.dp else 0.5.dp,
                        color = if (pkg.isPopular) JoyPinkPrimary else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("recharge_pkg_${pkg.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(JoyGold.copy(alpha = 0.2f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🪙", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${pkg.coins} Coins",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (pkg.bonusCoins > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(JoyPinkPrimary, shape = RoundedCornerShape(8.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "+${pkg.bonusCoins} Bonus",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Text(
                                text = if (pkg.isPopular) "🔥 সবচেয়ে জনপ্রিয় প্যাকেজ" else "তাৎক্ষণিক ডেলিভারি",
                                color = if (pkg.isPopular) JoyOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Button(
                        onClick = { selectedPackage = pkg },
                        colors = ButtonDefaults.buttonColors(containerColor = JoyPinkPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "৳ ${pkg.priceBdt}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // Checkout Confirmation Dialog
    if (selectedPackage != null) {
        val pkg = selectedPackage!!
        val paymentMethods = listOf("bKash (বিকাশ)", "Nagad (নগদ)", "Rocket (রকেট)", "Debit/Credit Card")

        AlertDialog(
            onDismissRequest = { selectedPackage = null },
            containerColor = Color(0xFF1B172E),
            title = {
                Text(
                    text = if (isBn) "পেমেন্ট সম্পন্ন করুন" else "Confirm Recharge",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isBn) "আপনি ${pkg.coins + pkg.bonusCoins} কয়েন রিচার্জ করছেন (৳ ${pkg.priceBdt})"
                        else "Recharging ${pkg.coins + pkg.bonusCoins} coins for ৳ ${pkg.priceBdt}",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isBn) "পেমেন্ট মাধ্যম বেছে নিন:" else "Select Payment Method:",
                        color = JoyCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    paymentMethods.forEach { method ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = method }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == method,
                                onClick = { selectedPaymentMethod = method },
                                colors = RadioButtonDefaults.colors(selectedColor = JoyPinkPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = method,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRecharge(pkg, selectedPaymentMethod)
                        selectedPackage = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JoyPinkPrimary),
                    modifier = Modifier.testTag("confirm_payment_button")
                ) {
                    Text(
                        text = if (isBn) "নিশ্চিত করুন (৳ ${pkg.priceBdt})" else "Confirm (৳ ${pkg.priceBdt})",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPackage = null }) {
                    Text(text = if (isBn) "বাতিল" else "Cancel", color = Color.Gray)
                }
            }
        )
    }
}
