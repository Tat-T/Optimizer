package ru.taniayn.optimizer.statistics

import ru.taniayn.optimizer.model.RapidoDraw

object StatisticsCalculator {

    /**
     * Частота выпадения основных чисел 1–20.
     */
    fun calculateMainNumberFrequency(
        draws: List<RapidoDraw>
    ): Map<Int, Int> {

        val frequency = (1..20)
            .associateWith { 0 }
            .toMutableMap()

        draws.forEach { draw ->

            draw.numbers.forEach { number ->

                if (number in 1..20) {
                    frequency[number] =
                        frequency.getValue(number) + 1
                }
            }
        }

        return frequency
    }

    /**
     * Частота выпадения дополнительных чисел 1–4.
     */
    fun calculateAdditionalNumberFrequency(
        draws: List<RapidoDraw>
    ): Map<Int, Int> {

        val frequency = (1..4)
            .associateWith { 0 }
            .toMutableMap()

        draws.forEach { draw ->

            val number = draw.additionalNumber

            if (number in 1..4) {
                frequency[number] =
                    frequency.getValue(number) + 1
            }
        }

        return frequency
    }

    /**
     * Возвращает числа от самых частых к самым редким.
     */
    fun sortByFrequency(
        frequency: Map<Int, Int>
    ): List<Pair<Int, Int>> {

        return frequency
            .toList()
            .sortedByDescending { it.second }
    }

    /**
     * Возвращает количество появлений конкретного числа.
     */
    fun getFrequency(
        frequency: Map<Int, Int>,
        number: Int
    ): Int {

        return frequency[number] ?: 0
    }
}