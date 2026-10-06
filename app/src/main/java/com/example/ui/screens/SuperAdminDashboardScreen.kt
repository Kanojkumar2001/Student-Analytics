package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.StudentEntity
import com.example.model.UserRole
import com.example.ui.components.MetricKpiCard
import com.example.ui.theme.AmberWarning
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
fun SuperAdminDashboardScreen(
    students: List<StudentEntity>,
    onOpenImportDialog: () -> Unit,
    onRoleChange: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var attendanceCutoff by remember { mutableFloatStateOf(75f) }
    var riskSensitivity by remember { mutableFloatStateOf(1.2f) }
    var modelStatus by remember { mutableStateOf("Online • Model gemini-3.5-flash Calibrated") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Super Admin Console 👑",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Institutional Governance, RBAC, AI Engine & Database Ops",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyanGlow
                    )
                }

                Box(
                    modifier = Modifier
                        .background(EmeraldSuccess.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, EmeraldSuccess.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(EmeraldSuccess, CircleShape))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(text = "System Healthy", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // System Telemetry KPIs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Total Users",
                    value = "698",
                    iconEmoji = "👥",
                    accentColor = IndigoAccent,
                    trendText = "+14 this week",
                    isPositiveTrend = true,
                    subtitle = "Students, Faculty, Staff",
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Active Sessions",
                    value = "142",
                    iconEmoji = "⚡",
                    accentColor = CyanGlow,
                    trendText = "Real-time",
                    isPositiveTrend = true,
                    subtitle = "JWT / Firebase Auth",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Firestore Records",
                    value = "${students.size * 5 + 40}",
                    iconEmoji = "🔥",
                    accentColor = AmberWarning,
                    trendText = "Synchronized",
                    isPositiveTrend = true,
                    subtitle = "CRUD & Snapshot Listeners",
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "AI Inference Requests",
                    value = "1,420",
                    iconEmoji = "🤖",
                    accentColor = EmeraldSuccess,
                    trendText = "99.9% Latency < 1s",
                    isPositiveTrend = true,
                    subtitle = "Risk Engine & Assistant",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Data Ingestion & CSV Import Action Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Batch Data Ingestion Pipeline",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Import academic records (CSV / Excel) into Firestore & Room, triggers automatic K-Means clustering.",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    Button(
                        onClick = onOpenImportDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("admin_import_csv_button")
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = Slate900, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(text = "Import", color = Slate900, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // User & Role Access Management (RBAC)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Role-Based Access Control (RBAC) 🛡️",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(text = "5 Roles Configured", fontSize = 11.sp, color = CyanGlow)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val sampleUsers = listOf(
                        Triple("Rahul Kumar (21A91A0501)", UserRole.STUDENT, "rahul.kumar@college.edu"),
                        Triple("Dr. S. K. Sharma (CS014)", UserRole.FACULTY, "sk.sharma@college.edu"),
                        Triple("Prof. Ananya Kumar (MT009)", UserRole.MENTOR, "ananya.kumar@college.edu"),
                        Triple("Dr. R. Raman (HOD-CSE)", UserRole.HOD, "hod.cse@college.edu"),
                        Triple("System Administrator", UserRole.ADMIN, "admin@college.edu")
                    )

                    sampleUsers.forEach { (uname, urole, uemail) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Slate850, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = uname, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                Text(text = uemail, fontSize = 11.sp, color = Slate400)
                            }

                            Box(
                                modifier = Modifier
                                    .background(IndigoAccent.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .border(1.dp, CyanGlow.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .clickable { onRoleChange(urole) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${urole.iconEmoji} ${urole.label}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanGlow
                                )
                            }
                        }
                    }
                }
            }
        }

        // AI Model Hyperparameter & Threshold Configuration
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "AI Analytics Model Configuration ⚙️",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Adjust institutional risk boundaries & early-warning thresholds",
                        fontSize = 11.sp,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Attendance Alert Threshold: ${attendanceCutoff.toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Slider(
                        value = attendanceCutoff,
                        onValueChange = { attendanceCutoff = it },
                        valueRange = 65f..85f,
                        colors = SliderDefaults.colors(thumbColor = CyanGlow, activeTrackColor = CyanGlow)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "ML Risk Multiplier Sensitivity: ${String.format("%.1f", riskSensitivity)}x",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Slider(
                        value = riskSensitivity,
                        onValueChange = { riskSensitivity = it },
                        valueRange = 0.8f..2.0f,
                        colors = SliderDefaults.colors(thumbColor = IndigoAccent, activeTrackColor = IndigoAccent)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate850, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = modelStatus,
                            fontSize = 11.sp,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
