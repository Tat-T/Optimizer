package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

data class CombinationScore(
    val combination: List<Int>,
    val score: Double,
    val frequencyScore: Double,
    val balanceScore: Double,
    val parityScore: Double,
    val sumScore: Double
)

object CombinationScorer {

    fun score(
        combination: List<Int>,
        history: List<RapidoDraw>,
        window: Int
    ): CombinationScore {

        if (combination.size != 8 || history.isEmpty()) {
            return CombinationScore(
                combination = combination,
                score = 0.0,
                frequencyScore = 0.0,
                balanceScore = 0.0,
                parityScore = 0.0,
                sumScore = 0.0
            )
        }

        val actualWindow = minOf(window, history.size)

        val recentHistory =
            history.takeLast(actualWindow)

        // -------------------------------------------------
        // 1. Частота чисел
        // -------------------------------------------------

        val frequency =
            (1..20)
                .associateWith { 0 }
                .toMutableMap()

        recentHistory.forEach { draw ->
            draw.numbers.forEach { number ->
                if (number in 1..20) {
                    frequency[number] =
                        frequency.getValue(number) + 1
                }
            }
        }

        val maxFrequency =
            frequency.values.maxOrNull()?.toDouble() ?: 1.0

        val averageFrequency =
            frequency.values.average()

        val selectedFrequency =
            combination.sumOf {
                frequency[it] ?: 0
            }.toDouble() / combination.size

        /*
         * Насколько средняя частота выбранных чисел
         * отличается от средней частоты всех чисел.
         *
         * Значение около 1.0 — близко к историческому
         * среднему.
         */

        val frequencyScore =
            if (averageFrequency > 0.0) {
                (
                        selectedFrequency /
                                averageFrequency
                        ).coerceIn(0.0, 2.0) / 2.0
            } else {
                0.5
            }

        // -------------------------------------------------
        // 2. Баланс HOT / COLD
        // -------------------------------------------------

        val sortedByFrequency =
            frequency.entries
                .sortedByDescending { it.value }

        val hotNumbers =
            sortedByFrequency
                .take(8)
                .map { it.key }
                .toSet()

        val coldNumbers =
            sortedByFrequency
                .takeLast(8)
                .map { it.key }
                .toSet()

        val hotCount =
            combination.count { it in hotNumbers }

        val coldCount =
            combination.count { it in coldNumbers }

        /*
         * Для первой версии считаем хорошим умеренный
         * баланс между HOT и COLD.
         *
         * 4 HOT + 4 COLD получает максимальную оценку.
         */

        val balanceScore =
            1.0 -
                    kotlin.math.abs(
                        hotCount - coldCount
                    ) / 8.0

        // -------------------------------------------------
        // 3. Баланс чётных / нечётных
        // -------------------------------------------------

        val evenCount =
            combination.count { it % 2 == 0 }

        val oddCount =
            combination.size - evenCount

        /*
         * Для 8 чисел идеальный центр:
         * 4 чётных + 4 нечётных.
         */

        val parityScore =
            1.0 -
                    kotlin.math.abs(
                        evenCount - oddCount
                    ) / 8.0

        // -------------------------------------------------
        // 4. Сумма комбинации
        // -------------------------------------------------

        val combinationSum =
            combination.sum()

        /*
         * Для 8 чисел из диапазона 1..20
         * ожидаемая центральная сумма:
         *
         * 8 × 10.5 = 84
         */

        val expectedSum = 84.0

        val sumDifference =
            kotlin.math.abs(
                combinationSum - expectedSum
            )

        /*
         * Чем ближе сумма к центральной,
         * тем выше оценка.
         *
         * 84 -> 1.0
         * сильное отклонение -> ниже.
         */

        val sumScore =
            (
                    1.0 -
                            sumDifference / expectedSum
                    ).coerceIn(0.0, 1.0)

        // -------------------------------------------------
        // Итоговый Score
        // -------------------------------------------------

        val score =
            frequencyScore * 0.40 +
                    balanceScore * 0.25 +
                    parityScore * 0.15 +
                    sumScore * 0.20

        return CombinationScore(
            combination = combination.sorted(),
            score = score,
            frequencyScore = frequencyScore,
            balanceScore = balanceScore,
            parityScore = parityScore,
            sumScore = sumScore
        )
    }
}