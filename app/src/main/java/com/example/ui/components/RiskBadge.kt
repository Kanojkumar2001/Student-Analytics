package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseRisk

@Composable
fun RiskBadge(
    riskLevel: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (riskLevel.uppercase()) {
        "HIGH" -> Triple(RoseRisk.copy(alpha = 0.15f), RoseRisk, "HIGH RISK 🔴")
        "MEDIUM" -> Triple(AmberWarning.copy(alpha = 0.15f), AmberWarning, "MEDIUM RISK 🟡")
        else -> Triple(EmeraldSuccess.copy(alpha = 0.15f), EmeraldSuccess, "LOW RISK 🟢")
    }

    Box(
        modifier = modifier
            .testTag("risk_badge_${riskLevel.lowercase()}")
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
