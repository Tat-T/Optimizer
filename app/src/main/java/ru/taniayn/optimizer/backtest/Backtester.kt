package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

object Backtester {

    fun run(
        draws: List<RapidoDraw>,
        trainSize: Int,
        strategy: (List<RapidoDraw>) -> List<Int>,
        strategyName: String
    ): BacktestResult {

        if (draws.size <= trainSize) {
            return BacktestResult(
                strategyName = strategyName,
                testedDraws = 0,
                averageOverlap = 0.0,
                matches4OrMore = 0,
                matches5OrMore = 0
            )
        }

        var totalOverlap = 0
        var matches4OrMore = 0
        var matches5OrMore = 0
        var testedDraws = 0

        for (i in trainSize until draws.size) {

            val history = draws.subList(0, i)

            val predictedNumbers =
                strategy(history)

            val actualNumbers =
                draws[i].numbers.toSet()

            val overlap =
                predictedNumbers
                    .toSet()
                    .intersect(actualNumbers)
                    .size

            totalOverlap += overlap

            if (overlap >= 4) {
                matches4OrMore++
            }

            if (overlap >= 5) {
                matches5OrMore++
            }

            testedDraws++
        }

        val averageOverlap =
            if (testedDraws > 0) {
                totalOverlap.toDouble() / testedDraws
            } else {
                0.0
            }

        return BacktestResult(
            strategyName = strategyName,
            testedDraws = testedDraws,
            averageOverlap = averageOverlap,
            matches4OrMore = matches4OrMore,
            matches5OrMore = matches5OrMore
        )
    }

    /**
     * RANDOM BACKTEST
     *
     * Для каждого тестового тиража
     * генерируем несколько случайных комбинаций
     * и усредняем результат.
     */
    fun runRandom(
        draws: List<RapidoDraw>,
        trainSize: Int
    ): BacktestResult {

        if (draws.size <= trainSize) {
            return BacktestResult(
                strategyName = "RANDOM",
                testedDraws = 0,
                averageOverlap = 0.0,
                matches4OrMore = 0,
                matches5OrMore = 0
            )
        }

        val randomTicketsPerDraw = 20

        var totalOverlap = 0
        var matches4OrMore = 0
        var matches5OrMore = 0
        var testedDraws = 0

        for (i in trainSize until draws.size) {

            val actualNumbers =
                draws[i].numbers.toSet()

            repeat(randomTicketsPerDraw) {

                val predictedNumbers =
                    RandomStrategy.generate()

                val overlap =
                    predictedNumbers
                        .toSet()
                        .intersect(actualNumbers)
                        .size

                totalOverlap += overlap

                if (overlap >= 4) matches4OrMore++
                if (overlap >= 5) matches5OrMore++
            }

            testedDraws++
        }

        val totalRandomTickets =
            testedDraws * randomTicketsPerDraw

        val averageOverlap =
            if (totalRandomTickets > 0) {
                totalOverlap.toDouble() / totalRandomTickets
            } else {
                0.0
            }

        return BacktestResult(
            strategyName = "RANDOM",
            testedDraws = totalRandomTickets,
            averageOverlap = averageOverlap,
            matches4OrMore = matches4OrMore,
            matches5OrMore = matches5OrMore
        )
    }
}