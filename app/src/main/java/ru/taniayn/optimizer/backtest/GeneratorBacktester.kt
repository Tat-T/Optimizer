package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

data class GeneratorBacktestResult(
    val testedDraws: Int,
    val testedCombinations: Int,
    val averageOverlap: Double,
    val matches4OrMore: Int,
    val matches5OrMore: Int,
    val randomAverageOverlap: Double,
    val randomMatches4OrMore: Int,
    val randomMatches5OrMore: Int,
    val bestRecommendationAverage: Double,
    val bestRecommendation4OrMore: Int,
    val bestRecommendation5OrMore: Int
)

object GeneratorBacktester {

    fun run(
        draws: List<RapidoDraw>,
        trainSize: Int = 385,
        window: Int = 200,
        recommendationsCount: Int = 5,
        candidatesCount: Int = 300
    ): GeneratorBacktestResult {

        if (draws.size <= trainSize) {
            return emptyResult()
        }

        var totalOverlap = 0
        var matches4OrMore = 0
        var matches5OrMore = 0

        var randomTotalOverlap = 0
        var randomMatches4OrMore = 0
        var randomMatches5OrMore = 0

        var bestTotalOverlap = 0
        var bestMatches4OrMore = 0
        var bestMatches5OrMore = 0

        var testedDraws = 0

        for (i in trainSize until draws.size) {

            val history =
                draws.subList(0, i)

            // ---------------------------------------------
            // Генерируем рекомендации только из прошлого
            // ---------------------------------------------

            val candidates =
                CombinationGenerator.generateCandidates(
                    history = history,
                    window = window,
                    count = candidatesCount,
                    seed = 12345 + i
                )

            val recommendations =
                DiversifiedSelector.select(
                    candidates = candidates,
                    count = recommendationsCount,
                    minDifference = 3
                )

            val actualNumbers =
                draws[i].numbers.toSet()

            // ---------------------------------------------
            // Проверяем все рекомендации
            // ---------------------------------------------

            var bestOverlap = 0

            recommendations.forEach { recommendation ->

                val overlap =
                    recommendation.combination
                        .toSet()
                        .intersect(actualNumbers)
                        .size

                totalOverlap += overlap

                if (overlap >= 4) {
                    matches4OrMore++
                }

                if (overlap >= 5) {
                    matches5OrMore++
                }

                if (overlap > bestOverlap) {
                    bestOverlap = overlap
                }
            }

            // ---------------------------------------------
            // RANDOM
            // ---------------------------------------------

            val randomNumbers =
                RandomStrategy
                    .generateForDraw(i)
                    .toSet()

            val randomOverlap =
                randomNumbers
                    .intersect(actualNumbers)
                    .size

            randomTotalOverlap += randomOverlap

            if (randomOverlap >= 4) {
                randomMatches4OrMore++
            }

            if (randomOverlap >= 5) {
                randomMatches5OrMore++
            }

            // ---------------------------------------------
            // Лучший из пяти
            // ---------------------------------------------

            bestTotalOverlap += bestOverlap

            if (bestOverlap >= 4) {
                bestMatches4OrMore++
            }

            if (bestOverlap >= 5) {
                bestMatches5OrMore++
            }

            testedDraws++
        }

        val testedCombinations =
            testedDraws * recommendationsCount

        val averageOverlap =
            if (testedCombinations > 0) {
                totalOverlap.toDouble() /
                        testedCombinations
            } else {
                0.0
            }

        val randomAverageOverlap =
            if (testedDraws > 0) {
                randomTotalOverlap.toDouble() /
                        testedDraws
            } else {
                0.0
            }

        val bestRecommendationAverage =
            if (testedDraws > 0) {
                bestTotalOverlap.toDouble() /
                        testedDraws
            } else {
                0.0
            }

        return GeneratorBacktestResult(
            testedDraws = testedDraws,
            testedCombinations = testedCombinations,

            averageOverlap = averageOverlap,
            matches4OrMore = matches4OrMore,
            matches5OrMore = matches5OrMore,

            randomAverageOverlap = randomAverageOverlap,
            randomMatches4OrMore = randomMatches4OrMore,
            randomMatches5OrMore = randomMatches5OrMore,

            bestRecommendationAverage =
                bestRecommendationAverage,

            bestRecommendation4OrMore =
                bestMatches4OrMore,

            bestRecommendation5OrMore =
                bestMatches5OrMore
        )
    }

    private fun emptyResult(): GeneratorBacktestResult {

        return GeneratorBacktestResult(
            testedDraws = 0,
            testedCombinations = 0,

            averageOverlap = 0.0,
            matches4OrMore = 0,
            matches5OrMore = 0,

            randomAverageOverlap = 0.0,
            randomMatches4OrMore = 0,
            randomMatches5OrMore = 0,

            bestRecommendationAverage = 0.0,
            bestRecommendation4OrMore = 0,
            bestRecommendation5OrMore = 0
        )
    }
}