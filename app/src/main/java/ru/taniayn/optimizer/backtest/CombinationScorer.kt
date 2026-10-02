package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw
import kotlin.math.abs

data class CombinationScore(
    val combination: List<Int>,
    val additionalNumber: Int,
    val score: Double,
    val frequencyScore: Double,
    val balanceScore: Double,
    val parityScore: Double,
    val sumScore: Double,
    val additionalScore: Double
)

object CombinationScorer {

    fun score(
        combination: List<Int>,
        history: List<RapidoDraw>,
        window: Int,
        additionalNumber: Int
    ): CombinationScore {

        if (
            combination.size != 8 ||
            combination.toSet().size != 8 ||
            history.isEmpty()
        ) {
            return emptyScore(combination,
                additionalNumber)
        }

        val actualWindow =
            minOf(window, history.size)

        val recentHistory =
            history.takeLast(actualWindow)

        // -------------------------------------------------
        // 1. Частотная модель
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
            frequency.values
                .maxOrNull()
                ?.toDouble()
                ?: 1.0

        val selectedFrequency =
            combination
                .sumOf { frequency[it] ?: 0 }
                .toDouble() / combination.size

        /*
         * Средняя частота выбранных чисел
         * относительно самого часто выпадавшего числа.
         *
         * Это показывает, насколько выбранные числа
         * соответствуют исторической частотной модели.
         */

        val frequencyScore =
            if (maxFrequency > 0.0) {
                (selectedFrequency / maxFrequency)
                    .coerceIn(0.0, 1.0)
            } else {
                0.0
            }

        // -------------------------------------------------
// Дополнительное число
// -------------------------------------------------

        val additionalFrequency =
            (1..4)
                .associateWith { 0 }
                .toMutableMap()

        recentHistory.forEach { draw ->

            val number = draw.additionalNumber

            if (number in 1..4) {
                additionalFrequency[number] =
                    additionalFrequency.getValue(number) + 1
            }
        }

        val maxAdditionalFrequency =
            additionalFrequency.values
                .maxOrNull()
                ?.toDouble()
                ?: 1.0

        val selectedAdditionalFrequency =
            additionalFrequency[additionalNumber]
                ?.toDouble()
                ?: 0.0

        val additionalScore =
            if (maxAdditionalFrequency > 0) {

                (selectedAdditionalFrequency /
                        maxAdditionalFrequency)
                    .coerceIn(0.0, 1.0)

            } else {
                0.0
            }

        // -------------------------------------------------
        // 2. Историческая HOT / COLD модель
        // -------------------------------------------------

        val sortedByFrequency =
            frequency.entries
                .sortedWith(
                    compareByDescending<Map.Entry<Int, Int>> {
                        it.value
                    }.thenBy {
                        it.key
                    }
                )

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

        /*
         * Для каждого исторического тиража считаем,
         * сколько HOT-чисел в нём оказалось.
         *
         * Затем смотрим, насколько количество HOT
         * в нашей комбинации соответствует реальной истории.
         */

        val historicalHotCounts =
            recentHistory.map { draw ->
                draw.numbers.count {
                    it in hotNumbers
                }
            }

        val candidateHotCount =
            combination.count {
                it in hotNumbers
            }

        val balanceScore =
            empiricalProbabilityScore(
                value = candidateHotCount,
                historicalValues = historicalHotCounts,
                minValue = 0,
                maxValue = 8
            )

        // -------------------------------------------------
        // 3. Историческая модель чётных / нечётных
        // -------------------------------------------------

        val historicalEvenCounts =
            recentHistory.map { draw ->
                draw.numbers.count {
                    it % 2 == 0
                }
            }

        val candidateEvenCount =
            combination.count {
                it % 2 == 0
            }

        val parityScore =
            empiricalProbabilityScore(
                value = candidateEvenCount,
                historicalValues = historicalEvenCounts,
                minValue = 0,
                maxValue = 8
            )

        // -------------------------------------------------
        // 4. Историческая модель суммы
        // -------------------------------------------------

        val historicalSums =
            recentHistory.map { draw ->
                draw.numbers.sum()
            }

        val candidateSum =
            combination.sum()

        val minSum =
            historicalSums.minOrNull() ?: 0

        val maxSum =
            historicalSums.maxOrNull() ?: 0

        val sumScore =
            empiricalProbabilityScore(
                value = candidateSum,
                historicalValues = historicalSums,
                minValue = minSum,
                maxValue = maxSum
            )

        // -------------------------------------------------
        // 5. Итоговая оценка
        // -------------------------------------------------

        val score =
            frequencyScore * 0.35 +
                    balanceScore * 0.25 +
                    parityScore * 0.15 +
                    sumScore * 0.15 +
                    additionalScore * 0.10

        return CombinationScore(
            combination = combination.sorted(),
            additionalNumber = additionalNumber,
            score = score,
            frequencyScore = frequencyScore,
            balanceScore = balanceScore,
            parityScore = parityScore,
            sumScore = sumScore,
            additionalScore = additionalScore
        )
    }



    /**
     * Оценивает, насколько значение соответствует
     * распределению, реально наблюдавшемуся в истории.
     *
     * Используется сглаживание +1, чтобы значение,
     * которое ещё не встречалось, не получало абсолютный ноль.
     */
    private fun empiricalProbabilityScore(
        value: Int,
        historicalValues: List<Int>,
        minValue: Int,
        maxValue: Int
    ): Double {

        if (historicalValues.isEmpty()) {
            return 0.0
        }

        if (value < minValue || value > maxValue) {
            return 0.0
        }

        val possibleValues =
            (minValue..maxValue).toList()

        val counts =
            possibleValues.associateWith { candidate ->
                historicalValues.count {
                    it == candidate
                }
            }

        val smoothedCounts =
            counts.mapValues {
                it.value + 1
            }

        val maxCount =
            smoothedCounts.values
                .maxOrNull()
                ?.toDouble()
                ?: 1.0

        val candidateCount =
            smoothedCounts[value]
                ?.toDouble()
                ?: 1.0

        return (
                candidateCount / maxCount
                ).coerceIn(0.0, 1.0)
    }

    private fun emptyScore(
        combination: List<Int>,
        additionalNumber: Int = 0
    ): CombinationScore {

        return CombinationScore(
            combination = combination.sorted(),
            additionalNumber = additionalNumber,
            score = 0.0,
            frequencyScore = 0.0,
            balanceScore = 0.0,
            parityScore = 0.0,
            sumScore = 0.0,
            additionalScore = 0.0
        )
    }
}