package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw
import kotlin.random.Random

object CombinationGenerator {

    fun generateCandidates(
        history: List<RapidoDraw>,
        window: Int,
        count: Int = 1000,
        seed: Int = System.nanoTime().toInt(),
        scoreModel: ScoreModel = ScoreModel.FULL,
        returnAllCandidates: Boolean = false
    ): List<CombinationScore> {

        if (history.isEmpty() || count <= 0) {
            return emptyList()
        }

        val random = Random(seed)

        val preparedStats = CombinationScorer.prepare(
            history = history,
            window = window
        )

        val combinations = mutableSetOf<List<Int>>()

        while (combinations.size < count) {

            val combination =
                (1..20)
                    .shuffled(random)
                    .take(8)
                    .sorted()

            combinations.add(combination)
        }

        val candidates = combinations
            .flatMap { combination ->
                (1..4).map { additionalNumber ->
                    CombinationScorer.score(
                        combination = combination,
                        stats = preparedStats,
                        additionalNumber = additionalNumber,
                        scoreModel = scoreModel
                    )
                }
            }
            .distinctBy { it.combination to it.additionalNumber }

        return if (returnAllCandidates) {
            candidates
        } else {
            candidates
                .sortedByDescending { it.score }
                .take(count)
        }
    }
}