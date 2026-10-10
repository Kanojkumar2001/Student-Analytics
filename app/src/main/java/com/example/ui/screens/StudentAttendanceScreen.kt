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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.local.entity.SubjectGradeEntity
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun StudentAttendanceScreen(
    student: StudentEntity?,
    subjects: List<SubjectGradeEntity>,
    modifier: Modifier = Modifier
) {
    val overallAttendance = student?.attendance?.toInt() ?: 78
    val isShortage = overallAttendance < 75

    // Calculate required classes for 75%
    val totalEstimatedClasses = 200
    val attendedClasses = (totalEstimatedClasses * overallAttendance) / 100
    val target75 = (totalEstimatedClasses * 0.75).toInt()
    val classesNeeded = (target75 - attendedClasses).coerceAtLeast(0)

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
                        text = "Attendance Records ⏰",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${student?.name ?: "Student"} • ${student?.rollNumber ?: "Roll"}",
                        fontSize = 11.sp,
                        color = CyanGlow
                    )
                }
            }
        }

        // Attendance Alert Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isShortage) RoseRisk.copy(alpha = 0.15f) else EmeraldSuccess.copy(alpha = 0.15f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isShortage) RoseRisk.copy(alpha = 0.5f) else EmeraldSuccess.copy(alpha = 0.5f)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                if (isShortage) RoseRisk.copy(alpha = 0.2f) else EmeraldSuccess.copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isShortage) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isShortage) RoseRisk else EmeraldSuccess,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isShortage) "Attendance Shortage Alert" else "Attendance Compliant",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isShortage) RoseRisk else EmeraldSuccess
                        )
                        Text(
                            text = if (isShortage)
                                "Current attendance is $overallAttendance% (below university 75% requirement). You need to attend $classesNeeded more consecutive classes to avoid exam detention."
                            else
                                "Current attendance is $overallAttendance%. You meet the semester examination criteria.",
                            fontSize = 11.sp,
                            color = Slate400,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Overall Attendance Meter
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cumulative Aggregate",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$overallAttendance%",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (overallAttendance >= 75) EmeraldSuccess else RoseRisk
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (overallAttendance / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = if (overallAttendance >= 75) EmeraldSuccess else RoseRisk,
                        trackColor = Slate850
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "0%", fontSize = 10.sp, color = Slate400)
                        Text(text = "75% Mandatory Cutoff", fontSize = 10.sp, color = AmberWarning, fontWeight = FontWeight.Bold)
                        Text(text = "100%", fontSize = 10.sp, color = Slate400)
                    }
                }
            }
        }

        // Subject Roster Header
        item {
            Text(
                text = "Course Attendance Breakdown",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Subject Breakdown Cards
        items(subjects, key = { it.subjectCode }) { subject ->
            val attPct = subject.attendance.toInt()
            val isSubjLow = attPct < 75
            val subjColor = if (isSubjLow) RoseRisk else EmeraldSuccess

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_att_${subject.subjectCode}"),
                shape = RoundedCornerShape(12.dp),
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
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "${subject.subjectCode} • ${subject.credits} Credits",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }

                        Text(
                            text = "$attPct%",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = subjColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (attPct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = subjColor,
                        trackColor = Slate850
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isSubjLow) "Below 75% Requirement" else "Satisfactory",
                            fontSize = 10.sp,
                            color = if (isSubjLow) RoseRisk else EmeraldSuccess,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Status: ${if (isSubjLow) "Detention Risk" else "Eligible"}",
                            fontSize = 10.sp,
                            color = Slate400
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
