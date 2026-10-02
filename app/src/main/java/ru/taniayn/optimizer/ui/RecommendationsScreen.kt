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
import ru.taniayn.optimizer.backtest.CombinationScore

@Composable
fun RecommendationsScreen(
    combinations: List<CombinationScore>,
    onBack: () -> Unit,
    onBacktest: () -> Unit
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
            text = "🎯 РЕКОМЕНДУЕМЫЕ\nКОМБИНАЦИИ",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Комбинации сформированы на основе " +
                    "исторической модели и отобраны с учётом " +
                    "разнообразия.",
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        if (combinations.isEmpty()) {

            Text(
                text = "Комбинации ещё не сгенерированы.",
                fontSize = 17.sp
            )

        } else {

            combinations.forEachIndexed { index, result ->

                Text(
                    text = "Вариант ${index + 1}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = result.combination
                        .joinToString("  ") {
                            "%02d".format(it)
                        },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "➕ Дополнительное число: " +
                            "%02d".format(result.additionalNumber),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Score: %.3f".format(result.score),
                    fontSize = 16.sp
                )

                Text(
                    text = "Частоты: %.3f".format(
                        result.frequencyScore
                    ),
                    fontSize = 14.sp
                )
                Text(
                    text = "Доп. число: %.3f".format(
                        result.additionalScore
                    ),
                    fontSize = 14.sp
                )

                Text(
                    text = "HOT/COLD: %.3f".format(
                        result.balanceScore
                    ),
                    fontSize = 14.sp
                )

                Text(
                    text = "Чёт/нечёт: %.3f".format(
                        result.parityScore
                    ),
                    fontSize = 14.sp
                )

                Text(
                    text = "Сумма: %.3f".format(
                        result.sumScore
                    ),
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                if (index < combinations.lastIndex) {
                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Button(
            onClick = onBacktest
        ) {
            Text(
                text = "🧪 ПРОВЕРИТЬ ГЕНЕРАТОР",
                fontSize = 16.sp
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            text = "Важно",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Score показывает соответствие комбинации " +
                    "исторической модели. Это не является " +
                    "прогнозом результата будущего тиража.",
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
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