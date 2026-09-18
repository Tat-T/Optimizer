package ru.taniayn.optimizer.backtest

data class BacktestResult(
    val strategyName: String,
    val testedDraws: Int,
    val averageOverlap: Double,
    val matches4OrMore: Int,
    val matches5OrMore: Int,
    val randomBaseline: Double = 3.2
)