package ru.taniayn.optimizer.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.taniayn.optimizer.backtest.BacktestResult

@Composable
fun OptimizationTable(
    results: List<BacktestResult>
) {
    if (results.isEmpty()) {
        return
    }

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = "Стратегия",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Среднее",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Δ",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "4+",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "5+",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Z",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "p",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "p adj.",
            fontSize = 14.sp,
            modifier = Modifier.padding(8.dp)
        )
    }

    results.forEach { result ->

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
                text = "%.3f".format(
                    result.averageOverlap
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "%+.3f".format(
                    result.differenceAgainstRandom
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "${result.matches4OrMore} " +
                        "(%.1f%%)".format(percent4),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "${result.matches5OrMore} " +
                        "(%.1f%%)".format(percent5),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "%.3f".format(
                    result.zScore
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "%.4f".format(
                    result.pValue
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "%.4f".format(
                    result.adjustedPValue
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}