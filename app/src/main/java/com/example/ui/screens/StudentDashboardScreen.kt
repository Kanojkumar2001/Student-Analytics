package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ai.AiAnalyticsEngine
import com.example.data.local.entity.BadgeEntity
import com.example.data.local.entity.StudentEntity
import com.example.data.local.entity.SubjectGradeEntity
import com.example.ui.components.AiRecommendationCard
import com.example.ui.components.ExplainableAiCard
import com.example.ui.components.MetricKpiCard
import com.example.ui.components.PerformanceTrendChart
import com.example.ui.components.RiskBadge
import com.example.ui.components.SubjectBarChart
import com.example.ui.components.SuccessScoreGauge
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
fun StudentDashboardScreen(
    student: StudentEntity?,
    subjects: List<SubjectGradeEntity>,
    badges: List<BadgeEntity>,
    onNavigateToChat: () -> Unit,
    onNavigateToStudyPlan: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (student == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading student analytics...", color = Slate400)
        }
        return
    }

    val history = listOf(
        "Sem 1" to 6.0,
        "Sem 2" to 6.3,
        "Sem 3" to student.previousGpa,
        "Current" to student.currentGpa
    )

    val weakSubjects = subjects.filter { it.overallScore < 70 || it.aiStatus == "NEEDS_ATTENTION" }
    val recommendations = AiAnalyticsEngine.generateRecommendations(student, weakSubjects)
    val factors = AiAnalyticsEngine.generateExplainableFactors(student)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Hero Welcome Card with Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_banner),
                            contentDescription = "Analytics Hero Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        listOf(Color.Transparent, SurfaceCard)
                                    )
                                )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Welcome, ${student.name.split(" ").first()} 👋",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "${student.department} • Year ${student.year} • Sec ${student.section} • Roll: ${student.rollNumber}",
                                fontSize = 12.sp,
                                color = Slate400
                            )
                        }

                        RiskBadge(riskLevel = student.riskLevel)
                    }
                }
            }
        }

        // 4 Primary KPI Cards in 2x2 Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Overall Score",
                    value = "${student.internalMarks.toInt()}%",
                    iconEmoji = "📊",
                    accentColor = IndigoAccent,
                    trendText = "+4% trend",
                    isPositiveTrend = true,
                    subtitle = "CGPA: ${student.currentGpa}",
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Attendance",
                    value = "${student.attendance.toInt()}%",
                    iconEmoji = "⏰",
                    accentColor = if (student.attendance >= 75.0) CyanGlow else RoseRisk,
                    trendText = if (student.attendance >= 75.0) "Eligible" else "Low Alert",
                    isPositiveTrend = student.attendance >= 75.0,
                    subtitle = "Cutoff: 75%",
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
                    title = "Assignments",
                    value = "${student.assignmentScore.toInt()}%",
                    iconEmoji = "📝",
                    accentColor = EmeraldSuccess,
                    trendText = "91% on-time",
                    isPositiveTrend = true,
                    subtitle = "10 Submitted",
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Academic Risk",
                    value = "${student.riskScore}%",
                    iconEmoji = if (student.riskScore > 60) "⚠️" else "🛡️",
                    accentColor = if (student.riskScore > 60) RoseRisk else if (student.riskScore > 30) AmberWarning else EmeraldSuccess,
                    trendText = student.riskLevel,
                    isPositiveTrend = student.riskScore <= 30,
                    subtitle = "Cluster: ${student.cluster}",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // AI Student Assistant CTA Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToChat() }
                    .testTag("ai_assistant_banner_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoAccent.copy(alpha = 0.2f)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(IndigoAccent.copy(alpha = 0.5f)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(IndigoAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Spark",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        Column {
                            Text(
                                text = "Ask EduPulse AI Assistant",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "\"Why is my risk high?\" or \"Build study plan\"",
                                fontSize = 11.sp,
                                color = CyanGlow
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Chat",
                        tint = CyanGlow
                    )
                }
            }
        }

        // Student Success Score Gauge
        item {
            SuccessScoreGauge(
                score = student.successScore,
                attendance = student.attendance,
                internalMarks = student.internalMarks,
                assignmentScore = student.assignmentScore
            )
        }

        // Performance Trend Chart
        item {
            PerformanceTrendChart(
                semesterHistory = history,
                predictedGpa = student.predictedGpa
            )
        }

        // Subject Performance Cards
        item {
            SubjectBarChart(subjects = subjects)
        }

        // Explainable AI Risk Diagnostic
        item {
            ExplainableAiCard(
                riskScore = student.riskScore,
                factors = factors
            )
        }

        // AI Prescriptive Recommendations
        item {
            AiRecommendationCard(recommendations = recommendations)
        }

        // Personalized Study Plan Quick Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToStudyPlan() }
                    .testTag("study_plan_quick_card"),
                shape = RoundedCornerShape(16.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📅", fontSize = 18.sp)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Personalized 7-Day Study Plan",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tailored to weakest subjects (Computer Networks & ML). Tap to view and mark tasks complete.",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    Button(
                        onClick = onNavigateToStudyPlan,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = "View Plan", color = Color(0xFF0F172A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Gamification & Badges
        item {
            BadgesSummarySection(badges = badges)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BadgesSummarySection(badges: List<BadgeEntity>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                    text = "🏆 Badges & Achievements",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${badges.count { it.isUnlocked }} / ${badges.size} Unlocked",
                    fontSize = 11.sp,
                    color = AmberWarning,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                badges.take(4).forEach { badge ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (badge.isUnlocked) IndigoAccent.copy(alpha = 0.15f) else Slate850,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                if (badge.isUnlocked) IndigoAccent.copy(alpha = 0.4f) else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = badge.iconEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = badge.title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (badge.isUnlocked) Color.White else Slate400,
                            maxLines = 1
                        )
                        Text(
                            text = "+${badge.points} pts",
                            fontSize = 9.sp,
                            color = if (badge.isUnlocked) CyanGlow else Slate400
                        )
                    }
                }
            }
        }
    }
}
