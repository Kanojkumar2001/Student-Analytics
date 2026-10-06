package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun CreateInterventionDialog(
    initialRoll: String,
    initialName: String,
    initialDept: String,
    onDismiss: () -> Unit,
    onSubmit: (roll: String, name: String, dept: String, type: String, issue: String, action: String, faculty: String, deadline: String) -> Unit
) {
    var roll by remember { mutableStateOf(initialRoll) }
    var name by remember { mutableStateOf(initialName) }
    var dept by remember { mutableStateOf(initialDept) }
    var type by remember { mutableStateOf("Remedial Coaching") }
    var issue by remember { mutableStateOf("Low attendance and declining internal exam marks") }
    var action by remember { mutableStateOf("Weekly 1-on-1 tutoring + attendance tracking") }
    var faculty by remember { mutableStateOf("Prof. Ananya Kumar") }
    var deadline by remember { mutableStateOf("25 Oct 2026") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_create_intervention"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Create Student Intervention 🎯",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Establish early-warning academic remedial roadmap",
                    fontSize = 11.sp,
                    color = Slate400
                )

                Spacer(modifier = Modifier.height(4.dp))

                DialogField("Student Roll Number", roll) { roll = it }
                DialogField("Student Name", name) { name = it }
                DialogField("Department", dept) { dept = it }
                DialogField("Intervention Type", type) { type = it }
                DialogField("Observed Risk Factors / Issue", issue) { issue = it }
                DialogField("Action Plan & Remedial Measure", action) { action = it }
                DialogField("Assigned Faculty", faculty) { faculty = it }
                DialogField("Target Resolution Deadline", deadline) { deadline = it }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Cancel", color = Slate400)
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Button(
                        onClick = {
                            onSubmit(roll, name, dept, type, issue, action, faculty, deadline)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_submit_intervention")
                    ) {
                        Text(text = "Assign Intervention", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.Medium)
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
