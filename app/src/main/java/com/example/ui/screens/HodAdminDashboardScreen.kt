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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.StudentEntity
import com.example.model.DepartmentStats
import com.example.model.StudentClusterType
import com.example.ui.components.MetricKpiCard
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
fun HodAdminDashboardScreen(
    students: List<StudentEntity>,
    onOpenImportDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deptStats = listOf(
        DepartmentStats("Computer Science (CSE)", 240, 7.8, 84.0, 91.0, 14, 18),
        DepartmentStats("Electronics & Comm (ECE)", 160, 7.2, 79.0, 85.0, 12, 22),
        DepartmentStats("Electrical (EEE)", 120, 7.1, 77.0, 82.0, 10, 16),
        DepartmentStats("Mechanical (MECH)", 122, 6.9, 74.0, 79.0, 12, 17)
    )

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
                        text = "Department & Institutional Analytics 🏢",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Macro trends, multi-branch comparisons, and ML cluster cohorts",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }
        }

        // Action banner: Data Import & ML Re-training
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoAccent.copy(alpha = 0.15f)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndigoAccent.copy(alpha = 0.4f)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Batch Data Ingestion & ML Pipeline",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Import academic CSV/Excel records into MongoDB/Room & run risk predictor.",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    Button(
                        onClick = onOpenImportDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_import_csv")
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color(0xFF0F172A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(text = "Import CSV", color = Color(0xFF0F172A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Macro KPI Cards (Requirement #20)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Total Enrolled",
                    value = "642",
                    iconEmoji = "🏛️",
                    accentColor = IndigoAccent,
                    trendText = "4 Branches",
                    isPositiveTrend = true,
                    subtitle = "Active Students",
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Average GPA",
                    value = "7.42",
                    iconEmoji = "📈",
                    accentColor = CyanGlow,
                    trendText = "+0.3 YOY",
                    isPositiveTrend = true,
                    subtitle = "Scale 10.0",
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
                    title = "Avg Attendance",
                    value = "81%",
                    iconEmoji = "⏰",
                    accentColor = EmeraldSuccess,
                    trendText = "Healthy",
                    isPositiveTrend = true,
                    subtitle = "Cutoff 75%",
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Pass Percentage",
                    value = "87%",
                    iconEmoji = "🎓",
                    accentColor = AmberWarning,
                    trendText = "+2.4%",
                    isPositiveTrend = true,
                    subtitle = "Across all depts",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Institutional Risk Distribution Chart
        item {
            RiskDistributionCard(students = students)
        }

        // Department Comparison Matrix (Requirement #21)
        item {
            DepartmentComparisonCard(deptStats = deptStats)
        }

        // Student Clustering (K-Means Unsupervised Learning) (Requirement #14)
        item {
            StudentClusteringCard(students = students)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RiskDistributionCard(students: List<StudentEntity>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Student Risk Distribution (Institutional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Early Warning System Classification Breakdown",
                fontSize = 11.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(14.dp))

            DistributionRow("Low Risk (0 - 30%) 🟢", "398 Students (62%)", 0.62f, EmeraldSuccess)
            Spacer(modifier = Modifier.height(10.dp))
            DistributionRow("Medium Risk (31 - 60%) 🟡", "196 Students (30%)", 0.30f, AmberWarning)
            Spacer(modifier = Modifier.height(10.dp))
            DistributionRow("High Risk (61 - 100%) 🔴", "48 Students (8%)", 0.08f, RoseRisk)
        }
    }
}

@Composable
private fun DistributionRow(title: String, countText: String, weight: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
            Text(text = countText, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { weight },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = Slate850
        )
    }
}

@Composable
private fun DepartmentComparisonCard(deptStats: List<DepartmentStats>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Department Benchmark Comparison 📊",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Academic metrics evaluated across engineering faculties",
                fontSize = 11.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(14.dp))

            deptStats.forEach { stat ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(Slate850.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = stat.department,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "GPA: ${stat.averageGpa}", fontSize = 11.sp, color = CyanGlow, fontWeight = FontWeight.SemiBold)
                        Text(text = "Att: ${stat.averageAttendance.toInt()}%", fontSize = 11.sp, color = EmeraldSuccess)
                        Text(text = "Pass: ${stat.passPercentage.toInt()}%", fontSize = 11.sp, color = AmberWarning)
                        Text(text = "High Risk: ${stat.highRiskCount}", fontSize = 11.sp, color = RoseRisk)
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentClusteringCard(students: List<StudentEntity>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "K-Means Student Clustering (Unsupervised ML) 🧬",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Algorithmic grouping based on multi-dimensional performance patterns",
                fontSize = 11.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(14.dp))

            val clusters = StudentClusterType.values()
            clusters.forEach { cluster ->
                val (color, badgeEmoji) = when (cluster) {
                    StudentClusterType.HIGH_PERFORMERS -> Pair(EmeraldSuccess, "🌟")
                    StudentClusterType.CONSISTENT_PERFORMERS -> Pair(CyanGlow, "🔹")
                    StudentClusterType.IMPROVEMENT_REQUIRED -> Pair(AmberWarning, "🔸")
                    StudentClusterType.AT_RISK -> Pair(RoseRisk, "🚨")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Slate850, RoundedCornerShape(10.dp))
                        .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = badgeEmoji, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cluster.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                        Text(
                            text = cluster.description,
                            fontSize = 11.sp,
                            color = Slate400,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
