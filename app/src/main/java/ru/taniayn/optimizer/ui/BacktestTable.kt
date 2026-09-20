package ru.taniayn.optimizer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.taniayn.optimizer.backtest.BacktestResult

@Composable
fun BacktestTable(
    results: List<BacktestResult>
) {
    if (results.isEmpty()) {
        return
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            // Заголовок таблицы
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Стратегия",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "Среднее",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "Δ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "4+",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "5+",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "Z",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Text(
                    text = "p",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            val randomResult =
                results.firstOrNull { it.strategyName == "RANDOM" }

            results.forEach { result ->

                val percent4 =
                    result.matches4OrMore.toDouble() /
                            result.testedDraws * 100

                val percent5 =
                    result.matches5OrMore.toDouble() /
                            result.testedDraws * 100

                val delta =
                    result.differenceAgainstRandom

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = result.strategyName,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "%.3f".format(
                            result.averageOverlap
                        ),
                        fontSize = 13.sp
                    )

                    Text(
                        text = "%+.3f".format(delta),
                        fontSize = 13.sp
                    )

                    Text(
                        text = "${result.matches4OrMore} " +
                                "(%.1f%%)".format(percent4),
                        fontSize = 13.sp
                    )

                    Text(
                        text = "${result.matches5OrMore} " +
                                "(%.1f%%)".format(percent5),
                        fontSize = 13.sp
                    )

                    Text(
                        text = "%.3f".format(result.zScore),
                        fontSize = 13.sp
                    )

                    Text(
                        text = "%.4f".format(result.pValue),
                        fontSize = 13.sp
                    )
                }
            }


        }
    }
}