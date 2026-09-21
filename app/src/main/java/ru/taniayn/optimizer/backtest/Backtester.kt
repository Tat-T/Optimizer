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

        var totalOverlap = 0
        var matches4OrMore = 0
        var matches5OrMore = 0
        var testedDraws = 0

        for (i in trainSize until draws.size) {

            val actualNumbers =
                draws[i].numbers.toSet()

            /*
             * Используем тот же RANDOM,
             * который используется в
             * StatisticalTest.
             *
             * Для каждого тестового тиража
             * существует один фиксированный
             * RANDOM-билет.
             */
            val predictedNumbers =
                RandomStrategy
                    .generateForDraw(i)
                    .toSet()

            val overlap =
                predictedNumbers
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
                totalOverlap.toDouble() /
                        testedDraws
            } else {
                0.0
            }

        return BacktestResult(
            strategyName = "RANDOM",
            testedDraws = testedDraws,
            averageOverlap = averageOverlap,
            matches4OrMore = matches4OrMore,
            matches5OrMore = matches5OrMore
        )
    }

    fun runAgainstRandom(
        draws: List<RapidoDraw>,
        trainSize: Int,
        strategy: (List<RapidoDraw>) -> List<Int>,
        strategyName: String
    ): Double {

        if (draws.size <= trainSize) {
            return 0.0
        }

        var totalDifference = 0

        for (i in trainSize until draws.size) {

            val history = draws.subList(0, i)

            // Комбинация стратегии
            val strategyNumbers =
                strategy(history)
                    .toSet()

            // Одна случайная комбинация
            val randomNumbers =
                RandomStrategy.generate()
                    .toSet()

            // Фактический тираж
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

            totalDifference +=
                strategyOverlap - randomOverlap
        }

        return totalDifference.toDouble() /
                (draws.size - trainSize)
    }
}