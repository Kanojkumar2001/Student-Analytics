package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate950
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

data class DemoAccount(val label: String, val email: String, val role: UserRole, val emoji: String)

@Composable
fun LoginScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onLoginSubmit: (String, String) -> Unit,
    onNavigateToSignup: () -> Unit,
    onForgotPasswordClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("rahul.kumar@college.edu") }
    var password by remember { mutableStateOf("pass123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val demoAccounts = listOf(
        DemoAccount("Student (Rahul)", "rahul.kumar@college.edu", UserRole.STUDENT, "👨‍🎓"),
        DemoAccount("Top Student (Priya)", "priya.sharma@college.edu", UserRole.STUDENT, "🌟"),
        DemoAccount("Faculty (Prof. Kumar)", "prof.kumar@college.edu", UserRole.FACULTY, "👨‍🏫"),
        DemoAccount("HOD (Dr. Raman)", "hod.raman@college.edu", UserRole.HOD, "🏢"),
        DemoAccount("Admin", "admin@college.edu", UserRole.ADMIN, "👑")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Brand Logo & Heading
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(IndigoAccent, RoundedCornerShape(16.dp))
                .border(2.dp, CyanGlow, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🎓", fontSize = 30.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "EduPulse", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(text = " AI", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = CyanGlow)
        }

        Text(
            text = "AI-Powered Student Analytics & Success Platform",
            fontSize = 12.sp,
            color = Slate400,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Main Login Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Sign In to Your Portal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (errorMessage != null || validationError != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RoseRisk.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .border(1.dp, RoseRisk.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = validationError ?: errorMessage ?: "",
                            fontSize = 12.sp,
                            color = RoseRisk,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Email input
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        validationError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_email_input"),
                    label = { Text("Institutional Email", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CyanGlow) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanGlow,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate850,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                // Password input with visibility toggle
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        validationError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_input"),
                    label = { Text("Password", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyanGlow) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility",
                                tint = Slate400
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanGlow,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate850,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Remember Me & Forgot Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = IndigoAccent, uncheckedColor = Slate400),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(text = "Remember me", fontSize = 12.sp, color = Slate400)
                    }

                    Text(
                        text = "Forgot password?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanGlow,
                        modifier = Modifier
                            .clickable { onForgotPasswordClick(email) }
                            .testTag("btn_forgot_password")
                    )
                }

                // Submit Button
                Button(
                    onClick = {
                        if (email.isBlank() || !email.contains("@")) {
                            validationError = "Please enter a valid academic email."
                        } else if (password.length < 4) {
                            validationError = "Password must be at least 4 characters."
                        } else {
                            onLoginSubmit(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_sign_in"),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(text = "Sign In with Firebase", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Demo Quick Login Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚡ Instant Demo Role Login (Tap to fill & test):",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(demoAccounts) { demo ->
                    Box(
                        modifier = Modifier
                            .background(Slate850, RoundedCornerShape(20.dp))
                            .border(1.dp, CyanGlow.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .clickable {
                                email = demo.email
                                password = "pass123"
                                onLoginSubmit(demo.email, "pass123")
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = demo.emoji, fontSize = 13.sp)
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = demo.label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Switch to Signup
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(text = "Don't have an academic account? ", fontSize = 13.sp, color = Slate400)
            Text(
                text = "Sign Up",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanGlow,
                modifier = Modifier
                    .clickable { onNavigateToSignup() }
                    .testTag("btn_switch_to_signup")
            )
        }
    }
}
