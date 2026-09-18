package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

object Strategies {

    /**
     * HOT 200
     *
     * Берём последние 200 тиражей
     * и выбираем 8 самых частых чисел.
     */
    fun hot200(
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
                compareByDescending<Pair<Int, Int>> { it.second }
                    .thenBy { it.first }
            )
            .take(8)
            .map { it.first }
    }


    /**
     * COLD 200
     *
     * Берём последние 200 тиражей
     * и выбираем 8 самых редких чисел.
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


    /**
     * HOT 50
     *
     * Берём последние 50 тиражей
     * и выбираем 8 самых частых чисел.
     */
    fun hot50(
        history: List<RapidoDraw>
    ): List<Int> {

        if (history.isEmpty()) {
            return emptyList()
        }

        val recentHistory =
            history.takeLast(50)

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
                compareByDescending<Pair<Int, Int>> { it.second }
                    .thenBy { it.first }
            )
            .take(8)
            .map { it.first }
    }


    /**
     * COLD 50
     *
     * Берём последние 50 тиражей
     * и выбираем 8 самых редких чисел.
     */
    fun cold50(
        history: List<RapidoDraw>
    ): List<Int> {

        if (history.isEmpty()) {
            return emptyList()
        }

        val recentHistory =
            history.takeLast(50)

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