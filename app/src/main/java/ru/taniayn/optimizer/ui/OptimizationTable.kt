package ru.taniayn.optimizer.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.taniayn.optimizer.backtest.BacktestResult
import androidx.compose.material3.HorizontalDivider

@Composable
fun OptimizationTable(results: List<BacktestResult>) {

    if (results.isEmpty()) return

    Column {

        // =========================
        // HOT
        // =========================

        Text(
            text = "🔥 HOT",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 6.dp
            )
        )

        // Заголовок таблицы
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
        )
        {
            Text(
                text = "Стратегия",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Среднее",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Δ",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "4+",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "5+",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Z",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "p",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "p adj.",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        results
            .filter { it.strategyName.startsWith("HOT") }
            .forEach { result ->

                Spacer(modifier = Modifier.padding(vertical = 2.dp))

                HorizontalDivider()

                val percent4 =
                    result.matches4OrMore.toDouble() /
                            result.testedDraws * 100

                val percent5 =
                    result.matches5OrMore.toDouble() /
                            result.testedDraws * 100

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = result.strategyName,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.3f".format(result.averageOverlap),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%+.3f".format(result.differenceAgainstRandom),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "${result.matches4OrMore} (%.1f%%)"
                            .format(percent4),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "${result.matches5OrMore} (%.1f%%)"
                            .format(percent5),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.3f".format(result.zScore),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.4f".format(result.pValue),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.4f".format(result.adjustedPValue),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

        // =========================
        // COLD
        // =========================

        Text(
            text = "❄️ COLD",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 6.dp
            )
        )

        // Заголовок таблицы
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
        )
        {
            Text(
                text = "Стратегия",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Среднее",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Δ",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "4+",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "5+",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Z",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "p",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "p adj.",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }

        results
            .filter { it.strategyName.startsWith("COLD") }
            .forEach { result ->

                Spacer(modifier = Modifier.padding(vertical = 2.dp))

                HorizontalDivider()

                val percent4 =
                    result.matches4OrMore.toDouble() /
                            result.testedDraws * 100

                val percent5 =
                    result.matches5OrMore.toDouble() /
                            result.testedDraws * 100

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = result.strategyName,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.3f".format(result.averageOverlap),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%+.3f".format(result.differenceAgainstRandom),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "${result.matches4OrMore} (%.1f%%)"
                            .format(percent4),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "${result.matches5OrMore} (%.1f%%)"
                            .format(percent5),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.3f".format(result.zScore),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.4f".format(result.pValue),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.4f".format(result.adjustedPValue),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
    }
}