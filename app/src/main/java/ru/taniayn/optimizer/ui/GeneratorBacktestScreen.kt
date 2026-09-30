package ru.taniayn.optimizer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.taniayn.optimizer.backtest.GeneratorBacktestResult

@Composable
fun GeneratorBacktestScreen(
    result: GeneratorBacktestResult?,
    onBack: () -> Unit
) {
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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "🧪 BACKTEST ГЕНЕРАТОРА",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        if (result == null) {

            Text(
                text = "Результат ещё не рассчитан.",
                fontSize = 17.sp
            )

        } else {

            Text(
                text = "Проверено тиражей: ${result.testedDraws}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Проверено комбинаций: ${result.testedCombinations}",
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "🎯 ГЕНЕРАТОР",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Среднее: %.3f".format(
                    result.averageOverlap
                ),
                fontSize = 17.sp
            )

            Text(
                text = "4+: ${result.matches4OrMore}",
                fontSize = 17.sp
            )

            Text(
                text = "5+: ${result.matches5OrMore}",
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "🎲 RANDOM",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Среднее: %.3f".format(
                    result.randomAverageOverlap
                ),
                fontSize = 17.sp
            )

            Text(
                text = "4+: ${result.randomMatches4OrMore}",
                fontSize = 17.sp
            )

            Text(
                text = "5+: ${result.bestRecommendation5OrMore}",
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "🏆 ЛУЧШИЙ ИЗ 5 RANDOM",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Комбинация: " +
                        result.bestGeneratorCombination.joinToString("  ") {
                            "%02d".format(it)
                        },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Попадание: " +
                        "${result.bestGeneratorOverlap} из 8",
                fontSize = 17.sp
            )

            Text(
                text = "Фактический тираж: " +
                        result.bestGeneratorActualDraw.joinToString("  ") {
                            "%02d".format(it)
                        },
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Среднее: %.3f".format(
                    result.bestRandomAverage
                ),
                fontSize = 17.sp
            )

            Text(
                text = "4+: ${result.bestRandom4OrMore}",
                fontSize = 17.sp
            )

            Text(
                text = "5+: ${result.bestRandom5OrMore}",
                fontSize = 17.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Что проверяется",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "На каждом шаге генератор использует только " +
                        "предыдущие тиражи. Затем пять сформированных " +
                        "комбинаций сравниваются с фактически выпавшим " +
                        "тиражом.",
                fontSize = 15.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "«Лучший из 5» показывает результат лучшей " +
                        "комбинации из пяти для каждого проверяемого " +
                        "тиража. Это отдельная метрика и она не означает, " +
                        "что заранее известна лучшая комбинация.",
                fontSize = 15.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Важно: исторический backtest показывает, как " +
                        "модель работала на прошлом наборе данных. " +
                        "Он не гарантирует аналогичный результат " +
                        "в будущих тиражах.",
                fontSize = 15.sp
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Button(
            onClick = onBack
        ) {
            Text(
                text = "← НАЗАД",
                fontSize = 16.sp
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}