package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EditingFeature
import com.example.ui.HomeTab
import com.example.ui.PhotoEditorViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PhotoEditorViewModel, onFeatureClick: (EditingFeature) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val features = EditingFeature.values()

    // Smooth vertical scroll for content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp) // Generous space to avoid overlaps with the fixed bottom bar
    ) {
        // Sleek Interface Theme Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: LuminaAI Brand
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(PurpleNeon, PinkAccent)))
                        .drawBehind {
                            drawCircle(
                                color = PurpleNeon.copy(alpha = 0.5f),
                                radius = size.width * 0.8f,
                                center = Offset(size.width / 2, size.height / 2)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Lumina",
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "AI",
                            color = PurpleNeon,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    Text(
                        text = "PRO EDITOR",
                        color = TextGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            // Right: Interactive Theme Controls (Star Premium Button & User Avatar)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Interactive star button (Saves or purchases premium)
                IconButton(
                    onClick = { viewModel.purchasePremium() },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, GlassWhite, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = if (state.session.isPremium) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Premium Badge",
                        tint = if (state.session.isPremium) Color(0xFFFEB800) else TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Interactive User Avatar
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardColor)
                        .border(1.5.dp, PurpleNeon.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .clickable { viewModel.setTab(HomeTab.PROFILE) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = PinkAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Beautiful glass search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search AI filters, styles...") },
            leadingIcon = { Icon(Icons.Default.Search, "Search", tint = PurpleNeon) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = PurpleNeon,
                unfocusedBorderColor = GlassWhite,
                focusedLabelColor = PurpleNeon,
                unfocusedLabelColor = TextGray,
                focusedContainerColor = CardColor.copy(alpha = 0.5f),
                unfocusedContainerColor = CardColor.copy(alpha = 0.5f)
            ),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Hero Promotion Section (Sleek Interface Style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, GlassWhite, RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF111827), Color(0xFF050816))
                    )
                )
                .drawBehind {
                    // Accent glowing circle in the upper right
                    drawCircle(
                        color = PurpleNeon.copy(alpha = 0.15f),
                        radius = size.width * 0.4f,
                        center = Offset(size.width * 0.95f, size.height * 0.05f)
                    )
                }
                .clickable { onFeatureClick(EditingFeature.AVATAR) }
                .padding(20.dp)
        ) {
            Column {
                // New Hot Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PinkAccent)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "NEW",
                        color = TextWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "AI Magic Avatar",
                    color = TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Create 50+ unique styles of yourself using high-definition generative AI models.",
                    color = TextGray,
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onFeatureClick(EditingFeature.AVATAR) },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "Try Now",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: AI Tools / Workflows
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI Tools",
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
                color = PurpleNeon,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* Visual click animation */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of 6 AI feature cards (User specified: AI Enhance, BG Remove, Cartoon Maker, Old Photo Restore, AI Avatar, Blur Fix)
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val filteredFeatures = features.filter {
                it.displayName.contains(searchQuery, ignoreCase = true) ||
                        it.rawDescription.contains(searchQuery, ignoreCase = true)
            }

            // Display in pairs (2 columns)
            for (i in filteredFeatures.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FeatureCard(
                        feature = filteredFeatures[i],
                        icon = getFeatureIcon(filteredFeatures[i]),
                        modifier = Modifier.weight(1f),
                        onClick = { onFeatureClick(filteredFeatures[i]) }
                    )

                    if (i + 1 < filteredFeatures.size) {
                        FeatureCard(
                            feature = filteredFeatures[i + 1],
                            icon = getFeatureIcon(filteredFeatures[i + 1]),
                            modifier = Modifier.weight(1f),
                            onClick = { onFeatureClick(filteredFeatures[i + 1]) }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Section Title: Trending Creations (Fulfills requested visual elements and provides sample items)
        Text(
            text = "Trending AI Creative Styles",
            color = TextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val trendingItems = listOf(
            TrendingItem("Synthwave Sunset", EditingFeature.CARTOON, "https://picsum.photos/id/237/300/400", "Pixar Cartoon"),
            TrendingItem("Cybernetic Soldier", EditingFeature.AVATAR, "https://picsum.photos/id/1011/300/400", "Cyberpunk Avatar"),
            TrendingItem("Retro Glow", EditingFeature.COLORIZE, "https://picsum.photos/id/64/300/400", "Old Restore"),
            TrendingItem("Sharp Studio Portrait", EditingFeature.ENHANCE, "https://picsum.photos/id/1025/300/400", "AI Enhance")
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(trendingItems) { item ->
                TrendingCard(item = item) {
                    onFeatureClick(item.targetFeature)
                }
            }
        }
    }
}

fun getFeatureGlowColor(feature: EditingFeature): Color {
    return when (feature) {
        EditingFeature.ENHANCE -> Color(0xFF3B82F6) // Blue
        EditingFeature.BG_REMOVE -> Color(0xFF8B5CF6) // Purple
        EditingFeature.CARTOON -> Color(0xFFEC4899) // Pink
        EditingFeature.COLORIZE -> Color(0xFF06B6D4) // Cyan
        EditingFeature.AVATAR -> PurpleNeon // Purple
        EditingFeature.BLUR_FIX -> Color(0xFF10B981) // Emerald
    }
}

@Composable
fun FeatureCard(
    feature: EditingFeature,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val glowColor = getFeatureGlowColor(feature)
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor.copy(alpha = 0.5f)),
        modifier = modifier
            .height(135.dp)
            .border(1.dp, GlassWhite, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .drawBehind {
                // Sleek subtle glow behind the container
                drawCircle(
                    color = glowColor.copy(alpha = 0.05f),
                    radius = size.width * 0.4f,
                    center = Offset(size.width * 0.2f, size.height * 0.3f)
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Elegant circle shape icon container with neon shadow/glow glow color tint
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(glowColor.copy(alpha = 0.15f))
                    .border(1.dp, glowColor.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = feature.displayName,
                    tint = glowColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = feature.displayName,
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = feature.rawDescription,
                    color = TextGray,
                    fontSize = 9.sp,
                    maxLines = 2,
                    lineHeight = 11.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

data class TrendingItem(
    val title: String,
    val targetFeature: EditingFeature,
    val imageUrlPlaceholder: String,
    val styleBadgeName: String
)

@Composable
fun TrendingCard(item: TrendingItem, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .size(130.dp, 170.dp)
            .border(1.dp, GlassWhite, RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Simulated local static rendering instead of breaking on offline coil
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2E1065), Color(0xFF03001C))
                        )
                    )
            ) {
                // Generative abstract vectors as backgrounds for robust preview offline
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawCircle(
                                color = PinkAccent.copy(alpha = 0.15f),
                                center = Offset(size.width, 0f),
                                radius = size.height / 2
                            )
                        }
                )
                
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = PurpleNeon.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )
            }

            // Foreground info overlays
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Style designation badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PinkAccent.copy(alpha = 0.85f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.styleBadgeName,
                        color = TextWhite,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Title
                Column {
                    Text(
                        text = item.title,
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Tap to Apply",
                        color = TextGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

fun getFeatureIcon(feature: EditingFeature): ImageVector {
    return when (feature) {
        EditingFeature.ENHANCE -> Icons.Default.AutoAwesome
        EditingFeature.BG_REMOVE -> Icons.Default.FilterFrames
        EditingFeature.CARTOON -> Icons.Default.Brush
        EditingFeature.COLORIZE -> Icons.Default.HistoryToggleOff
        EditingFeature.AVATAR -> Icons.Default.Face
        EditingFeature.BLUR_FIX -> Icons.Default.Portrait
    }
}
