package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HomeTab
import com.example.ui.PhotoEditorViewModel
import com.example.ui.Screen
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: PhotoEditorViewModel, onGetStarted: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        // If user already logged in, enter home directly
        // ViewModel state is monitored in MainActivity.
    }

    val rotationState = rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        // Glowing background nodes
        Box(
            modifier = Modifier
                .size(320.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(GlowPurple, Color.Transparent),
                            center = Offset(size.width / 2, size.height / 2),
                            radius = size.width / 1.4f
                        )
                    )
                }
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                    slideInVertically(initialOffsetY = { 60 })
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // Glow Neon camera logo box
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(PurpleNeon, PinkAccent, PurpleNeon)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .background(CardColor.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Camera Glow",
                        tint = PinkAccent,
                        modifier = Modifier
                            .size(54.dp)
                            .drawBehind {
                                // Subtle camera shutter rotate glow simulation
                            }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "LUMINA ",
                        color = TextWhite,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "AI",
                        color = PurpleNeon,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enhance. Edit. Create Magic.",
                    color = TextGray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))

                // Pulsing glowing standard button
                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(54.dp)
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(PurpleNeon, PinkAccent)),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(PurpleNeon, PinkAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "GET STARTED",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, "Start Icon", tint = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: PhotoEditorViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Glowing overlay
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(300.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(GlowPurple, Color.Transparent),
                            radius = size.width
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(
                    "Welcome Back",
                    color = TextWhite,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Enter your details to create digital wizardry",
                    color = TextGray,
                    fontSize = 14.sp
                )
            }

            // Input Fields Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, "Email Picker", tint = PurpleNeon) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = GlassWhite,
                        focusedLabelColor = PurpleNeon,
                        unfocusedLabelColor = TextGray,
                        focusedContainerColor = CardColor.copy(alpha = 0.6f),
                        unfocusedContainerColor = CardColor.copy(alpha = 0.6f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Lock Picker", tint = PurpleNeon) },
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, "Show Hide Password", tint = TextGray)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = GlassWhite,
                        focusedLabelColor = PurpleNeon,
                        unfocusedLabelColor = TextGray,
                        focusedContainerColor = CardColor.copy(alpha = 0.6f),
                        unfocusedContainerColor = CardColor.copy(alpha = 0.6f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Forgot password text
                Text(
                    "Forgot Password?",
                    color = PinkAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Simulate */ }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Sign In Button
                Button(
                    onClick = {
                        val targetEmail = if (email.trim().isEmpty()) "user@creative.ai" else email
                        viewModel.handleLogin(targetEmail, targetEmail.substringBefore("@"), false)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(PurpleNeon, PinkAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("SIGN IN", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Skip / Continue as Guest (CRITICAL FEATURE requested by user)
                OutlinedButton(
                    onClick = { viewModel.skipLogin() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                    border = borderTextField(GlassWhite),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        "CONTINUE AS GUEST",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Social Logins + Sign up toggle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text("Or connect using", color = TextGray, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialButton(icon = Icons.Default.AddCircle, text = "Google") {
                        viewModel.handleLogin("google.user@gmail.com", "Google Creator", false)
                    }
                    SocialButton(icon = Icons.Default.AccountCircle, text = "Facebook") {
                        viewModel.handleLogin("fb.artist@facebook.com", "FB Maestro", false)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account? ", color = TextGray, fontSize = 13.sp)
                    Text(
                        "Sign Up",
                        color = PurpleNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            viewModel.navigateTo(Screen.SignUp)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: PhotoEditorViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(280.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(GlowPurple, Color.Transparent),
                            radius = size.width
                        )
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 28.dp)
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Login) },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.ArrowBack, "Back to Login", tint = TextWhite)
                }
                
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Create Account",
                    color = TextWhite,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Unlock unlimited artistic capabilities",
                    color = TextGray,
                    fontSize = 14.sp
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, "Name", tint = PurpleNeon) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = GlassWhite,
                        focusedLabelColor = PurpleNeon,
                        unfocusedLabelColor = TextGray,
                        focusedContainerColor = CardColor.copy(alpha = 0.6f),
                        unfocusedContainerColor = CardColor.copy(alpha = 0.6f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, "Email Address", tint = PurpleNeon) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = GlassWhite,
                        focusedLabelColor = PurpleNeon,
                        unfocusedLabelColor = TextGray,
                        focusedContainerColor = CardColor.copy(alpha = 0.6f),
                        unfocusedContainerColor = CardColor.copy(alpha = 0.6f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Passwd", tint = PurpleNeon) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = GlassWhite,
                        focusedLabelColor = PurpleNeon,
                        unfocusedLabelColor = TextGray,
                        focusedContainerColor = CardColor.copy(alpha = 0.6f),
                        unfocusedContainerColor = CardColor.copy(alpha = 0.6f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Confirm Passwd", tint = PurpleNeon) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PurpleNeon,
                        unfocusedBorderColor = GlassWhite,
                        focusedLabelColor = PurpleNeon,
                        unfocusedLabelColor = TextGray,
                        focusedContainerColor = CardColor.copy(alpha = 0.6f),
                        unfocusedContainerColor = CardColor.copy(alpha = 0.6f)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Create Account Action
                Button(
                    onClick = {
                        val finalEmail = if (email.trim().isEmpty()) "artist@generative.ai" else email
                        val finalName = if (name.trim().isEmpty()) "Ai Creator" else name
                        viewModel.handleSignUp(finalEmail, finalName)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(PurpleNeon, PinkAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("CREATE ACCOUNT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Text("Already have an account? ", color = TextGray, fontSize = 13.sp)
                Text(
                    "Sign In",
                    color = PurpleNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.navigateTo(Screen.Login)
                    }
                )
            }
        }
    }
}

@Composable
fun SocialButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .border(1.dp, GlassWhite, RoundedCornerShape(14.dp))
            .background(CardColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Icon(icon, text, tint = TextWhite, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

private fun borderTextField(color: Color) = androidx.compose.foundation.BorderStroke(1.dp, color)
