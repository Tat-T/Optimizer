package ru.taniayn.optimizer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.taniayn.optimizer.statistics.StatisticsCalculator
import androidx.compose.material3.Button

@Composable
fun AnalysisScreen(
    drawCount: Int,
    numberFrequency: Map<Int, Int>,
    additionalNumberFrequency: Map<Int, Int>,
    onBack: () -> Unit
) {
    val averageFrequency =
        StatisticsCalculator.calculateAverageMainFrequency(
            numberFrequency
        )

    val mostFrequent =
        StatisticsCalculator.findMostFrequentNumber(
            numberFrequency
        )

    val leastFrequent =
        StatisticsCalculator.findLeastFrequentNumber(
            numberFrequency
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = onBack
        ) {
            Text(
                text = "← НАЗАД",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "📊 АНАЛИЗ",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Количество тиражей: $drawCount",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Средняя частота основных чисел: " +
                    "%.1f".format(averageFrequency),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        mostFrequent?.let {
            Text(
                text = "Максимальная частота: " +
                        "${it.key} — ${it.value} раз",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        leastFrequent?.let {
            Text(
                text = "Минимальная частота: " +
                        "${it.key} — ${it.value} раз",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Частота основных чисел",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        FrequencyChart(
            frequency = numberFrequency
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Дополнительные числа",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        additionalNumberFrequency
            .toSortedMap()
            .forEach { (number, frequency) ->

                Text(
                    text = "Число $number — $frequency раз",
                    fontSize = 17.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
    }
}