package ru.taniayn.optimizer.backtest

import kotlin.random.Random

object RandomStrategy {

    /**
     * RANDOM
     *
     * Каждый раз выбираем случайные 8 чисел
     * из диапазона 1..20.
     */
    fun generate(): List<Int> {
        return (1..20)
            .shuffled(Random.Default)
            .take(8)
            .sorted()
    }
}