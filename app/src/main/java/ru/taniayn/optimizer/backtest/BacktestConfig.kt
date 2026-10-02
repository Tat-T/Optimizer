package ru.taniayn.optimizer.backtest

object BacktestConfig {

    fun calculateTrainSize(drawCount: Int): Int {
        if (drawCount < 2) return 0

        return drawCount / 2
    }

    fun calculateTestSize(drawCount: Int): Int {
        if (drawCount < 2) return 0

        val trainSize = calculateTrainSize(drawCount)

        return drawCount - trainSize
    }
}