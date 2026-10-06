package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun DataImportDialog(
    onDismiss: () -> Unit,
    onImportSuccess: (name: String, roll: String, dept: String, att: Double, internal: Double, assign: Double, backlogs: Int) -> Unit
) {
    var rawCsvText by remember {
        mutableStateOf("21A91A0518,Devika Nair,CSE,66.0,52.0,58.0,2")
    }
    var isValidated by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("dialog_import_csv"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = CyanGlow)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = "Import Academic Dataset (CSV)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Schema format: RollNumber, Name, Dept, Attendance, Internal, Assignment, Backlogs",
                    fontSize = 11.sp,
                    color = Slate400
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Slate850, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "StudentID,Name,Dept,Attendance,InternalMarks,AssignmentScore,Backlogs\n$rawCsvText",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CyanGlow
                    )
                }

                OutlinedTextField(
                    value = rawCsvText,
                    onValueChange = {
                        rawCsvText = it
                        isValidated = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("CSV Record Line", color = Slate400) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanGlow,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Slate850,
                        unfocusedContainerColor = Slate850,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isValidated) "✅ Validated ML Features" else "Pending validation",
                        fontSize = 11.sp,
                        color = if (isValidated) EmeraldSuccess else Slate400
                    )

                    Button(
                        onClick = { isValidated = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Slate850),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Validate", color = CyanGlow, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                        Text(text = "Cancel", color = Slate400)
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Button(
                        onClick = {
                            val parts = rawCsvText.split(",")
                            if (parts.size >= 7) {
                                val roll = parts[0].trim()
                                val name = parts[1].trim()
                                val dept = parts[2].trim()
                                val att = parts[3].trim().toDoubleOrNull() ?: 70.0
                                val internal = parts[4].trim().toDoubleOrNull() ?: 60.0
                                val assign = parts[5].trim().toDoubleOrNull() ?: 65.0
                                val backlogs = parts[6].trim().toIntOrNull() ?: 0
                                onImportSuccess(name, roll, dept, att, internal, assign, backlogs)
                            }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_confirm_import_csv")
                    ) {
                        Text(text = "Ingest & Predict", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
