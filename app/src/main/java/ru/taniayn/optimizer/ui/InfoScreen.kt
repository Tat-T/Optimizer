package ru.taniayn.optimizer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "← НАЗАД",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ℹ️ ИНФОРМАЦИЯ",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 1. Что анализирует приложение

        Text(
            text = "1. Что анализирует приложение",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Оптимизатор анализирует историю тиражей Рапидо. " +
                    "Приложение подсчитывает частоту выпадения чисел, " +
                    "проверяет разные стратегии выбора чисел и сравнивает " +
                    "их результаты со случайным выбором.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. HOT и COLD

        Text(
            text = "2. HOT и COLD",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "HOT — числа, которые чаще встречались в выбранном " +
                    "историческом окне.\n\n" +
                    "COLD — числа, которые встречались реже в выбранном " +
                    "историческом окне.\n\n" +
                    "Размер окна показывает, сколько последних тиражей " +
                    "используется для расчёта частот.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. BACKTEST

        Text(
            text = "3. Что такое BACKTEST",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "BACKTEST — проверка стратегии на исторических данных. " +
                    "Для каждого проверяемого тиража стратегия использует " +
                    "только предыдущую историю, после чего её результат " +
                    "сравнивается с фактически выпавшими числами.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. RANDOM

        Text(
            text = "4. Что означает RANDOM",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "RANDOM — контрольный случайный выбор 8 чисел из 20. " +
                    "Он нужен как базовая точка сравнения: результаты " +
                    "стратегий можно сопоставить с тем, что получилось " +
                    "бы при случайном выборе.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Среднее

        Text(
            text = "5. Что означает Среднее",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Среднее — среднее количество совпавших чисел " +
                    "за все проверенные тиражи.\n\n" +
                    "Например, значение 3.288 означает, что в среднем " +
                    "стратегия совпала с фактическим тиражом примерно " +
                    "по 3.288 чисел на один проверенный тираж.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Δ

        Text(
            text = "6. Что такое Δ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Δ показывает разницу между результатом стратегии " +
                    "и соответствующим случайным выбором RANDOM.\n\n" +
                    "Положительное значение означает, что среднее " +
                    "количество совпадений стратегии было выше RANDOM " +
                    "на проверенном наборе данных. Отрицательное — ниже.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 7. 4+ и 5+

        Text(
            text = "7. Что означают 4+ и 5+",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "4+ — количество проверенных тиражей, в которых " +
                    "стратегия совпала минимум с 4 основными числами.\n\n" +
                    "5+ — количество проверенных тиражей, в которых " +
                    "совпало минимум 5 основных чисел.\n\n" +
                    "В скобках отображается процент таких тиражей " +
                    "от общего числа проверенных.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 8. Z

        Text(
            text = "8. Что такое Z",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Z — стандартизированная величина, показывающая, " +
                    "насколько средняя разница между стратегией и RANDOM " +
                    "велика относительно разброса этой разницы.\n\n" +
                    "Чем дальше Z от нуля, тем сильнее наблюдаемая разница " +
                    "относительно её вариативности. Само по себе большое " +
                    "или маленькое значение Z не является доказательством " +
                    "предсказуемости будущих тиражей.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 9. p-value

        Text(
            text = "9. Что такое p-value",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "p-value показывает, насколько наблюдаемая разница " +
                    "совместима со случайными колебаниями при используемой " +
                    "статистической проверке.\n\n" +
                    "Маленькое p-value означает, что наблюдаемая разница " +
                    "реже возникает в рамках проверяемой случайной модели. " +
                    "Это не означает вероятность выигрыша и не является " +
                    "вероятностью того, что стратегия будет работать " +
                    "в будущем.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 10. p adj. и Holm

        Text(
            text = "10. Что такое p adj. и поправка Holm",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "В оптимизации одновременно проверяется несколько " +
                    "размеров исторического окна. Если проводить много " +
                    "статистических проверок, возрастает вероятность " +
                    "случайно получить маленькое p-value.\n\n" +
                    "Поправка Holm учитывает множественные проверки. " +
                    "p adj. — скорректированное значение p после этой " +
                    "поправки.\n\n" +
                    "Поэтому p adj. следует использовать при оценке " +
                    "результатов оптимизации вместе с другими показателями.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 11. Почему статистическая значимость не даёт предсказания

        Text(
            text = "11. Почему статистически значимый результат " +
                    "не означает, что числа можно предсказать",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Даже статистически заметная разница на исторических " +
                    "данных не доказывает, что существует закономерность, " +
                    "которая сохранится в будущих тиражах.\n\n" +
                    "Исторические данные могут содержать случайные колебания, " +
                    "а результат стратегии может зависеть от конкретного " +
                    "периода проверки.\n\n" +
                    "Поэтому статистический тест помогает оценивать данные, " +
                    "но не превращает случайный процесс в гарантируемый " +
                    "способ предсказания.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 12. Как правильно интерпретировать результаты

        Text(
            text = "12. Как правильно интерпретировать результаты",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Результаты приложения следует рассматривать как " +
                    "статистический анализ исторических данных.\n\n" +
                    "При сравнении стратегий важно смотреть не на один " +
                    "показатель, а на совокупность результатов: среднее " +
                    "количество совпадений, 4+, 5+, разницу с RANDOM, " +
                    "p-value и p adj.\n\n" +
                    "Особенно важно учитывать размер выборки и то, что " +
                    "проверка проводится на исторических тиражах. " +
                    "Хороший результат backtest не является гарантией " +
                    "аналогичного результата в будущем.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Важно",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Приложение предназначено для анализа и исследования " +
                    "исторических данных. Результаты не гарантируют " +
                    "исход будущих тиражей.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "← НАЗАД",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}