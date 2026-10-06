package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.entity.InterventionEntity
import com.example.ui.components.InterventionCard
import com.example.ui.components.MetricKpiCard
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
fun InterventionsScreen(
    interventions: List<InterventionEntity>,
    onCreateInterventionClick: () -> Unit,
    onMarkSuccessful: (InterventionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var statusFilter by remember { mutableStateOf("ALL") }

    val filteredList = interventions.filter {
        statusFilter == "ALL" || it.status == statusFilter
    }

    val total = interventions.size
    val inProgress = interventions.count { it.status == "IN_PROGRESS" }
    val successful = interventions.count { it.status == "SUCCESSFUL" }

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
                        text = "Intervention Management 🎯",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Targeted actions, remedial programs & measured outcomes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }

                Button(
                    onClick = onCreateInterventionClick,
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoAccent),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("btn_interventions_create_new")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "New Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 3 Summary KPIs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricKpiCard(
                    title = "Total Active",
                    value = "$total",
                    iconEmoji = "📋",
                    accentColor = IndigoAccent,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "In Progress",
                    value = "$inProgress",
                    iconEmoji = "⏳",
                    accentColor = AmberWarning,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Resolved / Success",
                    value = "$successful",
                    iconEmoji = "✅",
                    accentColor = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Status Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusChip("ALL", statusFilter) { statusFilter = it }
                StatusChip("IN_PROGRESS", statusFilter) { statusFilter = it }
                StatusChip("SUCCESSFUL", statusFilter) { statusFilter = it }
                StatusChip("PROPOSED", statusFilter) { statusFilter = it }
            }
        }

        // List of interventions
        items(filteredList) { item ->
            InterventionCard(
                intervention = item,
                onMarkSuccessful = { onMarkSuccessful(item) }
            )
        }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No interventions found matching filter.", color = Slate400)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatusChip(name: String, current: String, onSelect: (String) -> Unit) {
    val isSelected = name == current
    val label = when (name) {
        "ALL" -> "All"
        "IN_PROGRESS" -> "In Progress"
        "SUCCESSFUL" -> "Successful"
        "PROPOSED" -> "Proposed"
        else -> name
    }

    Box(
        modifier = Modifier
            .background(
                if (isSelected) CyanGlow else Slate850,
                RoundedCornerShape(8.dp)
            )
            .clickable { onSelect(name) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFF0F172A) else Slate400
        )
    }
}
