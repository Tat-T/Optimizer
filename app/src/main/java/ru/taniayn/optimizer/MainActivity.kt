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

class MainActivity : ComponentActivity() {

    private var drawCount by mutableIntStateOf(0)
    private var numberFrequency by mutableStateOf<Map<Int, Int>>(emptyMap())
    private var additionalNumberFrequency by mutableStateOf<Map<Int, Int>>(emptyMap())

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
    onPickCsv: () -> Unit
) {
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

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (numberFrequency.isNotEmpty()) {

            item {
                Text(
                    text = "📊 Частота основных чисел",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
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

                        Text(
                            text = "$frequency раз",
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