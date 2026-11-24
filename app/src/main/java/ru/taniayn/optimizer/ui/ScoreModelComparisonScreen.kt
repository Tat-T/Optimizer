package ru.taniayn.optimizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.taniayn.optimizer.backtest.ScoreModelComparisonResult

@Composable
fun ScoreModelComparisonScreen(
    result: ScoreModelComparisonResult?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "⚖️ СРАВНЕНИЕ МОДЕЛЕЙ",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("НАЗАД")
        }

        if (result == null || result.results.isEmpty()) {
            Text("Недостаточно данных для сравнения.")
            return
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Случайная стратегия RANDOM")
        Text("Среднее совпадение: %.3f".format(result.randomAverageOverlap))
        Text("Совпадений 4+: ${result.randomMatches4OrMore}")
        Text("Совпадений 5+: ${result.randomMatches5OrMore}")
        Text("Лучшее из 5 — среднее: %.3f".format(result.randomBestAverage))
        Text("Лучшее из 5 — 4+: ${result.randomBest4OrMore}")
        Text("Лучшее из 5 — 5+: ${result.randomBest5OrMore}")

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            item {
                Text(
                    text = "📊 BASIC vs RANDOM — по повторам",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(result.repeatResults) { repeat ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "ПОВТОР №${repeat.repeatNumber}",
                            fontSize = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("BASIC")
                        Text(
                            "Среднее совпадение: %.3f"
                                .format(repeat.basicAverageOverlap)
                        )
                        Text("Совпадений 4+: ${repeat.basicMatches4OrMore}")
                        Text("Совпадений 5+: ${repeat.basicMatches5OrMore}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("RANDOM")
                        Text(
                            "Среднее совпадение: %.3f"
                                .format(repeat.randomAverageOverlap)
                        )
                        Text("Совпадений 4+: ${repeat.randomMatches4OrMore}")
                        Text("Совпадений 5+: ${repeat.randomMatches5OrMore}")
                    }
                }
            }

            items(result.results) { model ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = model.model.name,
                            fontSize = 18.sp
                        )
                        Text("Проверено тиражей: ${model.testedDraws}")
                        Text("Пропущено: ${model.skippedDraws}")
                        Text("Среднее совпадение: %.3f".format(model.averageOverlap))
                        Text("Совпадений 4+: ${model.matches4OrMore}")
                        Text("Совпадений 5+: ${model.matches5OrMore}")
                        Text("Лучшее из 5 — среднее: %.3f".format(model.bestAverageOverlap))
                        Text("Лучшее из 5 — 4+: ${model.best4OrMore}")
                        Text("Лучшее из 5 — 5+: ${model.best5OrMore}")
                        Text("Совпадений доп. числа: ${model.additionalHits}")
                    }
                }
            }
        }
    }
}
