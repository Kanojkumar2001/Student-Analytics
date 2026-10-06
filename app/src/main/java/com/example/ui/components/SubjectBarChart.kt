package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun SubjectBarChart(
    subjects: List<SubjectGradeEntity>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("subject_performance_card"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Subject Performance & AI Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Internal + Assignments + Lab + Attendance Diagnostics",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                subjects.forEach { item ->
                    SubjectRowItem(item)
                }
            }
        }
    }
}

@Composable
private fun SubjectRowItem(subject: SubjectGradeEntity) {
    val (statusBg, statusColor, statusLabel) = when (subject.aiStatus) {
        "EXCELLENT" -> Triple(EmeraldSuccess.copy(alpha = 0.15f), EmeraldSuccess, "EXCELLENT")
        "GOOD" -> Triple(CyanGlow.copy(alpha = 0.15f), CyanGlow, "GOOD")
        "NEEDS_ATTENTION" -> Triple(AmberWarning.copy(alpha = 0.15f), AmberWarning, "NEEDS ATTENTION ⚠️")
        else -> Triple(RoseRisk.copy(alpha = 0.15f), RoseRisk, "CRITICAL 🔴")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate850.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = subject.subjectName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = "${subject.subjectCode} • ${subject.credits} Credits",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${subject.overallScore.toInt()}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(6.dp))
                        .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { (subject.overallScore / 100f).toFloat().coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = statusColor,
            trackColor = Slate850
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Sub breakdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SubMetric("Internal", "${subject.internalMarks.toInt()}%")
            SubMetric("Assign.", "${subject.assignmentMarks.toInt()}%")
            SubMetric("Lab", "${subject.labMarks.toInt()}%")
            SubMetric("Att.", "${subject.attendance.toInt()}%")
        }
    }
}

@Composable
private fun SubMetric(label: String, value: String) {
    Text(
        text = "$label: $value",
        fontSize = 10.sp,
        color = Slate400
    )
}
