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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.local.entity.StudentEntity
import com.example.data.local.entity.SubjectGradeEntity
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun StudentPerformanceScreen(
    student: StudentEntity?,
    subjects: List<SubjectGradeEntity>,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "WEAK", "STRONG"

    val filteredSubjects = remember(subjects, selectedFilter) {
        when (selectedFilter) {
            "WEAK" -> subjects.filter { it.overallScore < 65 || it.aiStatus == "CRITICAL" || it.aiStatus == "NEEDS_ATTENTION" }
            "STRONG" -> subjects.filter { it.overallScore >= 75 }
            else -> subjects
        }
    }

    val avgMarks = if (subjects.isNotEmpty()) {
        subjects.map { it.overallScore }.average().toInt()
    } else 72

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Academic Performance 📊",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${student?.name ?: "Student"} • ${student?.department ?: "CSE"} • Sem ${student?.semester ?: 5}",
                        fontSize = 11.sp,
                        color = CyanGlow
                    )
                }
            }
        }

        // GPA and Aggregate Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScoreCard(
                    title = "Current GPA",
                    value = "${student?.currentGpa ?: 7.4}",
                    color = CyanGlow,
                    modifier = Modifier.weight(1f)
                )
                ScoreCard(
                    title = "Predicted GPA",
                    value = "${student?.predictedGpa ?: 7.6}",
                    color = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
                ScoreCard(
                    title = "Avg Marks",
                    value = "$avgMarks%",
                    color = IndigoAccent,
                    modifier = Modifier.weight(1f)
                )
                ScoreCard(
                    title = "Backlogs",
                    value = "${student?.backlogs ?: 0}",
                    color = if ((student?.backlogs ?: 0) > 0) RoseRisk else EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SubjectFilterChip(
                        label = "All Subjects (${subjects.size})",
                        isSelected = selectedFilter == "ALL",
                        onClick = { selectedFilter = "ALL" }
                    )
                }
                item {
                    SubjectFilterChip(
                        label = "Needs Focus (Score < 65%)",
                        isSelected = selectedFilter == "WEAK",
                        onClick = { selectedFilter = "WEAK" }
                    )
                }
                item {
                    SubjectFilterChip(
                        label = "Distinctions (Score ≥ 75%)",
                        isSelected = selectedFilter == "STRONG",
                        onClick = { selectedFilter = "STRONG" }
                    )
                }
            }
        }

        // AI Advisory on Performance
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoAccent.copy(alpha = 0.15f)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(IndigoAccent.copy(alpha = 0.4f))
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CyanGlow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Subject Diagnostic",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if ((student?.riskScore ?: 0) > 60)
                                "Focus on Mid-Term preparation in Database Systems and Mathematics to recover GPA before the semester exam."
                            else
                                "Strong consistent performance across coursework. Maintain revision schedules to secure placement eligibility.",
                            fontSize = 11.sp,
                            color = Slate400,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Subject Breakdown Cards
        items(filteredSubjects, key = { it.subjectCode }) { subject ->
            SubjectGradeCard(subject = subject)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ScoreCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = title, fontSize = 9.sp, color = Slate400)
        }
    }
}

@Composable
private fun SubjectFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF0F172A) else Color.White
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CyanGlow,
            containerColor = Slate850
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) CyanGlow else SurfaceCardBorder
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun SubjectGradeCard(subject: SubjectGradeEntity) {
    val grade = when {
        subject.overallScore >= 85 -> "A+"
        subject.overallScore >= 75 -> "A"
        subject.overallScore >= 65 -> "B"
        subject.overallScore >= 50 -> "C"
        else -> "F"
    }

    val gradeColor = when {
        grade.startsWith("A") -> EmeraldSuccess
        grade.startsWith("B") -> CyanGlow
        grade.startsWith("C") -> AmberWarning
        else -> RoseRisk
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_subject_${subject.subjectCode}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.subjectName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${subject.subjectCode} • ${subject.credits} Credits • Status: ${subject.aiStatus}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }

                Box(
                    modifier = Modifier
                        .background(gradeColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, gradeColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Grade $grade",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = gradeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { (subject.overallScore / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = gradeColor,
                trackColor = Slate850
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ComponentMark("Internal", "${subject.internalMarks.toInt()}/40")
                ComponentMark("Assignment", "${subject.assignmentMarks.toInt()}/20")
                ComponentMark("Lab", "${subject.labMarks.toInt()}/50")
                ComponentMark("Attendance", "${subject.attendance.toInt()}%")
                ComponentMark("Overall Score", "${subject.overallScore.toInt()}/100")
            }
        }
    }
}

@Composable
private fun ComponentMark(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 9.sp, color = Slate400)
    }
}
