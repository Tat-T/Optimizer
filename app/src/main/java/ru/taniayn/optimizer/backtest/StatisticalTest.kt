package ru.taniayn.optimizer.backtest

import kotlin.math.abs
import kotlin.math.sqrt
import ru.taniayn.optimizer.model.RapidoDraw

data class StatisticalResult(
    val averageDifference: Double,
    val standardDeviation: Double,
    val standardError: Double,
    val zScore: Double,
    val pValue: Double
)

object StatisticalTest {

    /**
     * Парное сравнение стратегии с RANDOM.
     *
     * Для каждого тестового тиража:
     * 1. рассчитываем результат стратегии;
     * 2. генерируем один RANDOM-билет;
     * 3. сравниваем количество совпадений;
     * 4. сохраняем разницу.
     *
     * Затем рассчитываем:
     * - среднюю разницу;
     * - стандартное отклонение;
     * - стандартную ошибку;
     * - z-score;
     * - двусторонний p-value.
     */
    fun compareWithRandom(
        draws: List<RapidoDraw>,
        trainSize: Int,
        strategy: (List<RapidoDraw>) -> List<Int>
    ): StatisticalResult {

        if (draws.size <= trainSize) {
            return StatisticalResult(
                averageDifference = 0.0,
                standardDeviation = 0.0,
                standardError = 0.0,
                zScore = 0.0,
                pValue = 1.0
            )
        }

        val differences = mutableListOf<Double>()

        /*
         * Для каждого тестового тиража создаём
         * один RANDOM-билет.
         *
         * Благодаря фиксированному seed в RandomStrategy
         * последовательность RANDOM воспроизводима.
         *
         * Одна и та же схема используется для каждой стратегии.
         */
        for (i in trainSize until draws.size) {

            val history = draws.subList(0, i)

            val strategyNumbers =
                strategy(history).toSet()

            val randomNumbers =
                RandomStrategy
                    .generateForDraw(i)
                    .toSet()

            val actualNumbers =
                draws[i].numbers.toSet()

            val strategyOverlap =
                strategyNumbers
                    .intersect(actualNumbers)
                    .size

            val randomOverlap =
                randomNumbers
                    .intersect(actualNumbers)
                    .size

            differences.add(
                (strategyOverlap - randomOverlap).toDouble()
            )
        }

        val n = differences.size

        if (n < 2) {
            return StatisticalResult(
                averageDifference =
                    differences.firstOrNull() ?: 0.0,
                standardDeviation = 0.0,
                standardError = 0.0,
                zScore = 0.0,
                pValue = 1.0
            )
        }

        val mean =
            differences.average()

        val variance =
            differences.sumOf {
                (it - mean) * (it - mean)
            } / (n - 1)

        val standardDeviation =
            sqrt(variance)

        val standardError =
            standardDeviation / sqrt(n.toDouble())

        val zScore =
            if (standardError > 0.0) {
                mean / standardError
            } else {
                0.0
            }

        val pValue =
            permutationPValue(differences)

        return StatisticalResult(
            averageDifference = mean,
            standardDeviation = standardDeviation,
            standardError = standardError,
            zScore = zScore,
            pValue = pValue
        )
    }

    private fun permutationPValue(
        differences: List<Double>
    ): Double {

        if (differences.isEmpty()) {
            return 1.0
        }

        /*
         * Точный перестановочный тест знаков.
         *
         * Для каждой разницы:
         *
         *   d
         *
         * рассматриваем два равновероятных варианта:
         *
         *   +d
         *   -d
         *
         * DP хранит вероятность каждой возможной
         * суммы после перестановок знаков.
         */

        val integerDifferences =
            differences.map { it.toInt() }

        val observedSum =
            integerDifferences.sum()

        val maxSum =
            integerDifferences.sumOf { abs(it) }

        if (maxSum == 0) {
            return 1.0
        }

        val size =
            maxSum * 2 + 1

        var probabilities =
            DoubleArray(size)

        val offset = maxSum

        probabilities[offset] = 1.0

        var currentMax = 0

        for (difference in integerDifferences) {

            val next =
                DoubleArray(size)

            for (sum in -currentMax..currentMax) {

                val probability =
                    probabilities[sum + offset]

                if (probability == 0.0) {
                    continue
                }

                next[
                    sum + difference + offset
                ] += probability * 0.5

                next[
                    sum - difference + offset
                ] += probability * 0.5
            }

            currentMax += abs(difference)

            probabilities = next
        }

        val observedAbs =
            abs(observedSum)

        var pValue = 0.0

        for (sum in -maxSum..maxSum) {

            if (abs(sum) >= observedAbs) {

                pValue +=
                    probabilities[sum + offset]
            }
        }

        return pValue.coerceIn(0.0, 1.0)
    }

    private fun twoSidedPValue(
        z: Double
    ): Double {

        val x = abs(z)

        // Приближение стандартного нормального распределения.
        val t = 1.0 / (1.0 + 0.2316419 * x)

        val d =
            0.3989422804014327 *
                    kotlin.math.exp(-x * x / 2.0)

        val probability =
            1.0 - d * (
                    0.319381530 * t
                            - 0.356563782 * t * t
                            + 1.781477937 * t * t * t
                            - 1.821255978 * t * t * t * t
                            + 1.330274429 * t * t * t * t * t
                    )

        return 2.0 * (1.0 - probability)
    }
}