package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

data class NumberTrend(
    val number: Int,
    val totalHits: Int,
    val hitsLast10: Int,
    val hitsLast20: Int,
    val hitsLast50: Int,
    val currentMissStreak: Int,
    val currentHitStreak: Int,
    val averageGap: Double,
    val lastGap: Int,
    val longTermRate: Double,
    val shortTermRate: Double,
    val trend: Double,
    val gapRatio: Double
)

object NumberTrendCalculator {

    fun calculate(
        history: List<RapidoDraw>
    ): List<NumberTrend> {

        if (history.isEmpty()) return emptyList()

        // Предполагаем, что drawNumber растёт со временем.
        val draws = history.sortedBy { it.drawNumber }

        return (1..20).map { number ->

            val hitIndices = draws.mapIndexedNotNull { index, draw ->
                if (number in draw.numbers) index else null
            }

            val totalHits = hitIndices.size

            fun hitsInLast(count: Int): Int =
                draws.takeLast(count).count { number in it.numbers }

            val longTermRate =
                totalHits.toDouble() / draws.size

            val shortTermRate =
                hitsInLast(20).toDouble() / minOf(20, draws.size)


            val trend =
                shortTermRate - longTermRate

            var currentHitStreak = 0
            for (draw in draws.asReversed()) {
                if (number in draw.numbers) currentHitStreak++
                else break
            }

            var currentMissStreak = 0
            for (draw in draws.asReversed()) {
                if (number !in draw.numbers) currentMissStreak++
                else break
            }

            val gaps = hitIndices.zipWithNext { previous, current ->
                current - previous
            }

            val averageGap =
                if (gaps.isNotEmpty()) gaps.average()
                else 0.0

            val lastGap =
                if (hitIndices.isNotEmpty()) {
                    draws.lastIndex - hitIndices.last()
                } else {
                    draws.size
                }

            val gapRatio =
                if (averageGap > 0.0) {
                    lastGap / averageGap
                } else {
                    0.0
                }

            NumberTrend(
                number = number,
                totalHits = totalHits,
                hitsLast10 = hitsInLast(10),
                hitsLast20 = hitsInLast(20),
                hitsLast50 = hitsInLast(50),
                currentMissStreak = currentMissStreak,
                currentHitStreak = currentHitStreak,
                averageGap = averageGap,
                lastGap = lastGap,
                longTermRate = longTermRate,
                shortTermRate = shortTermRate,
                trend = trend,
                gapRatio = gapRatio
            )
        }
    }
}