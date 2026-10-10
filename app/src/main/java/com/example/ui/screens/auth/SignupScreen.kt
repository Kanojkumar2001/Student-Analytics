package com.example.ui.screens.auth

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
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

@Composable
fun SignupScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onSignupSubmit: (name: String, email: String, pass: String, role: UserRole, dept: String, roll: String?) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var selectedDept by remember { mutableStateOf("CSE") }
    var passwordVisible by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "EduPulse", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(text = " AI", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = CyanGlow)
        }

        Text(
            text = "Create Academic Institutional Account",
            fontSize = 12.sp,
            color = Slate400,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("signup_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Sign Up",
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

                // Role selector
                Text(text = "Select Academic Role", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(UserRole.STUDENT, UserRole.FACULTY, UserRole.HOD, UserRole.ADMIN).forEach { role ->
                        val isSelected = role == selectedRole
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (isSelected) IndigoAccent else Slate850, RoundedCornerShape(8.dp))
                                .clickable { selectedRole = role }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${role.iconEmoji} ${role.label.split(" ").first()}",
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Slate400
                            )
                        }
                    }
                }

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; validationError = null },
                    modifier = Modifier.fillMaxWidth().testTag("signup_name_input"),
                    label = { Text("Full Name", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanGlow) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanGlow,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate850,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; validationError = null },
                    modifier = Modifier.fillMaxWidth().testTag("signup_email_input"),
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
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                // Department & Roll / Staff ID
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Branch / Dept", fontSize = 10.sp, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("CSE", "ECE", "EEE", "MECH").forEach { dept ->
                                val isSelected = dept == selectedDept
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(if (isSelected) CyanGlow else Slate850, RoundedCornerShape(6.dp))
                                        .clickable { selectedDept = dept }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dept,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFF0F172A) else Slate400
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = rollNumber,
                    onValueChange = { rollNumber = it },
                    modifier = Modifier.fillMaxWidth().testTag("signup_roll_input"),
                    label = { Text(if (selectedRole == UserRole.STUDENT) "Student Roll No. (e.g. 21A91A0518)" else "Staff Employee ID", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = CyanGlow) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanGlow,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate850,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; validationError = null },
                    modifier = Modifier.fillMaxWidth().testTag("signup_password_input"),
                    label = { Text("Password (Min 6 chars)", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyanGlow) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
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
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; validationError = null },
                    modifier = Modifier.fillMaxWidth().testTag("signup_confirm_password_input"),
                    label = { Text("Confirm Password", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyanGlow) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanGlow,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate850,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            validationError = "Please enter your full name."
                        } else if (email.isBlank() || !email.contains("@")) {
                            validationError = "Please enter a valid academic email."
                        } else if (password.length < 6) {
                            validationError = "Password must be at least 6 characters long."
                        } else if (password != confirmPassword) {
                            validationError = "Passwords do not match."
                        } else {
                            onSignupSubmit(name, email, password, selectedRole, selectedDept, rollNumber.ifBlank { null })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_signup"),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(text = "Create Account", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // OR Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = SurfaceCardBorder)
                    Text(
                        text = "  OR sign up with  ",
                        fontSize = 11.sp,
                        color = Slate400,
                        fontWeight = FontWeight.Medium
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = SurfaceCardBorder)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Social Sign-In Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Google Sign-In Button
                    OutlinedButton(
                        onClick = { /* Google sign-in integration */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_google_signup"),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Slate850)
                    ) {
                        GoogleIcon(modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Google",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    // Facebook Sign-In Button
                    OutlinedButton(
                        onClick = { /* Facebook sign-in integration */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_facebook_signup"),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1877F2).copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF1877F2).copy(alpha = 0.12f))
                    ) {
                        FacebookIcon(modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Facebook",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1877F2)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(text = "Already registered? ", fontSize = 13.sp, color = Slate400)
            Text(
                text = "Sign In",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanGlow,
                modifier = Modifier
                    .clickable { onNavigateToLogin() }
                    .testTag("btn_switch_to_login")
            )
        }
    }
}

/**
 * Google multicolor "G" icon drawn with Canvas.
 */
@Composable
fun GoogleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = minOf(w, h) / 2f
        val strokeW = r * 0.18f

        // Red arc (top-right)
        val redPath = Path().apply {
            arcTo(
                rect = Rect(Offset(cx - r, cy - r), Size(r * 2, r * 2)),
                startAngleDegrees = -30f,
                sweepAngleDegrees = 90f,
                forceMoveTo = true
            )
        }
        drawPath(redPath, color = Color(0xFFEA4335), style = Stroke(width = strokeW))

        // Yellow arc (bottom-right)
        val yellowPath = Path().apply {
            arcTo(
                rect = Rect(Offset(cx - r, cy - r), Size(r * 2, r * 2)),
                startAngleDegrees = 60f,
                sweepAngleDegrees = 90f,
                forceMoveTo = true
            )
        }
        drawPath(yellowPath, color = Color(0xFFFBBC05), style = Stroke(width = strokeW))

        // Green arc (bottom-left)
        val greenPath = Path().apply {
            arcTo(
                rect = Rect(Offset(cx - r, cy - r), Size(r * 2, r * 2)),
                startAngleDegrees = 150f,
                sweepAngleDegrees = 90f,
                forceMoveTo = true
            )
        }
        drawPath(greenPath, color = Color(0xFF34A853), style = Stroke(width = strokeW))

        // Blue arc (top-left)
        val bluePath = Path().apply {
            arcTo(
                rect = Rect(Offset(cx - r, cy - r), Size(r * 2, r * 2)),
                startAngleDegrees = 240f,
                sweepAngleDegrees = 90f,
                forceMoveTo = true
            )
        }
        drawPath(bluePath, color = Color(0xFF4285F4), style = Stroke(width = strokeW))

        // Blue horizontal bar of the "G"
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(cx, cy),
            end = Offset(cx + r * 0.82f, cy),
            strokeWidth = strokeW
        )
    }
}

/**
 * Facebook "f" icon drawn with Canvas.
 */
@Composable
fun FacebookIcon(modifier: Modifier = Modifier) {
    val facebookBlue = Color(0xFF1877F2)
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = minOf(w, h) / 2f

        // Circle background
        drawCircle(color = facebookBlue, radius = r, center = Offset(cx, cy))

        // "f" letter
        val strokeW = r * 0.22f
        val fTop = cy - r * 0.55f
        val fBottom = cy + r * 0.65f
        val fLeft = cx - r * 0.08f
        val fRight = cx + r * 0.42f
        val fCross = cy - r * 0.1f

        // Vertical stem
        drawLine(
            color = Color.White,
            start = Offset(cx + r * 0.08f, fTop + r * 0.2f),
            end = Offset(cx + r * 0.08f, fBottom),
            strokeWidth = strokeW
        )
        // Top curve of f
        val curvePath = Path().apply {
            moveTo(cx - r * 0.08f, fTop + r * 0.35f)
            quadraticTo(cx + r * 0.08f, fTop, fRight - r * 0.1f, fTop + r * 0.2f)
        }
        drawPath(curvePath, color = Color.White, style = Stroke(width = strokeW))
        // Crossbar
        drawLine(
            color = Color.White,
            start = Offset(fLeft, fCross),
            end = Offset(fRight - r * 0.05f, fCross),
            strokeWidth = strokeW
        )
    }
}
