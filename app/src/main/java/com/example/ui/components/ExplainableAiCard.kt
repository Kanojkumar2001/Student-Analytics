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
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.model.RiskFactorContribution
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseRisk
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun ExplainableAiCard(
    riskScore: Int,
    factors: List<RiskFactorContribution>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("explainable_ai_card"),
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
                        text = "Explainable AI Risk Diagnostic 🧠",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Why is the student evaluated at $riskScore% Risk?",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }

                Box(
                    modifier = Modifier
                        .background(RoseRisk.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Risk: $riskScore%",
                        color = RoseRisk,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Primary Risk Factor Drivers:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                factors.forEach { factor ->
                    FactorProgressItem(factor)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Interpretation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyanGlow.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .border(1.dp, CyanGlow.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💡", fontSize = 16.sp)
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "AI Synthetic Interpretation",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "The student is currently flagged in the critical risk bracket primarily due to substandard attendance (68%) below the mandatory 75% cutoff, combined with declining mid-term exam scores (54%) and 2 uncleared backlogs. Early faculty intervention and focused remedial classes will reverse this trajectory.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }
            }
        }
    }
}

@Composable
private fun FactorProgressItem(factor: RiskFactorContribution) {
    val barColor = if (factor.isNegative) RoseRisk else EmeraldSuccess

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = factor.factorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = factor.currentValue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "(${factor.statusText})",
                    fontSize = 10.sp,
                    color = Slate400
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { factor.impactWeight },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = barColor,
            trackColor = Slate850
        )
    }
}
