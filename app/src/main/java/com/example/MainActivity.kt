package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.PhotoEditorViewModel
import com.example.ui.Screen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainAppContainer
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: PhotoEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val state by viewModel.uiState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (state.currentScreen == Screen.MainContainer) 0.dp else innerPadding.calculateBottomPadding())
                    ) {
                        AnimatedContent(
                            targetState = state.currentScreen,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "ScreenSwitchAnimated"
                        ) { screen ->
                            when (screen) {
                                Screen.Splash -> {
                                    SplashScreen(viewModel = viewModel) {
                                        if (state.session.isLoggedIn) {
                                            viewModel.navigateTo(Screen.MainContainer)
                                        } else {
                                            viewModel.navigateTo(Screen.Login)
                                        }
                                    }
                                }
                                Screen.Login -> {
                                    LoginScreen(viewModel = viewModel)
                                }
                                Screen.SignUp -> {
                                    SignUpScreen(viewModel = viewModel)
                                }
                                Screen.MainContainer -> {
                                    MainAppContainer(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
