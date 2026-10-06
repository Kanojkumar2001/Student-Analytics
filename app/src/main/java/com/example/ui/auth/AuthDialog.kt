package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.UserRole
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun AuthDialog(
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("rahul.kumar@college.edu") }
    var password by remember { mutableStateOf("password123") }
    var name by remember { mutableStateOf("Rahul Kumar") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by remember { authViewModel.isLoading }
    val errorMessage by remember { authViewModel.errorMessage }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_auth"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(IndigoAccent.copy(alpha = 0.2f), CircleShape)
                        .border(1.dp, CyanGlow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔐", fontSize = 24.sp)
                }

                Text(
                    text = if (isSignUp) "Create Academic Account" else "EduPulse Secure Sign-In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Firebase Authentication & Multi-Role RBAC",
                    fontSize = 11.sp,
                    color = CyanGlow
                )

                // Role Selector Radio Pills
                Text(
                    text = "Select Persona / Role:",
                    fontSize = 11.sp,
                    color = Slate400,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    UserRole.values().forEach { role ->
                        val isSelected = role == selectedRole
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSelected) CyanGlow else Slate850,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedRole = role }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = role.iconEmoji, fontSize = 14.sp)
                                Text(
                                    text = role.label.split(" ").first(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Slate900 else Slate400
                                )
                            }
                        }
                    }
                }

                if (isSignUp) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Full Name", color = Slate400) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanGlow) },
                        colors = outlinedColors(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_input"),
                    label = { Text("Institutional Email", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CyanGlow) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = outlinedColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input"),
                    label = { Text("Password", color = Slate400) },
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = outlinedColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = RoseRisk,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Submit Button
                Button(
                    onClick = {
                        if (isSignUp) {
                            authViewModel.signUp(email, password, name, selectedRole) {
                                onSuccess()
                            }
                        } else {
                            authViewModel.login(email, password, selectedRole) {
                                onSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("btn_auth_submit"),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text(
                            text = if (isSignUp) "Register Account" else "Sign In as ${selectedRole.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Switch Login/Signup Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUp) "Already registered?" else "Need an account?",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                    Text(
                        text = if (isSignUp) "Sign In" else "Create Account",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanGlow,
                        modifier = Modifier.clickable {
                            isSignUp = !isSignUp
                            authViewModel.clearError()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun outlinedColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CyanGlow,
    unfocusedBorderColor = SurfaceCardBorder,
    focusedContainerColor = Slate850,
    unfocusedContainerColor = Slate850,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)
