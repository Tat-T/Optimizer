package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw
import kotlin.random.Random

object CombinationGenerator {

    fun generateCandidates(
        history: List<RapidoDraw>,
        window: Int,
        count: Int = 1000,
        seed: Int = System.nanoTime().toInt()
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
            .flatMap { combination ->
                (1..4).map { additionalNumber ->
                    CombinationScorer.score(
                        combination = combination,
                        history = history,
                        window = window,
                        additionalNumber = additionalNumber
                    )
                }
            }
            .sortedByDescending { it.score }
            .distinctBy { it.combination }
            .take(count)
    }
}