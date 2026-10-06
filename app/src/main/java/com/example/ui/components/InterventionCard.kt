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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.InterventionEntity
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun InterventionCard(
    intervention: InterventionEntity,
    onMarkSuccessful: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusBg, statusColor, statusLabel) = when (intervention.status) {
        "SUCCESSFUL" -> Triple(EmeraldSuccess.copy(alpha = 0.15f), EmeraldSuccess, "Successful ✅")
        "IN_PROGRESS" -> Triple(CyanGlow.copy(alpha = 0.15f), CyanGlow, "In Progress ⏳")
        "COMPLETED" -> Triple(EmeraldSuccess.copy(alpha = 0.15f), EmeraldSuccess, "Completed")
        else -> Triple(AmberWarning.copy(alpha = 0.15f), AmberWarning, "Proposed 📌")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("intervention_card_${intervention.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = intervention.studentName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${intervention.studentRollNumber} • ${intervention.department}",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                }

                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(6.dp))
                        .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Type: ${intervention.type}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanGlow
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Issue: ${intervention.issue}",
                fontSize = 12.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Action: ${intervention.actionPlan}",
                fontSize = 12.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Before vs After impact comparison
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate850, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "BEFORE", fontSize = 10.sp, color = Slate400)
                        Text(text = "Risk: ${intervention.beforeRisk}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoseRisk)
                        Text(text = "Att: ${intervention.beforeAttendance.toInt()}%", fontSize = 11.sp, color = Slate400)
                    }

                    Box(
                        modifier = Modifier
                            .size(1.dp, 36.dp)
                            .background(SurfaceCardBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "AFTER INTERVENTION", fontSize = 10.sp, color = Slate400)
                        Text(text = "Risk: ${intervention.afterRisk}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        Text(text = "Att: ${intervention.afterAttendance.toInt()}%", fontSize = 11.sp, color = EmeraldSuccess)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Faculty: ${intervention.assignedFaculty}\nDeadline: ${intervention.deadline}",
                    fontSize = 11.sp,
                    color = Slate400,
                    lineHeight = 15.sp
                )

                if (intervention.status != "SUCCESSFUL") {
                    Button(
                        onClick = onMarkSuccessful,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_mark_successful_${intervention.id}")
                    ) {
                        Text(text = "Mark Resolved", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
