package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.UserRole
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun RoleSelectionModal(
    currentRole: UserRole,
    onSelectRole: (UserRole) -> Unit,
    onDismiss: () -> Unit,
    onOpenAuth: () -> Unit,
    isLoggedIn: Boolean,
    userEmail: String?
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("dialog_role_selection"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Slate950),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Switch Academic Persona",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Role-based dashboards & data permissions",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(IndigoAccent.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "RBAC Live",
                            color = CyanGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // The 5 Exact Roles matching user's uploaded image!
                UserRole.values().forEach { role ->
                    val isSelected = role == currentRole
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) Slate900 else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) CyanGlow.copy(alpha = 0.6f) else SurfaceCardBorder.copy(alpha = 0.4f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onSelectRole(role)
                                onDismiss()
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                            .testTag("role_item_${role.name.lowercase()}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = role.iconEmoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.size(14.dp))
                            Text(
                                text = role.label,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) CyanGlow else Color.White // Glowing cyan highlight like in screenshot!
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(CyanGlow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active",
                                    tint = Slate950,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Session / Auth Trigger Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate850, RoundedCornerShape(10.dp))
                        .clickable {
                            onDismiss()
                            onOpenAuth()
                        }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isLoggedIn) "Logged in as ${userEmail ?: "User"}" else "Firebase Authentication",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLoggedIn) EmeraldSuccess else Color.White
                        )
                        Text(
                            text = if (isLoggedIn) "Tap to switch user / credentials" else "Sign in or register new account",
                            fontSize = 10.sp,
                            color = Slate400
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(IndigoAccent, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isLoggedIn) "Manage" else "Sign In",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
