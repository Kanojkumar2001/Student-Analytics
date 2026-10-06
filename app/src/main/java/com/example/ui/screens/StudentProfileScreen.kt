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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.AiAnalyticsEngine
import com.example.data.local.entity.BadgeEntity
import com.example.data.local.entity.StudentEntity
import com.example.data.local.entity.SubjectGradeEntity
import com.example.ui.components.ExplainableAiCard
import com.example.ui.components.RiskBadge
import com.example.ui.components.SubjectBarChart
import com.example.ui.components.SuccessScoreGauge
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun StudentProfileScreen(
    student: StudentEntity?,
    subjects: List<SubjectGradeEntity>,
    badges: List<BadgeEntity>,
    onLogout: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (student == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Student profile not available.", color = Slate400)
        }
        return
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Academics", "Attendance", "Achievements", "AI Diagnostics")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Profile Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_profile_header"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(IndigoAccent, CircleShape)
                                .border(2.dp, CyanGlow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = student.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString(""),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = student.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                RiskBadge(riskLevel = student.riskLevel)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Roll: ${student.rollNumber} • ${student.department} Year ${student.year}",
                                fontSize = 12.sp,
                                color = Slate400
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact & Meta Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate850, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Email", fontSize = 10.sp, color = Slate400)
                            Text(text = student.email, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text(text = "Phone", fontSize = 10.sp, color = Slate400)
                            Text(text = student.phone, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text(text = "Cluster", fontSize = 10.sp, color = Slate400)
                            Text(text = student.cluster, fontSize = 11.sp, color = CyanGlow, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Tab Selector Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate850, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEachIndexed { idx, tabTitle ->
                    val isSelected = selectedTab == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) IndigoAccent else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedTab = idx }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabTitle,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Slate400
                        )
                    }
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Overview
                item {
                    SuccessScoreGauge(
                        score = student.successScore,
                        attendance = student.attendance,
                        internalMarks = student.internalMarks,
                        assignmentScore = student.assignmentScore
                    )
                }
                item {
                    SubjectBarChart(subjects = subjects)
                }
            }
            1 -> {
                // Academics
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Academic Scorecards", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                MetricColumn("Cumulative GPA", "${student.currentGpa}", CyanGlow)
                                MetricColumn("Predicted GPA", "${student.predictedGpa}", EmeraldSuccess)
                                MetricColumn("Active Backlogs", "${student.backlogs}", if (student.backlogs > 0) RoseRisk else EmeraldSuccess)
                            }
                        }
                    }
                }
                item {
                    SubjectBarChart(subjects = subjects)
                }
            }
            2 -> {
                // Attendance
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Attendance Compliance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Current Overall Attendance: ${student.attendance.toInt()}%",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (student.attendance >= 75) EmeraldSuccess else RoseRisk
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (student.attendance < 75)
                                    "⚠️ Debarment Warning: Requires attending next 8 sessions to cross university 75% cutoff."
                                else
                                    "✅ Regular Attendance: Compliant with examination criteria.",
                                fontSize = 12.sp,
                                color = Slate400
                            )
                        }
                    }
                }
            }
            3 -> {
                // Achievements & Badges
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Badges & Gamification Milestones 🏆", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(12.dp))
                            badges.forEach { badge ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Slate850, RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = badge.iconEmoji, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.size(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = badge.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(text = badge.description, fontSize = 11.sp, color = Slate400)
                                    }
                                    Text(
                                        text = if (badge.isUnlocked) "UNLOCKED ✨" else "LOCKED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (badge.isUnlocked) EmeraldSuccess else Slate400
                                    )
                                }
                            }
                        }
                    }
                }
            }
            4 -> {
                // AI Diagnostics
                item {
                    val factors = AiAnalyticsEngine.generateExplainableFactors(student)
                    ExplainableAiCard(riskScore = student.riskScore, factors = factors)
                }
            }
        }

        if (onLogout != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLogout() }
                        .testTag("btn_profile_logout"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate850),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(RoseRisk.copy(alpha = 0.4f)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🚪 Sign Out of Institutional Session",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseRisk
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

@Composable
private fun MetricColumn(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(text = title, fontSize = 11.sp, color = Slate400)
    }
}
