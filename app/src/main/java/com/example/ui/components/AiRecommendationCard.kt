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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun AiRecommendationCard(
    recommendations: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_recommendations_card"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceCardBorder))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AI Prescriptive Recommendations 🎯",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Actionable Steps to Maximize Academic Potential",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }

                Box(
                    modifier = Modifier
                        .background(IndigoAccent.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Real-time",
                        color = CyanGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                recommendations.forEachIndexed { idx, rec ->
                    RecommendationItem(idx + 1, rec)
                }
            }
        }
    }
}

@Composable
private fun RecommendationItem(index: Int, text: String) {
    val icon = when {
        text.contains("attendance", ignoreCase = true) -> "⏰"
        text.contains("remedial", ignoreCase = true) || text.contains("coaching", ignoreCase = true) -> "⚠️"
        text.contains("backlog", ignoreCase = true) -> "🔄"
        text.contains("assignment", ignoreCase = true) -> "📝"
        text.contains("faculty", ignoreCase = true) -> "👨‍🏫"
        else -> "✨"
    }

    val iconColor = when {
        text.contains("remedial", ignoreCase = true) || text.contains("backlog", ignoreCase = true) -> AmberWarning
        text.contains("attendance", ignoreCase = true) -> CyanGlow
        else -> EmeraldSuccess
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate850.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .border(1.dp, iconColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
