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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AttendanceManagementScreen(
    students: List<StudentEntity>,
    onRecordAttendance: (roll: String, code: String, attended: Int, total: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val subjects = listOf(
        Pair("CS301", "Data Structures & Algorithms"),
        Pair("CS302", "Database Management Systems"),
        Pair("CS303", "Computer Networks"),
        Pair("CS304", "Operating Systems")
    )

    var selectedSubject by remember { mutableStateOf(subjects[0]) }
    var selectedDept by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    // Map: rollNumber -> Boolean (true: Present, false: Absent)
    val attendanceMap = remember { mutableStateMapOf<String, Boolean>() }

    // Initialize students default to Present if not yet recorded
    students.forEach { student ->
        if (!attendanceMap.containsKey(student.rollNumber)) {
            attendanceMap[student.rollNumber] = true
        }
    }

    val filteredList = remember(students, selectedDept, searchQuery) {
        students.filter { student ->
            val matchesDept = selectedDept == "ALL" || student.department.equals(selectedDept, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    student.name.contains(searchQuery, ignoreCase = true) ||
                    student.rollNumber.contains(searchQuery, ignoreCase = true)
            matchesDept && matchesSearch
        }
    }

    val presentCount = filteredList.count { attendanceMap[it.rollNumber] == true }
    val absentCount = filteredList.size - presentCount
    val attendancePercent = if (filteredList.isNotEmpty()) {
        (presentCount * 100) / filteredList.size
    } else 0

    val todayStr = remember {
        SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date())
    }

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
                        text = "Attendance Management 📋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "$todayStr • Daily Classroom Roster",
                        fontSize = 11.sp,
                        color = CyanGlow
                    )
                }
            }
        }

        // Subject Selector Chips
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Selected Subject: ${selectedSubject.first} — ${selectedSubject.second}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(subjects) { subj ->
                            val isSelected = selectedSubject.first == subj.first
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedSubject = subj },
                                label = {
                                    Text(
                                        text = subj.first,
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
                    }
                }
            }
        }

        // Live Attendance Stats & Quick Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$presentCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        Text(text = "Present", fontSize = 10.sp, color = Slate400)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$absentCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RoseRisk)
                        Text(text = "Absent", fontSize = 10.sp, color = Slate400)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$attendancePercent%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (attendancePercent >= 75) EmeraldSuccess else RoseRisk
                        )
                        Text(text = "Rate", fontSize = 10.sp, color = Slate400)
                    }
                }
            }
        }

        // Batch Action Controls & Search
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                filteredList.forEach { attendanceMap[it.rollNumber] = true }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = EmeraldSuccess
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
                        ) {
                            Text(text = "All Present", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                filteredList.forEach { attendanceMap[it.rollNumber] = false }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = RoseRisk
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RoseRisk.copy(alpha = 0.5f))
                        ) {
                            Text(text = "All Absent", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Filter roster...", color = Slate400, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanGlow,
                            unfocusedBorderColor = SurfaceCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Slate850,
                            unfocusedContainerColor = Slate850
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            }
        }

        // Success Confirmation Banner
        if (saveMessage != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldSuccess.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, EmeraldSuccess.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = saveMessage!!, color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Student Roster
        items(filteredList, key = { it.rollNumber }) { student ->
            val isPresent = attendanceMap[student.rollNumber] != false
            StudentAttendanceRow(
                student = student,
                isPresent = isPresent,
                onToggle = {
                    attendanceMap[student.rollNumber] = !isPresent
                    saveMessage = null
                }
            )
        }

        // Save Attendance Floating / Bottom Button
        item {
            Button(
                onClick = {
                    filteredList.forEach { student ->
                        val isPresent = attendanceMap[student.rollNumber] != false
                        val currentAttended = (student.attendance * 40 / 100).toInt()
                        val newAttended = if (isPresent) currentAttended + 1 else currentAttended
                        val newTotal = 41
                        onRecordAttendance(student.rollNumber, selectedSubject.first, newAttended, newTotal)
                    }
                    saveMessage = "Recorded attendance for ${filteredList.size} students in ${selectedSubject.first}!"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_save_attendance"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanGlow)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Class Attendance (${selectedSubject.first})",
                    color = Color(0xFF0F172A),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StudentAttendanceRow(
    student: StudentEntity,
    isPresent: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .testTag("row_attendance_${student.rollNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPresent) SurfaceCard else Slate850.copy(alpha = 0.7f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isPresent) EmeraldSuccess.copy(alpha = 0.3f) else RoseRisk.copy(alpha = 0.3f)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = "${student.rollNumber} • ${student.department} • Sec ${student.section} • Overall: ${student.attendance.toInt()}%",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }

            // Attendance toggle badge
            Box(
                modifier = Modifier
                    .background(
                        if (isPresent) EmeraldSuccess.copy(alpha = 0.2f) else RoseRisk.copy(alpha = 0.2f),
                        CircleShape
                    )
                    .border(
                        1.dp,
                        if (isPresent) EmeraldSuccess else RoseRisk,
                        CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isPresent) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isPresent) EmeraldSuccess else RoseRisk,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPresent) "PRESENT" else "ABSENT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isPresent) EmeraldSuccess else RoseRisk
                    )
                }
            }
        }
    }
}
