package ru.taniayn.optimizer.data

import ru.taniayn.optimizer.model.RapidoDraw

object CsvParser {

    fun parse(csvText: String): List<RapidoDraw> {

        val lines = csvText
            .replace("\uFEFF", "")
            .lineSequence()
            .filter { it.isNotBlank() }
            .toList()

        if (lines.size < 2) {
            return emptyList()
        }

        val result = mutableListOf<RapidoDraw>()

        // Пропускаем заголовок.
        lines.drop(1).forEachIndexed { index, line ->

            val columns = parseCsvLine(line)

            // Нам нужны:
            // 0 — Тираж
            // 1 — Дата
            // 2 — Числа
            if (columns.size < 3) {
                return@forEachIndexed
            }

            val drawNumber = columns[0].trim().toIntOrNull()
                ?: return@forEachIndexed


            val parsedNumbers = columns[2]
                .split(",")
                .map { it.trim().toIntOrNull() }

// В Рапидо: 8 основных чисел и 1 дополнительное.
            if (parsedNumbers.size != 9 || parsedNumbers.any { it == null }) {
                return@forEachIndexed
            }

            val numbers = parsedNumbers.filterNotNull()
            val mainNumbers = numbers.take(8)
            val additionalNumber = numbers[8]

// Основные числа должны быть уникальными и находиться в диапазоне 1–20.
            if (
                mainNumbers.size != 8 ||
                mainNumbers.any { it !in 1..20 } ||
                mainNumbers.distinct().size != 8
            ) {
                return@forEachIndexed
            }

// Дополнительное число должно находиться в диапазоне 1–4.
            if (additionalNumber !in 1..4) {
                return@forEachIndexed
            }


            result.add(
                RapidoDraw(
                    drawNumber = drawNumber,
                    numbers = mainNumbers,
                    additionalNumber = additionalNumber
                )
            )
        }

        return result
            .distinctBy { it.drawNumber }
            .sortedBy { it.drawNumber }
    }

    /**
     * Разбирает одну строку CSV.
     *
     * Запятые внутри кавычек не считаются
     * разделителями столбцов.
     */
    private fun parseCsvLine(line: String): List<String> {

        val result = mutableListOf<String>()
        val current = StringBuilder()

        var insideQuotes = false

        for (char in line) {

            when {

                char == '"' -> {
                    insideQuotes = !insideQuotes
                }

                char == ',' && !insideQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }

                else -> {
                    current.append(char)
                }
            }
        }

        // Добавляем последний столбец.
        result.add(current.toString())

        return result
    }
}