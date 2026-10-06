package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.data.local.entity.StudentEntity

@Composable
fun AdminDashboardScreen(
    students: List<StudentEntity>,
    onOpenImportDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    HodAdminDashboardScreen(
        students = students,
        onOpenImportDialog = onOpenImportDialog,
        modifier = modifier
    )
}
