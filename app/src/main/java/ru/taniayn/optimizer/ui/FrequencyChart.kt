package ru.taniayn.optimizer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun FrequencyChart(
    frequency: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    if (frequency.isEmpty()) {
        return
    }

    val maxFrequency =
        frequency.values.maxOrNull()?.toFloat() ?: 1f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(horizontal = 4.dp)
    ) {

        val count = frequency.size

        val step = size.width / count
        val barWidth = step * 0.65f

        frequency.toSortedMap().forEach { (number, value) ->

            val index = number - 1

            val barHeight =
                (size.height - 40.dp.toPx()) *
                        (value / maxFrequency)

            val left =
                index * step +
                        (step - barWidth) / 2

            val top =
                size.height -
                        40.dp.toPx() -
                        barHeight

            // Столбец
            drawRect(
                color = Color(0xFF6750A4),
                topLeft = Offset(left, top),
                size = Size(
                    barWidth,
                    barHeight
                )
            )

            // Подпись числа
            drawContext.canvas.nativeCanvas.drawText(
                number.toString(),
                left + barWidth / 2,
                size.height - 10.dp.toPx(),
                android.graphics.Paint().apply {
                    textSize = 12.dp.toPx()
                    textAlign =
                        android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                    color =
                        android.graphics.Color.DKGRAY
                }
            )
        }
    }
}