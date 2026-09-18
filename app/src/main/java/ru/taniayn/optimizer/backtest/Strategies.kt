package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

object Strategies {

    /**
     * COLD 200
     *
     * Берём последние 200 доступных тиражей
     * и выбираем 8 чисел с наименьшей частотой.
     */
    fun cold200(
        history: List<RapidoDraw>
    ): List<Int> {

        if (history.isEmpty()) {
            return emptyList()
        }

        val recentHistory =
            history.takeLast(200)

        val frequency =
            (1..20)
                .associateWith { 0 }
                .toMutableMap()

        recentHistory.forEach { draw ->

            draw.numbers.forEach { number ->

                if (number in 1..20) {
                    frequency[number] =
                        frequency.getValue(number) + 1
                }
            }
        }

        return frequency
            .toList()
            .sortedWith(
                compareBy<Pair<Int, Int>> { it.second }
                    .thenBy { it.first }
            )
            .take(8)
            .map { it.first }
    }
}