package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PhotoEditorViewModel
import com.example.ui.theme.*

@Composable
fun ProfileScreen(viewModel: PhotoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()

    // Config options
    var darkModeSim by remember { mutableStateOf(true) }
    var notificationsSim by remember { mutableStateOf(true) }
    var adsBlockSim by remember { mutableStateOf(!state.session.isPremium) }
    var selectedLanguage by remember { mutableStateOf("English (US)") }

    var showPlansDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 90.dp)
    ) {
        // Section Title
        Text(
            "Studio Profile",
            color = TextWhite,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        // Glass User Profile Card
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassWhite, RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Glow avatar bubble
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(PurpleNeon, PinkAccent)
                            )
                        )
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(CardColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "User Avatar",
                        tint = PinkAccent,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = state.session.name,
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = state.session.email,
                    color = TextGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Premium Status Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (state.session.isPremium) PinkAccent.copy(alpha = 0.2f)
                            else GlassWhite
                        )
                        .border(
                            1.dp,
                            if (state.session.isPremium) PinkAccent else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showPlansDialog = true }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.WorkspacePremium,
                        null,
                        tint = if (state.session.isPremium) PinkAccent else TextGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (state.session.isPremium) "ACTIVE PREMIUM PASS" else "UPGRADE TO HD PREMIUM",
                        color = if (state.session.isPremium) PinkAccent else TextWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Subscription Section (If not premium, show visual options)
        if (!state.session.isPremium) {
            Text(
                "Subscription Plans",
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlanCard(
                    title = "Weekly Custom",
                    price = "$1.99",
                    badge = "Basic Creator",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.purchasePremium() }
                )
                PlanCard(
                    title = "Yearly Pass",
                    price = "$19.99/yr",
                    badge = "Best Offer • 80% Off",
                    isFeature = true,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.purchasePremium() }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Settings Section
        Text(
            "Settings & Privacy",
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CardColor)
                .border(1.dp, GlassWhite, RoundedCornerShape(20.dp))
        ) {
            // Dark Mode
            SettingsToggleRow(
                icon = Icons.Default.DarkMode,
                title = "AMOLED Dark Mode",
                subtitle = "Ensures futuristic high-contrast UI",
                checked = darkModeSim,
                onCheckedChange = { darkModeSim = it }
            )

            Divider(color = GlassWhite, thickness = 1.dp)

            // Notifications
            SettingsToggleRow(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                subtitle = "Alert on weekly AI effects releases",
                checked = notificationsSim,
                onCheckedChange = { notificationsSim = it }
            )

            Divider(color = GlassWhite, thickness = 1.dp)

            // Language picker
            SettingsNavigateRow(
                icon = Icons.Default.Language,
                title = "Language",
                value = selectedLanguage,
                onClick = {
                    selectedLanguage = if (selectedLanguage == "English (US)") "Spanish (ES)" else "English (US)"
                }
            )

            Divider(color = GlassWhite, thickness = 1.dp)

            // Privacy settings
            SettingsNavigateRow(
                icon = Icons.Default.Security,
                title = "Privacy & Encryption",
                value = "Secured locally",
                onClick = { /* Simulated */ }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Logout Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x1FFF4D4D))
                .border(1.dp, Color(0x7FFF4D4D), RoundedCornerShape(16.dp))
                .clickable { viewModel.logout() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Logout, "Log Out", tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOGOUT ARTIST ACCOUNT",
                    color = Color(0xFFFF5252),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }

    // Interactive Dialog Simulator
    if (showPlansDialog) {
        AlertDialog(
            onDismissRequest = { showPlansDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.purchasePremium()
                    showPlansDialog = false
                }) {
                    Text("ACTIVATE FREE PASS", color = PinkAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPlansDialog = false }) {
                    Text("CLOSE", color = TextWhite)
                }
            },
            containerColor = CardColor,
            title = {
                Text(
                    "HD Premium Access",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    "You will bypass AdMob video players, unlock full HD exports, gain customizable background replacement presets, and increase AI model processing performance instantly.",
                    color = TextGray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier.border(1.dp, GlassWhite, RoundedCornerShape(22.dp))
        )
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    badge: String,
    isFeature: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(130.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isFeature) PurpleNeon.copy(alpha = 0.15f) else CardColor)
            .border(
                width = 1.dp,
                brush = if (isFeature) Brush.linearGradient(listOf(PurpleNeon, PinkAccent)) else borderStrokeSolid(GlassWhite),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isFeature) PinkAccent else GlassWhite)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge.uppercase(),
                    color = TextWhite,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(title, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(price, color = if (isFeature) PinkAccent else TextWhite, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GlassWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, title, tint = PurpleNeon, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = TextGray, fontSize = 11.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextWhite,
                checkedTrackColor = PinkAccent,
                uncheckedThumbColor = TextGray,
                uncheckedTrackColor = GlassWhite
            )
        )
    }
}

@Composable
fun SettingsNavigateRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GlassWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, title, tint = PurpleNeon, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.ChevronRight, null, tint = TextGray, modifier = Modifier.size(16.dp))
        }
    }
}

private fun borderStrokeSolid(color: Color) = Brush.linearGradient(listOf(color, color))
