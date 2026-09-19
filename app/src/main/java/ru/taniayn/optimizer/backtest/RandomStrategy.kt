package ru.taniayn.optimizer.backtest

import kotlin.random.Random

object RandomStrategy {

    private val random = Random(12345)

    /**
     * RANDOM
     *
     * Каждый раз выбираем случайные 8 чисел
     * из диапазона 1..20.
     *
     * Фиксированный seed делает результаты
     * воспроизводимыми между запусками.
     */
    fun generate(): List<Int> {
        return (1..20)
            .shuffled(random)
            .take(8)
            .sorted()
    }
}