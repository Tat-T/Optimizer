package ru.taniayn.optimizer.backtest

object DiversifiedSelector {

    fun select(
        candidates: List<CombinationScore>,
        count: Int = 5,
        minDifference: Int = 3
    ): List<CombinationScore> {

        if (candidates.isEmpty() || count <= 0) {
            return emptyList()
        }

        val selected = mutableListOf<CombinationScore>()

        // Сначала берём лучшую комбинацию по Score
        selected.add(candidates.first())

        for (candidate in candidates.drop(1)) {

            if (selected.size >= count) {
                break
            }

            val sufficientlyDifferent =
                selected.all { existing ->

                    val commonNumbers =
                        candidate.combination
                            .toSet()
                            .intersect(
                                existing.combination.toSet()
                            )
                            .size

                    val difference =
                        8 - commonNumbers

                    difference >= minDifference
                }

            if (sufficientlyDifferent) {
                selected.add(candidate)
            }
        }

        return selected
    }
}