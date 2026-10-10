package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.auth.AuthUser
import com.example.model.UserRole
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun StaffProfileScreen(
    currentUser: AuthUser,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val roleColor = when (currentUser.role) {
        UserRole.HOD -> IndigoAccent
        UserRole.PRINCIPAL -> AmberWarning
        UserRole.ADMIN -> RoseRisk
        UserRole.MENTOR -> PurpleAccent
        else -> CyanGlow
    }

    val roleDescription = when (currentUser.role) {
        UserRole.HOD -> "Department Head — manages faculty, students, and department-level analytics."
        UserRole.PRINCIPAL -> "Principal — institution-wide oversight, department comparisons, and executive insights."
        UserRole.ADMIN -> "System Administrator — full system access, RBAC management, and AI configuration."
        UserRole.FACULTY -> "Faculty Member — manages class grades, attendance, and student interventions."
        UserRole.STAFF -> "Staff Member — assigned class oversight, student progress tracking, and reporting."
        UserRole.MENTOR -> "Academic Mentor — guides students through personalized development plans."
        else -> "Academic Professional"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(roleColor.copy(alpha = 0.15f), CircleShape)
                            .border(2.dp, roleColor.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = roleColor
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = currentUser.displayName, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .background(roleColor.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
                            .border(1.dp, roleColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "${currentUser.role.iconEmoji}  ${currentUser.role.label}",
                            fontSize = 13.sp, fontWeight = FontWeight.Bold, color = roleColor
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = roleDescription, fontSize = 12.sp, color = Slate400,
                        lineHeight = 18.sp, modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Account Information", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    ProfileInfoRow(Icons.Default.Person, "Full Name", currentUser.displayName, CyanGlow)
                    ProfileInfoRow(Icons.Default.Email, "Email", currentUser.email, IndigoAccent)
                    ProfileInfoRow(Icons.Default.Business, "Department", currentUser.department, EmeraldSuccess)
                    ProfileInfoRow(Icons.Default.School, "Role", currentUser.role.label, roleColor)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Access Permissions", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Your account has role-based access to the following features:", fontSize = 12.sp, color = Slate400)

                    val perms = when (currentUser.role) {
                        UserRole.HOD -> listOf(
                            "✅ Department Analytics Dashboard",
                            "✅ Faculty Performance Overview",
                            "✅ Student At-Risk Management",
                            "✅ Intervention Tracking",
                            "✅ Department Reports",
                            "❌ System Administration"
                        )
                        UserRole.PRINCIPAL -> listOf(
                            "✅ Institution Analytics Dashboard",
                            "✅ All Department Comparisons",
                            "✅ AI Executive Insights",
                            "✅ At-Risk Overview",
                            "✅ Institution Reports",
                            "❌ Direct Student Records Edit"
                        )
                        UserRole.ADMIN -> listOf(
                            "✅ Full System Administration",
                            "✅ User & Role Management",
                            "✅ AI Engine Configuration",
                            "✅ Database Operations",
                            "✅ All Analytics",
                            "✅ All Reports"
                        )
                        else -> listOf(
                            "✅ Assigned Class Management",
                            "✅ Attendance Recording",
                            "✅ Marks & Assessment Entry",
                            "✅ Intervention Creation",
                            "✅ Student AI Analysis",
                            "❌ Department-Level Analytics"
                        )
                    }
                    perms.forEach { perm ->
                        val isAllowed = perm.startsWith("✅")
                        Text(
                            text = perm, fontSize = 12.sp,
                            color = if (isAllowed) Color.White else Slate400,
                            fontWeight = if (isAllowed) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_staff_logout"),
                colors = ButtonDefaults.buttonColors(containerColor = RoseRisk.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Sign Out", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, label: String, value: String, iconTint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate850, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(34.dp).background(iconTint.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.size(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = Slate400)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}
