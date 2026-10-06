package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder

@Composable
fun PerformanceTrendChart(
    semesterHistory: List<Pair<String, Double>>,
    predictedGpa: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("performance_trend_chart"),
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
                        text = "GPA Performance Trend 📈",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Historical Semesters vs AI Projected Target",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LegendItem("Historical", CyanGlow)
                    LegendItem("AI Forecast", EmeraldSuccess)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Canvas Chart Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    val bottomPadding = 30.dp.toPx()
                    val chartHeight = h - bottomPadding

                    // Draw 4 horizontal grid lines (GPA 10, 8, 6, 4)
                    val gpaSteps = listOf(10.0, 8.0, 6.0, 4.0)
                    gpaSteps.forEach { gpaVal ->
                        val y = (1.0 - (gpaVal - 4.0) / 6.0).toFloat() * chartHeight
                        drawLine(
                            color = Slate800,
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }

                    // Combined points: History + Predicted
                    val allPoints = semesterHistory + ("Predicted" to predictedGpa)
                    val pointOffsets = mutableListOf<Offset>()
                    val stepX = w / (allPoints.size - 1)

                    allPoints.forEachIndexed { i, pair ->
                        val x = i * stepX
                        val normalizedY = ((1.0 - ((pair.second - 4.0) / 6.0).coerceIn(0.0, 1.0)) * chartHeight).toFloat()
                        pointOffsets.add(Offset(x, normalizedY))
                    }

                    // Area fill gradient for history
                    val pathFill = Path().apply {
                        moveTo(pointOffsets.first().x, pointOffsets.first().y)
                        for (i in 1 until pointOffsets.size) {
                            lineTo(pointOffsets[i].x, pointOffsets[i].y)
                        }
                        lineTo(pointOffsets.last().x, chartHeight)
                        lineTo(pointOffsets.first().x, chartHeight)
                        close()
                    }

                    drawPath(
                        path = pathFill,
                        brush = Brush.verticalGradient(
                            listOf(CyanGlow.copy(alpha = 0.25f), Color.Transparent),
                            startY = 0f,
                            endY = chartHeight
                        )
                    )

                    // Historical Line
                    val historyPath = Path().apply {
                        moveTo(pointOffsets.first().x, pointOffsets.first().y)
                        for (i in 1 until pointOffsets.size - 1) {
                            lineTo(pointOffsets[i].x, pointOffsets[i].y)
                        }
                    }
                    drawPath(
                        path = historyPath,
                        color = CyanGlow,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Forecast dashed line to predicted
                    if (pointOffsets.size >= 2) {
                        val lastReal = pointOffsets[pointOffsets.size - 2]
                        val predicted = pointOffsets.last()
                        drawLine(
                            color = EmeraldSuccess,
                            start = lastReal,
                            end = predicted,
                            strokeWidth = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                        )
                    }

                    // Draw dots
                    pointOffsets.forEachIndexed { i, offset ->
                        val isPredicted = i == pointOffsets.size - 1
                        drawCircle(
                            color = if (isPredicted) EmeraldSuccess else CyanGlow,
                            radius = if (isPredicted) 6.dp.toPx() else 5.dp.toPx(),
                            center = offset
                        )
                        drawCircle(
                            color = Slate850,
                            radius = 2.5.dp.toPx(),
                            center = offset
                        )
                    }
                }
            }

            // X-Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val labels = semesterHistory.map { it.first } + "Pred. Sem"
                labels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (label.startsWith("Pred")) EmeraldSuccess else Slate400,
                        fontWeight = if (label.startsWith("Pred")) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = text, fontSize = 11.sp, color = Slate400)
    }
}
