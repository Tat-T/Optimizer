package ru.taniayn.optimizer.backtest

import ru.taniayn.optimizer.model.RapidoDraw

data class GeneratorBacktestResult(
    val testedDraws: Int,
    val testedCombinations: Int,

    val bestGeneratorCombination: List<Int>,
    val bestGeneratorActualDraw: List<Int>,
    val bestGeneratorOverlap: Int,

    val bestRandomCombination: List<Int>,
    val bestRandomActualDraw: List<Int>,
    val bestRandomOverlap: Int,

    // Генератор — среднее по 5 комбинациям
    val averageOverlap: Double,
    val matches4OrMore: Int,
    val matches5OrMore: Int,
    // Дополнительное число
    val additionalHits: Int,

    // RANDOM — среднее по 5 комбинациям
    val randomAverageOverlap: Double,
    val randomMatches4OrMore: Int,
    val randomMatches5OrMore: Int,

    // Лучший результат из 5
    val bestRecommendationAverage: Double,
    val bestRecommendation4OrMore: Int,
    val bestRecommendation5OrMore: Int,

    // Лучший результат из 5 RANDOM
    val bestRandomAverage: Double,
    val bestRandom4OrMore: Int,
    val bestRandom5OrMore: Int,
    val diagnostics: List<GeneratorDiagnostic>
)

data class GeneratorDiagnostic(
    val score: Double,
    val overlap: Int
)

object GeneratorBacktester {

    fun run(
        draws: List<RapidoDraw>,
        trainSize: Int,
        window: Int = 200,
        recommendationsCount: Int = 5,
        candidatesCount: Int = 300
    ): GeneratorBacktestResult {

        if (draws.size <= trainSize) {
            return emptyResult()
        }

        var totalGeneratorOverlap = 0
        var generatorMatches4OrMore = 0
        var generatorMatches5OrMore = 0

        var additionalHits = 0

        var overallBestGeneratorOverlap = -1
        var overallBestGeneratorCombination = emptyList<Int>()
        var overallBestGeneratorActualDraw = emptyList<Int>()

        var overallBestRandomOverlap = -1
        var overallBestRandomCombination = emptyList<Int>()
        var overallBestRandomActualDraw = emptyList<Int>()

        var totalRandomOverlap = 0
        var randomMatches4OrMore = 0
        var randomMatches5OrMore = 0

        var totalBestGeneratorOverlap = 0
        var bestGenerator4OrMore = 0
        var bestGenerator5OrMore = 0

        var totalBestRandomOverlap = 0
        var bestRandom4OrMore = 0
        var bestRandom5OrMore = 0

        var testedDraws = 0
        val diagnostics = mutableListOf<GeneratorDiagnostic>()

        for (i in trainSize until draws.size) {

            val history = draws.subList(0, i)

            // -----------------------------
            // ГЕНЕРАТОР
            // -----------------------------

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
                    minDifference = 2
                )

            val actualNumbers =
                draws[i].numbers.toSet()

// Дополнительное число используется отдельно от 8 основных

            val actualAdditionalNumber =
                draws[i].additionalNumber

            var bestGeneratorOverlap = 0

            recommendations.forEach { recommendation ->

                if (recommendation.additionalNumber == actualAdditionalNumber) {
                    additionalHits++
                }
                val overlap =
                    recommendation.combination
                        .toSet()
                        .intersect(actualNumbers)
                        .size

                diagnostics.add(
                    GeneratorDiagnostic(
                        score = recommendation.score,
                        overlap = overlap
                    )
                )

                totalGeneratorOverlap += overlap

                if (overlap >= 4) {
                    generatorMatches4OrMore++
                }

                if (overlap >= 5) {
                    generatorMatches5OrMore++
                }

                if (overlap > bestGeneratorOverlap) {
                    bestGeneratorOverlap = overlap
                }
            }

            totalBestGeneratorOverlap +=
                bestGeneratorOverlap

            if (bestGeneratorOverlap >= 4) {
                bestGenerator4OrMore++
            }

            if (bestGeneratorOverlap >= 5) {
                bestGenerator5OrMore++
            }

            if (bestGeneratorOverlap > overallBestGeneratorOverlap) {

                overallBestGeneratorOverlap = bestGeneratorOverlap

                val bestRecommendation =
                    recommendations.firstOrNull { recommendation ->

                        recommendation.combination
                            .toSet()
                            .intersect(actualNumbers)
                            .size == bestGeneratorOverlap
                    }

                if (bestRecommendation != null) {

                    overallBestGeneratorCombination =
                        bestRecommendation.combination

                    overallBestGeneratorActualDraw =
                        draws[i].numbers.toList()
                }
            }
            // -----------------------------
            // RANDOM — 5 КОМБИНАЦИЙ
            // -----------------------------

            var randomOverlapForDraw = 0
            var bestRandomOverlap = 0

            repeat(recommendationsCount) {

                val randomNumbers =
                    RandomStrategy
                        .generateForDraw(
                            i * recommendationsCount + it
                        )
                        .toSet()

                val overlap =
                    randomNumbers
                        .intersect(actualNumbers)
                        .size

                randomOverlapForDraw += overlap

                if (overlap > bestRandomOverlap) {

                    bestRandomOverlap = overlap

                    if (overlap > overallBestRandomOverlap) {
                        overallBestRandomOverlap = overlap
                        overallBestRandomCombination = randomNumbers.toList().sorted()
                        overallBestRandomActualDraw = draws[i].numbers.toList()
                    }
                }

                if (overlap >= 4) {
                    randomMatches4OrMore++
                }

                if (overlap >= 5) {
                    randomMatches5OrMore++
                }
            }

            totalRandomOverlap +=
                randomOverlapForDraw

            totalBestRandomOverlap +=
                bestRandomOverlap

            if (bestRandomOverlap >= 4) {
                bestRandom4OrMore++
            }

            if (bestRandomOverlap >= 5) {
                bestRandom5OrMore++
            }

            testedDraws++
        }

        val testedCombinations =
            testedDraws * recommendationsCount

        val averageOverlap =
            if (testedCombinations > 0) {
                totalGeneratorOverlap.toDouble() /
                        testedCombinations
            } else {
                0.0
            }

        val randomAverageOverlap =
            if (testedCombinations > 0) {
                totalRandomOverlap.toDouble() /
                        testedCombinations
            } else {
                0.0
            }

        val bestRecommendationAverage =
            if (testedDraws > 0) {
                totalBestGeneratorOverlap.toDouble() /
                        testedDraws
            } else {
                0.0
            }

        val bestRandomAverage =
            if (testedDraws > 0) {
                totalBestRandomOverlap.toDouble() /
                        testedDraws
            } else {
                0.0
            }

        return GeneratorBacktestResult(

            testedDraws = testedDraws,
            testedCombinations = testedCombinations,
            bestGeneratorCombination =
                overallBestGeneratorCombination,

            bestGeneratorActualDraw =
                overallBestGeneratorActualDraw,

            bestGeneratorOverlap =
                overallBestGeneratorOverlap,

            bestRandomCombination =
                overallBestRandomCombination,

            bestRandomActualDraw =
                overallBestRandomActualDraw,

            bestRandomOverlap =
                overallBestRandomOverlap,

            averageOverlap = averageOverlap,
            matches4OrMore = generatorMatches4OrMore,
            matches5OrMore = generatorMatches5OrMore,

            additionalHits = additionalHits,

            randomAverageOverlap = randomAverageOverlap,
            randomMatches4OrMore = randomMatches4OrMore,
            randomMatches5OrMore = randomMatches5OrMore,

            bestRecommendationAverage =
                bestRecommendationAverage,

            bestRecommendation4OrMore =
                bestGenerator4OrMore,

            bestRecommendation5OrMore =
                bestGenerator5OrMore,

            bestRandomAverage =
                bestRandomAverage,

            bestRandom4OrMore =
                bestRandom4OrMore,

            bestRandom5OrMore =
                bestRandom5OrMore,
            diagnostics = diagnostics
        )
    }

    private fun emptyResult(): GeneratorBacktestResult =
        GeneratorBacktestResult(
            testedDraws = 0,
            testedCombinations = 0,

            bestGeneratorCombination = emptyList(),
            bestGeneratorActualDraw = emptyList(),
            bestGeneratorOverlap = 0,

            bestRandomCombination = emptyList(),
            bestRandomActualDraw = emptyList(),
            bestRandomOverlap = 0,

            averageOverlap = 0.0,
            matches4OrMore = 0,
            matches5OrMore = 0,

            additionalHits = 0,

            randomAverageOverlap = 0.0,
            randomMatches4OrMore = 0,
            randomMatches5OrMore = 0,

            bestRecommendationAverage = 0.0,
            bestRecommendation4OrMore = 0,
            bestRecommendation5OrMore = 0,

            bestRandomAverage = 0.0,
            bestRandom4OrMore = 0,
            bestRandom5OrMore = 0,

            diagnostics = emptyList()
        )
}