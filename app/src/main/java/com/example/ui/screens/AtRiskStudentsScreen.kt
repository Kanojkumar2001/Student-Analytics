package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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

@Composable
fun AtRiskStudentsScreen(
    students: List<StudentEntity>,
    onSelectStudent: (String) -> Unit,
    onCreateInterventionForStudent: (StudentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRiskFilter by remember { mutableStateOf("ALL") } // "ALL", "HIGH", "MEDIUM"
    var selectedDeptFilter by remember { mutableStateOf("ALL") }

    // Filter students
    val atRiskStudents = remember(students, searchQuery, selectedRiskFilter, selectedDeptFilter) {
        students.filter { student ->
            val isAtRiskCohort = student.riskLevel == "HIGH" || student.riskLevel == "MEDIUM" || student.riskScore >= 45
            val matchesRisk = when (selectedRiskFilter) {
                "HIGH" -> student.riskLevel == "HIGH" || student.riskScore >= 65
                "MEDIUM" -> student.riskLevel == "MEDIUM" || (student.riskScore in 45..64)
                else -> isAtRiskCohort
            }
            val matchesDept = selectedDeptFilter == "ALL" || student.department.equals(selectedDeptFilter, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    student.name.contains(searchQuery, ignoreCase = true) ||
                    student.rollNumber.contains(searchQuery, ignoreCase = true)

            matchesRisk && matchesDept && matchesSearch
        }.sortedByDescending { it.riskScore }
    }

    val highRiskCount = students.count { it.riskLevel == "HIGH" || it.riskScore >= 65 }
    val mediumRiskCount = students.count { it.riskLevel == "MEDIUM" || (it.riskScore in 45..64) }
    val avgAttendance = if (atRiskStudents.isNotEmpty()) {
        atRiskStudents.map { it.attendance }.average().toInt()
    } else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "At-Risk Early Warning 🚨",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "AI-Identified Students Needing Immediate Academic Intervention",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }
            }
        }

        // Summary Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryKpiCard(
                    title = "High Risk",
                    value = "$highRiskCount",
                    badgeColor = RoseRisk,
                    modifier = Modifier.weight(1f)
                )
                SummaryKpiCard(
                    title = "Medium Risk",
                    value = "$mediumRiskCount",
                    badgeColor = AmberWarning,
                    modifier = Modifier.weight(1f)
                )
                SummaryKpiCard(
                    title = "Cohort Att.",
                    value = "$avgAttendance%",
                    badgeColor = if (avgAttendance < 75) RoseRisk else EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Search & Filters
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
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_search_at_risk"),
                        placeholder = { Text("Search by student name or roll...", color = Slate400, fontSize = 12.sp) },
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Risk Level:", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        item {
                            RiskFilterChip(
                                label = "All At-Risk (${highRiskCount + mediumRiskCount})",
                                isSelected = selectedRiskFilter == "ALL",
                                onClick = { selectedRiskFilter = "ALL" },
                                color = CyanGlow
                            )
                        }
                        item {
                            RiskFilterChip(
                                label = "High Risk ($highRiskCount)",
                                isSelected = selectedRiskFilter == "HIGH",
                                onClick = { selectedRiskFilter = "HIGH" },
                                color = RoseRisk
                            )
                        }
                        item {
                            RiskFilterChip(
                                label = "Medium Risk ($mediumRiskCount)",
                                isSelected = selectedRiskFilter == "MEDIUM",
                                onClick = { selectedRiskFilter = "MEDIUM" },
                                color = AmberWarning
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Department:", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val depts = listOf("ALL", "CSE", "ECE", "EEE", "MECH")
                        items(depts) { dept ->
                            RiskFilterChip(
                                label = dept,
                                isSelected = selectedDeptFilter == dept,
                                onClick = { selectedDeptFilter = dept },
                                color = IndigoAccent
                            )
                        }
                    }
                }
            }
        }

        // Student List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Identified Cohort (${atRiskStudents.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Sorted by Risk Score",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }
        }

        if (atRiskStudents.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🎉", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Students Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "No students meet the current at-risk criteria in this department.",
                            fontSize = 12.sp,
                            color = Slate400,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        } else {
            items(atRiskStudents, key = { it.rollNumber }) { student ->
                AtRiskStudentCard(
                    student = student,
                    onSelect = { onSelectStudent(student.rollNumber) },
                    onCreateIntervention = { onCreateInterventionForStudent(student) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SummaryKpiCard(
    title: String,
    value: String,
    badgeColor: Color,
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
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = badgeColor)
            Text(text = title, fontSize = 10.sp, color = Slate400, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun RiskFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color
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
            selectedContainerColor = color,
            containerColor = Slate850
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) color else SurfaceCardBorder
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun AtRiskStudentCard(
    student: StudentEntity,
    onSelect: () -> Unit,
    onCreateIntervention: () -> Unit
) {
    val isHighRisk = student.riskLevel == "HIGH" || student.riskScore >= 65
    val riskColor = if (isHighRisk) RoseRisk else AmberWarning

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .testTag("card_at_risk_${student.rollNumber}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(riskColor.copy(alpha = 0.35f))
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Name & Risk Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${student.rollNumber} • ${student.department} • Sec ${student.section}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }

                Box(
                    modifier = Modifier
                        .background(riskColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, riskColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${student.riskScore}% Risk",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = riskColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { student.riskScore / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = riskColor,
                trackColor = Slate850
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricSnippet(
                    label = "Attendance",
                    value = "${student.attendance.toInt()}%",
                    color = if (student.attendance < 75) RoseRisk else EmeraldSuccess
                )
                MetricSnippet(
                    label = "Internal",
                    value = "${student.internalMarks.toInt()}/40",
                    color = if (student.internalMarks < 24) AmberWarning else CyanGlow
                )
                MetricSnippet(
                    label = "Backlogs",
                    value = "${student.backlogs}",
                    color = if (student.backlogs > 0) RoseRisk else EmeraldSuccess
                )
                MetricSnippet(
                    label = "Est. GPA",
                    value = "${student.predictedGpa}",
                    color = CyanGlow
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCreateIntervention,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("btn_intervene_${student.rollNumber}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Handshake,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Intervene", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onSelect,
                    modifier = Modifier
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate850)
                ) {
                    Text(text = "Profile", fontSize = 11.sp, color = CyanGlow, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = CyanGlow
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricSnippet(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, fontSize = 10.sp, color = Slate400)
    }
}
