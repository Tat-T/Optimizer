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
    val diagnostics: List<GeneratorDiagnostic>,

    val top5ScoreAverage: Double,
    val selectedScoreAverage: Double,
    val frequencyScoreAverage: Double,
    val balanceScoreAverage: Double,
    val parityScoreAverage: Double,
    val sumScoreAverage: Double,
    val pairScoreAverage: Double,

    val lowScoreAverageOverlap: Double,
    val score82to84AverageOverlap: Double,
    val score84to86AverageOverlap: Double,
    val score86to88AverageOverlap: Double,
    val score88PlusAverageOverlap: Double
)

data class GeneratorDiagnostic(
    val score: Double,
    val frequencyScore: Double,
    val balanceScore: Double,
    val parityScore: Double,
    val sumScore: Double,
    val pairScore: Double,
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

        var totalTop5Score = 0.0
        var totalSelectedScore = 0.0
        var totalFrequencyScore = 0.0
        var totalBalanceScore = 0.0
        var totalParityScore = 0.0
        var totalSumScore = 0.0
        var totalPairScore = 0.0

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
            val top5Candidates =
                candidates.take(recommendationsCount)

            totalTop5Score +=
                top5Candidates.sumOf { it.score }

            totalSelectedScore +=
                recommendations.sumOf { it.score }
            totalFrequencyScore +=
                recommendations.sumOf { it.frequencyScore }

            totalBalanceScore +=
                recommendations.sumOf { it.balanceScore }

            totalParityScore +=
                recommendations.sumOf { it.parityScore }

            totalSumScore +=
                recommendations.sumOf { it.sumScore }

            totalPairScore +=
                recommendations.sumOf { it.pairScore }

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
                        frequencyScore = recommendation.frequencyScore,
                        balanceScore = recommendation.balanceScore,
                        parityScore = recommendation.parityScore,
                        sumScore = recommendation.sumScore,
                        pairScore = recommendation.pairScore,
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
        val lowScoreDiagnostics =
            diagnostics.filter { it.score < 0.82 }

        val score82to84 =
            diagnostics.filter {
                it.score >= 0.82 && it.score < 0.84
            }

        val score84to86 =
            diagnostics.filter {
                it.score >= 0.84 && it.score < 0.86
            }

        val score86to88 =
            diagnostics.filter {
                it.score >= 0.86 && it.score < 0.88
            }

        val score88Plus =
            diagnostics.filter {
                it.score >= 0.88
            }

        val lowScoreAverageOverlap =
            if (lowScoreDiagnostics.isNotEmpty()) {
                lowScoreDiagnostics.map { it.overlap }.average()
            } else {
                0.0
            }

        val score82to84AverageOverlap =
            if (score82to84.isNotEmpty()) {
                score82to84.map { it.overlap }.average()
            } else {
                0.0
            }

        val score84to86AverageOverlap =
            if (score84to86.isNotEmpty()) {
                score84to86.map { it.overlap }.average()
            } else {
                0.0
            }

        val score86to88AverageOverlap =
            if (score86to88.isNotEmpty()) {
                score86to88.map { it.overlap }.average()
            } else {
                0.0
            }

        val score88PlusAverageOverlap =
            if (score88Plus.isNotEmpty()) {
                score88Plus.map { it.overlap }.average()
            } else {
                0.0
            }
        val top5ScoreAverage =
            if (testedDraws > 0) {
                totalTop5Score / testedDraws / recommendationsCount
            } else {
                0.0
            }

        val selectedScoreAverage =
            if (testedDraws > 0) {
                totalSelectedScore / testedDraws / recommendationsCount
            } else {
                0.0
            }

        val frequencyScoreAverage =
            if (testedDraws > 0) {
                totalFrequencyScore / testedDraws / recommendationsCount
            } else {
                0.0
            }

        val balanceScoreAverage =
            if (testedDraws > 0) {
                totalBalanceScore / testedDraws / recommendationsCount
            } else {
                0.0
            }

        val parityScoreAverage =
            if (testedDraws > 0) {
                totalParityScore / testedDraws / recommendationsCount
            } else {
                0.0
            }

        val sumScoreAverage =
            if (testedDraws > 0) {
                totalSumScore / testedDraws / recommendationsCount
            } else {
                0.0
            }

        val pairScoreAverage =
            if (testedDraws > 0) {
                totalPairScore / testedDraws / recommendationsCount
            } else {
                0.0
            }

        return GeneratorBacktestResult(

            top5ScoreAverage = top5ScoreAverage,
            selectedScoreAverage = selectedScoreAverage,
            frequencyScoreAverage = frequencyScoreAverage,
            balanceScoreAverage = balanceScoreAverage,
            parityScoreAverage = parityScoreAverage,
            sumScoreAverage = sumScoreAverage,
            pairScoreAverage = pairScoreAverage,

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
            diagnostics = diagnostics,

            lowScoreAverageOverlap = lowScoreAverageOverlap,
            score82to84AverageOverlap = score82to84AverageOverlap,
            score84to86AverageOverlap = score84to86AverageOverlap,
            score86to88AverageOverlap = score86to88AverageOverlap,
            score88PlusAverageOverlap = score88PlusAverageOverlap
        )
    }

    private fun emptyResult(): GeneratorBacktestResult =
        GeneratorBacktestResult(
            top5ScoreAverage = 0.0,
            selectedScoreAverage = 0.0,
            frequencyScoreAverage = 0.0,
            balanceScoreAverage = 0.0,
            parityScoreAverage = 0.0,
            sumScoreAverage = 0.0,
            pairScoreAverage = 0.0,

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

            diagnostics = emptyList(),

            lowScoreAverageOverlap = 0.0,
            score82to84AverageOverlap = 0.0,
            score84to86AverageOverlap = 0.0,
            score86to88AverageOverlap = 0.0,
            score88PlusAverageOverlap = 0.0
        )
}