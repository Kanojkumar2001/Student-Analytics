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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.StudyTaskEntity
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
fun StudyPlanScreen(
    tasks: List<StudyTaskEntity>,
    onToggleTask: (Int, Boolean) -> Unit,
    studentName: String?,
    modifier: Modifier = Modifier
) {
    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val totalMinutes = tasks.sumOf { it.durationMinutes }

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
                        text = "Personalized 7-Day Study Plan 📅",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Dynamically structured to address learning gaps & upcoming exams",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
            }
        }

        // Summary KPI Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$completedCount / $totalCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CyanGlow)
                        Text(text = "Tasks Completed", fontSize = 11.sp, color = Slate400)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${totalMinutes / 60}h ${totalMinutes % 60}m", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        Text(text = "Total Commitment", fontSize = 11.sp, color = Slate400)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${if (totalCount > 0) (completedCount * 100 / totalCount) else 0}%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                        Text(text = "Weekly Progress", fontSize = 11.sp, color = Slate400)
                    }
                }
            }
        }

        // Daily Task Items
        items(tasks) { task ->
            StudyTaskCard(
                task = task,
                onToggle = { isChecked -> onToggleTask(task.id, isChecked) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StudyTaskCard(
    task: StudyTaskEntity,
    onToggle: (Boolean) -> Unit
) {
    val priorityColor = when (task.priority) {
        "HIGH" -> RoseRisk
        "MEDIUM" -> AmberWarning
        else -> CyanGlow
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("study_task_item_${task.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (task.isCompleted) Slate850.copy(alpha = 0.5f) else SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (task.isCompleted) SurfaceCardBorder.copy(alpha = 0.5f) else SurfaceCardBorder
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = EmeraldSuccess,
                    uncheckedColor = Slate400
                ),
                modifier = Modifier.testTag("checkbox_task_${task.id}")
            )

            Spacer(modifier = Modifier.size(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${task.dayOfWeek} • ${task.subject}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) Slate400 else CyanGlow
                    )

                    Box(
                        modifier = Modifier
                            .background(priorityColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${task.priority} (${task.durationMinutes}m)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = task.taskDescription,
                    fontSize = 13.sp,
                    color = if (task.isCompleted) Slate400 else Color.White,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
