package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw
import kotlin.random.Random

object CombinationGenerator {

    fun generateCandidates(
        history: List<RapidoDraw>,
        window: Int,
        count: Int = 1000,
        seed: Int = 12345
    ): List<CombinationScore> {

        if (history.isEmpty() || count <= 0) {
            return emptyList()
        }

        val random = Random(seed)

        val combinations = mutableSetOf<List<Int>>()

        while (combinations.size < count) {

            val combination =
                (1..20)
                    .shuffled(random)
                    .take(8)
                    .sorted()

            combinations.add(combination)
        }

        return combinations
            .map { combination ->
                CombinationScorer.score(
                    combination = combination,
                    history = history,
                    window = window
                )
            }
            .sortedByDescending { it.score }
    }
}