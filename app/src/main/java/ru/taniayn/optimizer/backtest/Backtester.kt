package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

object Backtester {

    /**
     * Проверяет стратегию на исторических тиражах.
     *
     * Для каждого проверяемого тиража стратегия получает
     * только предыдущие тиражи.
     *
     * Текущий тираж в расчёт стратегии не попадает.
     */
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

            // ВАЖНО:
            // стратегия видит только тиражи ДО текущего.
            val history = draws.subList(0, i)

            val predictedNumbers = strategy(history)

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
}