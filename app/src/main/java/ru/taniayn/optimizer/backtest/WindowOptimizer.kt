package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

object WindowOptimizer {

    /**
     * Размеры исторического окна,
     * которые будем автоматически проверять.
     */
    val windows = listOf(
        20,
        30,
        40,
        50,
        60,
        80,
        100,
        120,
        150,
        200
    )

    /**
     * Проверяет одну HOT-стратегию
     * с заданным размером окна.
     */
    fun testHotWindow(
        draws: List<RapidoDraw>,
        trainSize: Int,
        window: Int
    ): BacktestResult {

        val result = Backtester.run(
            draws = draws,
            trainSize = trainSize,
            strategy = { history ->
                Strategies.hot(
                    history = history,
                    window = window
                )
            },
            strategyName = "HOT $window"
        )

        val statisticalResult =
            StatisticalTest.compareWithRandom(
                draws = draws,
                trainSize = trainSize,
                strategy = { history ->
                    Strategies.hot(
                        history = history,
                        window = window
                    )
                }
            )

        return result.copy(
            differenceAgainstRandom =
                statisticalResult.averageDifference,

            zScore =
                statisticalResult.zScore,

            pValue =
                statisticalResult.pValue
        )
    }

    /**
     * Проверяет одну COLD-стратегию
     * с заданным размером окна.
     */
    fun testColdWindow(
        draws: List<RapidoDraw>,
        trainSize: Int,
        window: Int
    ): BacktestResult {

        val result = Backtester.run(
            draws = draws,
            trainSize = trainSize,
            strategy = { history ->
                Strategies.cold(
                    history = history,
                    window = window
                )
            },
            strategyName = "COLD $window"
        )

        val statisticalResult =
            StatisticalTest.compareWithRandom(
                draws = draws,
                trainSize = trainSize,
                strategy = { history ->
                    Strategies.cold(
                        history = history,
                        window = window
                    )
                }
            )

        return result.copy(
            differenceAgainstRandom =
                statisticalResult.averageDifference,

            zScore =
                statisticalResult.zScore,

            pValue =
                statisticalResult.pValue
        )
    }

    fun testAllHotWindows(
        draws: List<RapidoDraw>,
        trainSize: Int
    ): List<BacktestResult> {

        return windows.map { window ->

            testHotWindow(
                draws = draws,
                trainSize = trainSize,
                window = window
            )
        }
    }

    fun testAllColdWindows(
        draws: List<RapidoDraw>,
        trainSize: Int
    ): List<BacktestResult> {

        return windows.map { window ->

            testColdWindow(
                draws = draws,
                trainSize = trainSize,
                window = window
            )
        }
    }
}