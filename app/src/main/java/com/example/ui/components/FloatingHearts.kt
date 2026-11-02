package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.model.FloatingHeart
import kotlin.math.sin

@Composable
fun FloatingHeartsCanvas(
    hearts: List<FloatingHeart>,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "hearts_float")
    val animProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "heart_loop"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        hearts.forEach { heart ->
            val now = System.currentTimeMillis()
            val age = (now - heart.id).coerceAtLeast(0)
            val lifetime = 2500f // 2.5 seconds flight
            val progress = (age / lifetime).coerceIn(0f, 1f)

            if (progress < 1f) {
                val alpha = (1f - progress).coerceIn(0f, 1f)
                val y = canvasHeight * (1f - progress * 0.85f)
                val baseX = canvasWidth * heart.startXFraction
                val wobble = sin(progress * 4f * Math.PI.toFloat()) * 30f
                val x = baseX + wobble

                drawHeartShape(
                    center = Offset(x, y),
                    size = heart.sizeDp * (1f + progress * 0.3f),
                    color = heart.color.copy(alpha = alpha)
                )
            }
        }
    }
}

private fun DrawScope.drawHeartShape(center: Offset, size: Float, color: Color) {
    val width = size
    val height = size
    val path = Path().apply {
        moveTo(center.x, center.y + height * 0.35f)
        cubicTo(
            center.x - width * 0.6f, center.y - height * 0.1f,
            center.x - width * 0.5f, center.y - height * 0.6f,
            center.x, center.y - height * 0.2f
        )
        cubicTo(
            center.x + width * 0.5f, center.y - height * 0.6f,
            center.x + width * 0.6f, center.y - height * 0.1f,
            center.x, center.y + height * 0.35f
        )
        close()
    }
    drawPath(path = path, color = color)
}
