package ru.taniayn.optimizer.backtest

import kotlin.random.Random

object RandomStrategy {

    private val random = Random(12345)

    /**
     * Обычный RANDOM.
     *
     * Используется для отдельного backtest
     * случайных комбинаций.
     */
    fun generate(): List<Int> {
        return (1..20)
            .shuffled(random)
            .take(8)
            .sorted()
    }

    /**
     * Воспроизводимый RANDOM для конкретного
     * тестового тиража.
     *
     * Один и тот же drawIndex всегда получает
     * один и тот же случайный билет.
     *
     * Поэтому все стратегии сравниваются
     * с одинаковым RANDOM-билетом.
     */
    fun generateForDraw(drawIndex: Int): List<Int> {

        val randomForDraw =
            Random(12345 + drawIndex)

        return (1..20)
            .shuffled(randomForDraw)
            .take(8)
            .sorted()
    }
}