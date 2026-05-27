package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.EditingFeature
import com.example.ui.HomeTab
import com.example.ui.PhotoEditorViewModel
import com.example.ui.theme.*

@Composable
fun MainAppContainer(viewModel: PhotoEditorViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Safe navigation view switcher
        Box(modifier = Modifier.fillMaxSize()) {
            when (state.activeTab) {
                HomeTab.HOME -> HomeScreen(viewModel, onFeatureClick = { feat ->
                    viewModel.selectFeature(feat)
                })
                HomeTab.EDIT -> {
                    if (state.activeFeature == null) {
                        // Fallback/No active project selected edit flow view
                        NoProjectSelectedView {
                            viewModel.setTab(HomeTab.HOME)
                        }
                    } else {
                        EditorWorkflowScreen(viewModel = viewModel)
                    }
                }
                HomeTab.HISTORY -> HistoryScreen(viewModel = viewModel)
                HomeTab.PROFILE -> ProfileScreen(viewModel = viewModel)
            }
        }

        // FIXED Glasses/Neon Bottom Navigation Row (CRITICAL Rule: must sit on top of content and never scroll)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars) // Proper Android system gesture safe spacing
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, GlassWhite, RoundedCornerShape(24.dp))
                    .background(Color(0xFF0B1120).copy(alpha = 0.92f))
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    label = "Home",
                    iconActive = Icons.Filled.Home,
                    iconInactive = Icons.Outlined.Home,
                    selected = state.activeTab == HomeTab.HOME,
                    onClick = { viewModel.setTab(HomeTab.HOME) }
                )

                BottomNavItem(
                    label = "Edit",
                    iconActive = Icons.Filled.Brush,
                    iconInactive = Icons.Outlined.Brush,
                    selected = state.activeTab == HomeTab.EDIT,
                    onClick = { viewModel.setTab(HomeTab.EDIT) }
                )

                BottomNavItem(
                    label = "History",
                    iconActive = Icons.Filled.History,
                    iconInactive = Icons.Outlined.History,
                    selected = state.activeTab == HomeTab.HISTORY,
                    onClick = { viewModel.setTab(HomeTab.HISTORY) }
                )

                BottomNavItem(
                    label = "Profile",
                    iconActive = Icons.Filled.Person,
                    iconInactive = Icons.Outlined.Person,
                    selected = state.activeTab == HomeTab.PROFILE,
                    onClick = { viewModel.setTab(HomeTab.PROFILE) }
                )
            }
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    label: String,
    iconActive: ImageVector,
    iconInactive: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = PurpleNeon
    val inactiveColor = TextGray

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(if (selected) PurpleNeon.copy(alpha = 0.15f) else Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (selected) PurpleNeon.copy(alpha = 0.3f) else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 14.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (selected) iconActive else iconInactive,
                contentDescription = label,
                tint = if (selected) activeColor else inactiveColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (selected) TextWhite else inactiveColor,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun NoProjectSelectedView(onGoHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
            .background(DarkBg)
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Brush,
            null,
            tint = PurpleNeon.copy(alpha = 0.4f),
            modifier = Modifier.size(90.dp)
        )
        
        Spacer(modifier = Modifier.height(18.dp))

        Text(
            "No Active Project selected",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            "Please select a model from the Home tab, upload a sample photo, and generate premium AI transformations.",
            color = TextGray,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onGoHome,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(PurpleNeon, PinkAccent))),
                contentAlignment = Alignment.Center
            ) {
                Text("EXPLORE MODELS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
