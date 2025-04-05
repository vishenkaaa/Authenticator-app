package com.example.authenticatorapp.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.MainBlue

@Composable
fun CustomCircularProgressIndicator(remainingTime: Int){
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(30.dp)) {
            val strokeWidth = 5.dp.toPx()
            val sweepAngle = 360f * (remainingTime / 30f)

            drawArc(
                color = Color(0xFFC6DFFF),
                startAngle = -90f + sweepAngle,
                sweepAngle = 360f - sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = MainBlue,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

        }

        Text(
            text = remainingTime.toString(),
            style = AppTypography.labelSmall,
            color = MainBlue
        )
    }
}