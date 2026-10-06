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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import com.example.data.ai.AiAnalyticsEngine
import com.example.data.local.entity.StudentEntity
import com.example.data.local.entity.SubjectGradeEntity
import com.example.ui.components.MetricKpiCard
import com.example.ui.components.TopicGapCard
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
fun FacultyDashboardScreen(
    students: List<StudentEntity>,
    onUpdateMarks: (roll: String, code: String, internal: Double, assign: Double, lab: Double) -> Unit,
    onRecordAttendance: (roll: String, code: String, attended: Int, total: Int) -> Unit,
    onSelectStudent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMarksDialog by remember { mutableStateOf(false) }
    var selectedStudentForEdit by remember { mutableStateOf<StudentEntity?>(null) }

    val totalClassStudents = students.size
    val classAvgMarks = if (students.isNotEmpty()) students.map { it.internalMarks }.average().toInt() else 74
    val classAvgAttendance = if (students.isNotEmpty()) students.map { it.attendance }.average().toInt() else 82
    val weakCount = students.count { it.riskLevel == "HIGH" || it.internalMarks < 60 }

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
                        text = "Faculty Course & Gradebook Portal 👨‍🏫",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Subject: Machine Learning (CS302) • Class Section III-A",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyanGlow
                    )
                }
            }
        }

        // Faculty Subject Analytics KPIs (Section 22)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Enrolled Students",
                    value = "$totalClassStudents",
                    iconEmoji = "👥",
                    accentColor = IndigoAccent,
                    trendText = "CSE Sec A",
                    isPositiveTrend = true,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Subject Average",
                    value = "$classAvgMarks%",
                    iconEmoji = "📈",
                    accentColor = CyanGlow,
                    trendText = "+5% vs Mid-1",
                    isPositiveTrend = true,
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
                    title = "Class Attendance",
                    value = "$classAvgAttendance%",
                    iconEmoji = "⏰",
                    accentColor = EmeraldSuccess,
                    trendText = "82% Avg",
                    isPositiveTrend = true,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Needs Remedial",
                    value = "$weakCount",
                    iconEmoji = "⚠️",
                    accentColor = RoseRisk,
                    trendText = "Action Req.",
                    isPositiveTrend = false,
                    subtitle = "Below 60% mark",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Curriculum Learning Gap Detection (Section 23)
        item {
            TopicGapCard(topicGaps = AiAnalyticsEngine.sampleClassTopicGaps)
        }

        // Student Gradebook Table with CRUD Action
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Student Gradebook & Attendance Log (${students.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Real-time sync 🔄",
                    fontSize = 11.sp,
                    color = CyanGlow
                )
            }
        }

        items(students) { student ->
            FacultyGradebookRow(
                student = student,
                onEditClick = {
                    selectedStudentForEdit = student
                    showMarksDialog = true
                },
                onViewProfile = { onSelectStudent(student.rollNumber) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showMarksDialog && selectedStudentForEdit != null) {
        UpdateMarksDialog(
            student = selectedStudentForEdit!!,
            onDismiss = { showMarksDialog = false },
            onSave = { internal, assign, lab, attended, total ->
                onUpdateMarks(selectedStudentForEdit!!.rollNumber, "CS302", internal, assign, lab)
                onRecordAttendance(selectedStudentForEdit!!.rollNumber, "CS302", attended, total)
                showMarksDialog = false
            }
        )
    }
}

@Composable
private fun FacultyGradebookRow(
    student: StudentEntity,
    onEditClick: () -> Unit,
    onViewProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("faculty_grade_row_${student.rollNumber}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).clickable { onViewProfile() }) {
                Text(
                    text = student.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${student.rollNumber} • ${student.department}",
                    fontSize = 11.sp,
                    color = Slate400
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Internal: ${student.internalMarks.toInt()}%", fontSize = 11.sp, color = CyanGlow)
                    Text(text = "Assign: ${student.assignmentScore.toInt()}%", fontSize = 11.sp, color = EmeraldSuccess)
                    Text(text = "Att: ${student.attendance.toInt()}%", fontSize = 11.sp, color = if (student.attendance < 75) RoseRisk else Slate400)
                }
            }

            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_edit_grade_${student.rollNumber}")
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Update", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun UpdateMarksDialog(
    student: StudentEntity,
    onDismiss: () -> Unit,
    onSave: (internal: Double, assign: Double, lab: Double, attended: Int, total: Int) -> Unit
) {
    var internalText by remember { mutableStateOf(student.internalMarks.toInt().toString()) }
    var assignText by remember { mutableStateOf(student.assignmentScore.toInt().toString()) }
    var labText by remember { mutableStateOf(student.labScore.toInt().toString()) }
    var attendedText by remember { mutableStateOf((student.attendance * 45 / 100).toInt().toString()) }
    var totalText by remember { mutableStateOf("45") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Update Marks & Attendance",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${student.name} (${student.rollNumber}) • Real-time Sync",
                    fontSize = 11.sp,
                    color = CyanGlow
                )

                EditGradeField("Internal Exam Mark (%)", internalText) { internalText = it }
                EditGradeField("Assignment Score (%)", assignText) { assignText = it }
                EditGradeField("Lab Practical Score (%)", labText) { labText = it }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        EditGradeField("Attended Classes", attendedText) { attendedText = it }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        EditGradeField("Total Classes", totalText) { totalText = it }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Slate850)
                    ) {
                        Text(text = "Cancel", color = Slate400)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = {
                            val internal = internalText.toDoubleOrNull() ?: 70.0
                            val assign = assignText.toDoubleOrNull() ?: 75.0
                            val lab = labText.toDoubleOrNull() ?: 70.0
                            val attended = attendedText.toIntOrNull() ?: 35
                            val total = totalText.toIntOrNull() ?: 45
                            onSave(internal, assign, lab, attended, total)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                        modifier = Modifier.testTag("btn_save_marks_attendance")
                    ) {
                        Text(text = "Save & Sync", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EditGradeField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontSize = 11.sp, color = Slate400)
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyanGlow,
                unfocusedBorderColor = SurfaceCardBorder,
                focusedContainerColor = Slate850,
                unfocusedContainerColor = Slate850,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}
