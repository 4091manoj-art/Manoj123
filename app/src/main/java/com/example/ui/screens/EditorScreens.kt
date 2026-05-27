package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EditingFeature
import com.example.ui.EditorStep
import com.example.ui.PhotoEditorViewModel
import com.example.ui.theme.*

@Composable
fun EditorWorkflowScreen(viewModel: PhotoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()

    val feature = state.activeFeature ?: return Box(
        modifier = Modifier.fillMaxSize().background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Text("No Active Feature selected. Please return Home.", color = TextWhite)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        when (state.editorStep) {
            EditorStep.ChooseSource -> UploadImageScreen(feature, viewModel)
            EditorStep.Processing -> ProcessingScreen(state.progress)
            EditorStep.ActiveTool -> ActiveToolScreen(feature, state, viewModel)
            EditorStep.Result -> ResultScreen(feature, state, viewModel)
        }

        // Global Rewarded Ad Player Simulator (CRITICAL Monetization workflow requested by user)
        if (state.isRewardedAdShowing) {
            RewardedAdPlayer(state, viewModel)
        }
    }
}

// 1. Upload/Choose Image Panel
@Composable
fun UploadImageScreen(feature: EditingFeature, viewModel: PhotoEditorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(bottom = 90.dp)
    ) {
        // App top header
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.resetEditor() }) {
                Icon(Icons.Default.ArrowBack, "Back to home", tint = TextWhite)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(feature.displayName, color = TextWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Select file to begin AI processing", color = TextGray, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big, glowing interactive drag-n-drop upload field area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    2.dp,
                    Brush.sweepGradient(listOf(PurpleNeon, PinkAccent, PurpleNeon)),
                    RoundedCornerShape(24.dp)
                )
                .background(CardColor.copy(alpha = 0.5f))
                .clickable {
                    // Tap triggers instant preset selected to keep demo working
                    viewModel.selectPresetImage("preset_standard")
                },
            contentAlignment = Alignment.Center
        ) {
            // Internal grid elements
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PinkAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CloudUpload,
                        "Upload Cloud",
                        tint = PinkAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    "Click or Drag & Drop Photo",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Supports JPG, PNG, WEBP (Max 20MB)",
                    color = TextGray,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Actions Row: Gallery or Camera (Simulates inputs)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Button(
                onClick = { viewModel.selectPresetImage("preset_user_gallery") },
                colors = ButtonDefaults.buttonColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .border(1.dp, GlassWhite, RoundedCornerShape(16.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PhotoLibrary, "Gallery", tint = PurpleNeon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("FROM GALLERY", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Button(
                onClick = { viewModel.selectPresetImage("preset_live_camera") },
                colors = ButtonDefaults.buttonColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .border(1.dp, GlassWhite, RoundedCornerShape(16.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PhotoCamera, "Camera", tint = PinkAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("OPEN CAMERA", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Sample Presets layout (Ensures first launch is populated elegantly)
        Text(
            "Or work with sample template images:",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        val samples = listOf(
            SamplePhoto("Portrait Person", "preset_portrait"),
            SamplePhoto("Nature Landscapes", "preset_nature"),
            SamplePhoto("Old Scratched Memory", "preset_damaged"),
            SamplePhoto("Fuzzy Action Action", "preset_action")
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(samples) { sample ->
                Box(
                    modifier = Modifier
                        .size(110.dp, 120.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardColor)
                        .border(1.dp, GlassWhite, RoundedCornerShape(16.dp))
                        .clickable { viewModel.selectPresetImage(sample.imgId) }
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawCircle(
                                    Brush.linearGradient(listOf(PurpleNeon.copy(alpha = 0.3f), Color.Transparent)),
                                    radius = size.height
                                )
                            }
                    )
                    Text(
                        sample.title,
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}

data class SamplePhoto(val title: String, val imgId: String)

// 2. Beautiful Progress Screen
@Composable
fun ProcessingScreen(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                progress = progress,
                color = PurpleNeon,
                trackColor = GlassWhite,
                strokeWidth = 6.dp,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "AI is manipulating pixels...",
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Processing with neural style models (${(progress * 100).toInt()}% completed)",
                color = TextGray,
                fontSize = 12.sp
            )
        }
    }
}

// 3. Main Tools adjustment screen
@Composable
fun ActiveToolScreen(
    feature: EditingFeature,
    state: com.example.ui.PhotoEditorUiState,
    viewModel: PhotoEditorViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.resetEditor() }) {
                Icon(Icons.Default.Close, "Cancel", tint = TextWhite)
            }
            Text(
                text = feature.displayName.uppercase(),
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            IconButton(onClick = { viewModel.applyActiveAdjustments() }) {
                Icon(Icons.Default.Check, "Apply", tint = PinkAccent)
            }
        }

        // Live Preview Canvas with adaptive elements
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0C101F))
                .border(1.dp, GlassWhite, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // The output photo mockup
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        // Drawing professional radial backdrop shadow glow
                    }
            ) {
                // Background removal live simulation output
                if (feature == EditingFeature.BG_REMOVE) {
                    val bgBrush = when (state.bgFillType) {
                        "Royal" -> Brush.verticalGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8)))
                        "Green" -> Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF047857)))
                        "Cyan" -> Brush.verticalGradient(listOf(Color(0xFF06B6D4), Color(0xFF0E7490)))
                        "Space" -> Brush.linearGradient(listOf(PurpleNeon, PinkAccent))
                        else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgBrush)
                    )
                }

                // Core photo element
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardColor),
                        contentAlignment = Alignment.Center
                    ) {
                        // Icon matching active filters
                        Icon(
                            imageVector = getFeatureIcon(feature),
                            contentDescription = null,
                            tint = PinkAccent,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Live Preview Matrix",
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Preset File: ${state.originalImage ?: "Uploaded Image"}",
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }

                // Blur fix sharpen overlay strength indicator
                if (feature == EditingFeature.BLUR_FIX) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PurpleNeon.copy(alpha = 0.8f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "HD Sharpness: ${state.sharpenLevel.toInt()}%",
                            color = TextWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // AI Avatar style indicator
                if (feature == EditingFeature.AVATAR) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PinkAccent)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            state.avatarTheme.uppercase(),
                            color = TextWhite,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tool Adjustment options block based on selected feature
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardColor)
                .border(1.dp, GlassWhite, RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            when (feature) {
                EditingFeature.ENHANCE -> {
                    Column {
                        Text("Compare Before & After Split", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Drag to inspect enhancement fidelity", color = TextGray, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Slider(
                            value = state.beforeAfterSplit,
                            onValueChange = { viewModel.setBeforeAfterRatio(it) },
                            colors = SliderDefaults.colors(
                                thumbColor = PinkAccent,
                                activeTrackColor = PurpleNeon,
                                inactiveTrackColor = GlassWhite
                            )
                        )
                    }
                }
                EditingFeature.BG_REMOVE -> {
                    Column {
                        Text("Replace Isolated Background", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            BgFillButton("Transparent", state.bgFillType, viewModel)
                            BgFillButton("Royal", state.bgFillType, viewModel)
                            BgFillButton("Green", state.bgFillType, viewModel)
                            BgFillButton("Space", state.bgFillType, viewModel)
                        }
                    }
                }
                EditingFeature.CARTOON -> {
                    Column {
                        Text("Select Cartoonish Shader Option", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OptionBadgeButton("Anime Spark", state.cartoonStyle) { viewModel.updateCartoonStyle(it) }
                            OptionBadgeButton("Pixar Cute", state.cartoonStyle) { viewModel.updateCartoonStyle(it) }
                            OptionBadgeButton("Vintage", state.cartoonStyle) { viewModel.updateCartoonStyle(it) }
                        }
                    }
                }
                EditingFeature.COLORIZE -> {
                    Column {
                        Text("Scratch Repair Sensitivity", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("B&W Colorization Mode", color = TextGray, fontSize = 11.sp)
                            Switch(
                                checked = state.colorizeMode,
                                onCheckedChange = { viewModel.toggleColorize(it) },
                                colors = SwitchDefaults.colors(checkedTrackColor = PinkAccent)
                            )
                        }
                    }
                }
                EditingFeature.AVATAR -> {
                    Column {
                        Text("Choose Generation Character Motif", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OptionBadgeButton("Cyberpunk", state.avatarTheme) { viewModel.updateAvatarTheme(it) }
                            OptionBadgeButton("Synthwave", state.avatarTheme) { viewModel.updateAvatarTheme(it) }
                            OptionBadgeButton("Royal Aura", state.avatarTheme) { viewModel.updateAvatarTheme(it) }
                        }
                    }
                }
                EditingFeature.BLUR_FIX -> {
                    Column {
                        Text("AI Sharpness Deblur", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Slider(
                            value = state.sharpenLevel,
                            onValueChange = { viewModel.updateSharpenLevel(it) },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = PunkGradientRightTheme,
                                activeTrackColor = PurpleNeon
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large CTA run button
        Button(
            onClick = { viewModel.applyActiveAdjustments() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(PurpleNeon, PinkAccent))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RUN ARTISTIC GENERATION",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun BgFillButton(label: String, selected: String, viewModel: PhotoEditorViewModel) {
    val isSelected = selected == label
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) PurpleNeon else GlassWhite)
            .clickable { viewModel.updateBgFill(label) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OptionBadgeButton(
    label: String,
    selected: String,
    onClick: (String) -> Unit
) {
    val isSelected = selected == label
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) PinkAccent else GlassWhite)
            .clickable { onClick(label) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

// 4. Results screen
@Composable
fun ResultScreen(
    feature: EditingFeature,
    state: com.example.ui.PhotoEditorUiState,
    viewModel: PhotoEditorViewModel
) {
    var showSuccessToast by remember { mutableStateOf(false) }

    LaunchedEffect(state.rewardUnlocked) {
        if (state.rewardUnlocked && state.pendingHdExport) {
            showSuccessToast = true
            viewModel.resetReward()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.resetEditor() }) {
                Icon(Icons.Default.ArrowBack, "Home", tint = TextWhite)
            }
            Text("ARTWORK RESULT", color = TextWhite, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            IconButton(onClick = { viewModel.startReedit() }) {
                Icon(Icons.Default.Edit, "Re-edit", tint = PinkAccent)
            }
        }

        // Live preview with watermark overlay
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF03010A))
                .border(1.dp, GlassWhite, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Main photo layout representation
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1E1B4B), DarkBg)
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = PinkAccent.copy(alpha = 0.2f),
                        modifier = Modifier.size(140.dp).align(Alignment.Center)
                    )
                }

                // Transparent dynamic watermark (Disappears if premium)
                if (!state.session.isPremium) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(1.dp, GlassWhite, RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "WATERMARK AI PHOTO EDITOR",
                            color = TextWhite.copy(alpha = 0.4f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Completion Badge
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF10B981))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, null, tint = TextWhite, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "AI MATRIX PASSED",
                            color = TextWhite,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toast feedback overlay
        if (showSuccessToast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF10B981))
                    .padding(12.dp)
            ) {
                Text(
                    "UHD High Definition Export Unlocked! Saved to target system storage successfully.",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }

        // Functional action items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // High-definition export (Bypasses with Premium, loads interactive Ad for free)
            Button(
                onClick = { viewModel.startHdExport() },
                colors = ButtonDefaults.buttonColors(containerColor = if (state.session.isPremium) PinkAccent else PurpleNeon),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (state.session.isPremium) Icons.Default.WorkspacePremium else Icons.Default.Tv,
                        contentDescription = "AdMob Export",
                        tint = TextWhite
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (state.session.isPremium) "EXPORT ULTRA-HD (PREMIUM BENEFIT)" else "WATCH AD TO EXPORT HIGH-DEFINITION",
                        color = TextWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Secondary standard download
                Button(
                    onClick = { showSuccessToast = true },
                    colors = ButtonDefaults.buttonColors(containerColor = CardColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, GlassWhite, RoundedCornerShape(16.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, null, tint = TextGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAVE SD", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Share button
                Button(
                    onClick = { /* Share simulated */ },
                    colors = ButtonDefaults.buttonColors(containerColor = CardColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, GlassWhite, RoundedCornerShape(16.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, null, tint = TextGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SHARE ART", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 5. Google AdMob simulated overlay video ad with timer (Premium UX requirement)
@Composable
fun RewardedAdPlayer(
    state: com.example.ui.PhotoEditorUiState,
    viewModel: PhotoEditorViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp)
        ) {
            // Header with Skip validation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEB800))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("AdMob Sponsor", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = { viewModel.closeAdEarly() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.Close, "Abuse Abort Ad", tint = TextWhite, modifier = Modifier.size(16.dp))
                }
            }

            // Visual active video body container
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(PinkAccent, PurpleNeon)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = "Video playing stream",
                        tint = TextWhite,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    "Video Sponsor: Premium Shader Tech",
                    color = TextWhite,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Rewarding high resolution HD export in progress...",
                    color = TextGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Countdown panel
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(PurpleNeon),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        state.adTimeRemaining.toString(),
                        color = TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Reward unlocks in ${state.adTimeRemaining}s",
                    color = TextGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

val PunkGradientRightTheme = Color(0xFFF43F5E)
