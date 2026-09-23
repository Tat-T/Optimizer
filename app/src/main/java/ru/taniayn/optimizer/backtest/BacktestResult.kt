package ru.taniayn.optimizer.backtest

data class BacktestResult(
    val strategyName: String,
    val testedDraws: Int,
    val averageOverlap: Double,
    val matches4OrMore: Int,
    val matches5OrMore: Int,
    val differenceAgainstRandom: Double = 0.0,
    val zScore: Double = 0.0,
    val pValue: Double = 1.0,
    val adjustedPValue: Double = 1.0
)