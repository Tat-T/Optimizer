package ru.taniayn.optimizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.taniayn.optimizer.data.CsvParser
import ru.taniayn.optimizer.statistics.StatisticsCalculator
import ru.taniayn.optimizer.ui.BacktestTable
import ru.taniayn.optimizer.ui.FrequencyChart
import androidx.compose.runtime.mutableStateOf
import ru.taniayn.optimizer.ui.AnalysisScreen
import ru.taniayn.optimizer.backtest.BacktestResult
import ru.taniayn.optimizer.backtest.Backtester
import ru.taniayn.optimizer.backtest.Strategies
import ru.taniayn.optimizer.backtest.StatisticalTest
import ru.taniayn.optimizer.backtest.WindowOptimizer
import ru.taniayn.optimizer.ui.BacktestTable
import ru.taniayn.optimizer.ui.OptimizationTable

class MainActivity : ComponentActivity() {

    private var drawCount by mutableIntStateOf(0)
    private var showAnalysis by mutableStateOf(false)
    private var numberFrequency by mutableStateOf<Map<Int, Int>>(emptyMap())
    private var additionalNumberFrequency by mutableStateOf<Map<Int, Int>>(emptyMap())
    private var backtestResults by mutableStateOf<List<BacktestResult>>(emptyList())
    private var optimizationResults by mutableStateOf<List<BacktestResult>>(emptyList())

    private val csvFilePicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->

            if (uri != null) {
                try {

                    val csvText = contentResolver
                        .openInputStream(uri)
                        ?.bufferedReader()
                        ?.use { it.readText() }
                        ?: return@registerForActivityResult

                    val draws = CsvParser.parse(csvText)

                    drawCount = draws.size

                    numberFrequency =
                        StatisticsCalculator.calculateMainNumberFrequency(draws)

                    additionalNumberFrequency =
                        StatisticsCalculator.calculateAdditionalNumberFrequency(draws)

                    optimizationResults =
                        WindowOptimizer.applyHolmCorrection(
                            WindowOptimizer.testAllHotWindows(
                                draws = draws,
                                trainSize = 385
                            ) +
                                    WindowOptimizer.testAllColdWindows(
                                        draws = draws,
                                        trainSize = 385
                                    )
                        )

                    backtestResults = listOf(

                        Backtester.runRandom(
                            draws = draws,
                            trainSize = 385
                        ),

                        Backtester.run(
                            draws = draws,
                            trainSize = 385,
                            strategy = Strategies::hot200,
                            strategyName = "HOT 200"
                        ).let { result ->

                            val statisticalResult =
                                StatisticalTest.compareWithRandom(
                                    draws = draws,
                                    trainSize = 385,
                                    strategy = Strategies::hot200
                                )

                            result.copy(
                                differenceAgainstRandom =
                                    statisticalResult.averageDifference,

                                zScore =
                                    statisticalResult.zScore,

                                pValue =
                                    statisticalResult.pValue
                            )
                        },

                        Backtester.run(
                            draws = draws,
                            trainSize = 385,
                            strategy = Strategies::cold200,
                            strategyName = "COLD 200"
                        ).let { result ->

                            val statisticalResult =
                                StatisticalTest.compareWithRandom(
                                    draws = draws,
                                    trainSize = 385,
                                    strategy = Strategies::cold200
                                )

                            result.copy(
                                differenceAgainstRandom =
                                    statisticalResult.averageDifference,

                                zScore =
                                    statisticalResult.zScore,

                                pValue =
                                    statisticalResult.pValue
                            )
                        },

                        Backtester.run(
                            draws = draws,
                            trainSize = 385,
                            strategy = Strategies::hot50,
                            strategyName = "HOT 50"
                        ).let { result ->

                            val statisticalResult =
                                StatisticalTest.compareWithRandom(
                                    draws = draws,
                                    trainSize = 385,
                                    strategy = Strategies::hot50
                                )

                            result.copy(
                                differenceAgainstRandom =
                                    statisticalResult.averageDifference,

                                zScore =
                                    statisticalResult.zScore,

                                pValue =
                                    statisticalResult.pValue
                            )
                        },

                        Backtester.run(
                            draws = draws,
                            trainSize = 385,
                            strategy = Strategies::cold50,
                            strategyName = "COLD 50"
                        ).let { result ->

                            val statisticalResult =
                                StatisticalTest.compareWithRandom(
                                    draws = draws,
                                    trainSize = 385,
                                    strategy = Strategies::cold50
                                )

                            result.copy(
                                differenceAgainstRandom =
                                    statisticalResult.averageDifference,

                                zScore =
                                    statisticalResult.zScore,

                                pValue =
                                    statisticalResult.pValue
                            )
                        }
                    )
                } catch (e: Exception) {

                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OptimizerApp(
                drawCount = drawCount,
                numberFrequency = numberFrequency,
                additionalNumberFrequency = additionalNumberFrequency,
                showAnalysis = showAnalysis,
                backtestResults = backtestResults,
                optimizationResults = optimizationResults,
                onShowAnalysis = {
                    showAnalysis = true
                },
                onBack = {
                    showAnalysis = false
                },
                onPickCsv = {
                    csvFilePicker.launch(
                        arrayOf(
                            "text/csv",
                            "text/comma-separated-values",
                            "*/*"
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun OptimizerApp(
    drawCount: Int,
    numberFrequency: Map<Int, Int>,
    additionalNumberFrequency: Map<Int, Int>,
    backtestResults: List<BacktestResult>,
    optimizationResults: List<BacktestResult>,
    showAnalysis: Boolean,
    onShowAnalysis: () -> Unit,
    onBack: () -> Unit,
    onPickCsv: () -> Unit
) {
    if (showAnalysis) {
        AnalysisScreen(
            drawCount = drawCount,
            numberFrequency = numberFrequency,
            additionalNumberFrequency = additionalNumberFrequency,
            onBack = onBack
        )
        return
    }
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                text = "🎯",
                fontSize = 56.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ОПТИМИЗАТОР",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Анализатор Рапидо",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Загружено тиражей: $drawCount",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onPickCsv,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📂  ЗАГРУЗИТЬ CSV",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onShowAnalysis,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📊  АНАЛИЗ",
                    fontSize = 17.sp
                )
            }

            if (backtestResults.isNotEmpty()) {

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "🧪 BACKTEST",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                BacktestTable(
                    results = backtestResults
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Проверено тиражей: 386",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Случайных комбинаций RANDOM: ${backtestResults.firstOrNull { it.strategyName == "RANDOM" }?.testedDraws ?: 0}",
                    fontSize = 14.sp
                )
            }

            if (optimizationResults.isNotEmpty()) {

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "⚙️ ОПТИМИЗАЦИЯ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Проверка размеров исторического окна",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OptimizationTable(
                    results = optimizationResults
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (numberFrequency.isNotEmpty()) {

            val averageFrequency =
                StatisticsCalculator.calculateAverageMainFrequency(
                    numberFrequency
                )

            item {
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

                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "🔎 Краткий анализ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Средняя частота: " +
                                    "%.1f".format(averageFrequency),
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        mostFrequent?.let {
                            Text(
                                text = "Самое частое: " +
                                        "${it.key} — ${it.value} раз",
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        leastFrequent?.let {
                            Text(
                                text = "Самое редкое: " +
                                        "${it.key} — ${it.value} раз",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "📊 Частота основных чисел",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                FrequencyChart(
                    frequency = numberFrequency
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(20) { index ->

                val number = index + 1
                val frequency = numberFrequency[number] ?: 0

                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            ),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Число $number",
                            fontSize = 16.sp
                        )

                        val deviation =
                            StatisticsCalculator.calculateDeviation(
                                frequency,
                                averageFrequency
                            )

                        Text(
                            text = "$frequency раз  " +
                                    "(${if (deviation >= 0) "+" else ""}" +
                                    "%.1f".format(deviation) + ")",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "📊 Дополнительные числа",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(4) { index ->

                val number = index + 1
                val frequency = additionalNumberFrequency[number] ?: 0

                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            ),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Число $number",
                            fontSize = 16.sp
                        )

                        Text(
                            text = "$frequency раз",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}