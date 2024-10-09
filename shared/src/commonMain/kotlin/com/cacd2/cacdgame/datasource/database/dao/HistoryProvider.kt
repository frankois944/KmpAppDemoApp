package com.cacd2.cacdgame.datasource.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.apollographql.apollo3.mpp.currentTimeMillis
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.HistoryData
import com.cacd2.cacdgame.model.HistoryFilterCriteria
import com.cacd2.cacdgame.model.Language
import com.cacd2.cacdgame.model.QuestionChoice
import com.cacd2.cacdgame.tools.logger.AppLogger
import com.cacd2.cacdgame.tools.logger.CommonFlow
import com.cacd2.cacdgame.tools.logger.asCommonFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Created by francois.dabonot@cacd2.fr on 24/05/2023.
 */
suspend fun Database.saveHistory(category: GameChoice, questions: List<QuestionChoice>): Long {
    return withContext(Dispatchers.Default) {
        AppLogger.i("saveHistory")
        var historyId = -1L
        try {
            choiceHistoryQueries.transaction {
                AppLogger.i("choiceHistoryQueries.insert ${questions[0].game.gameId}")
                choiceHistoryQueries.insert(
                    categoryId = category.gameId,
                    timestamp = currentTimeMillis(),
                    lang = questions.first().lang.code
                )
                historyId = choiceHistoryQueries.selectLastInsertedRowId().executeAsOne()
                choiceHistoryLineQueries.transaction {
                    questions.forEach { question ->
                        AppLogger.i("choiceHistoryLineQueries.insert ${question.id}")
                        choiceHistoryLineQueries.insert(
                            choiceHistoryId = historyId,
                            choiceId = question.id,
                            selectedChoiceId = question.userSelectedAnswer?.id ?: "-1",
                            userHasOk =
                            if (question.userSelectedAnswer?.isCorrect == true) {
                                1
                            } else {
                                0
                            },
                            responseTime = question.responseTime
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            historyId = -1
            AppLogger.e("Cant save history gameId: ${questions.firstOrNull()?.game?.gameId}", ex)
        } finally {
            if (historyId == -1L) {
                AppLogger.e("historyId is -1: no data saved")
            }
        }
        historyId
    }
}

suspend fun Database.getAllHistory(criteria: HistoryFilterCriteria?): List<HistoryData> {
    return withContext(Dispatchers.Default) {
        val result = mutableListOf<HistoryData>()

        val currentCriteria = criteria?.buildCategoryIdList()

        val allHistory =
            if (!currentCriteria.isNullOrEmpty()) {
                choiceHistoryQueries
                    .selectWithCategoriesByDesc(currentCriteria)
                    .executeAsList()
            } else {
                choiceHistoryQueries.selectAllByDesc().executeAsList()
            }

        allHistory.forEach { historyLine ->
            buildHistory(historyLine.id)?.let { element ->
                result.add(element)
            }
        }
        result
    }
}

fun Database.getAllHistoryWithFlow(
    criteria: HistoryFilterCriteria?
): CommonFlow<List<HistoryData>> {
    val currentCriteria = criteria?.buildCategoryIdList()

    return if (!currentCriteria.isNullOrEmpty()) {
        choiceHistoryQueries
            .selectWithCategoriesByDesc(currentCriteria)
    } else {
        choiceHistoryQueries.selectAllByDesc()
    }
        .asFlow()
        .mapToList(Dispatchers.Default)
        .map { list ->
            val result = mutableListOf<HistoryData>()
            list.forEach { historyLine ->
                buildHistory(historyLine.id)?.let { element ->
                    result.add(element)
                }
            }
            result
        }
        .asCommonFlow()
}

suspend fun Database.getHistoryDoneCategory(): List<GameChoice> {
    return withContext(Dispatchers.Default) {
        val list = choiceHistoryQueries.selectCategoryDone().executeAsList()
        return@withContext list.map { GameChoice.fromId(it) }
    }
}

suspend fun Database.getHistory(historyId: Long): HistoryData? {
    return withContext(Dispatchers.Default) {
        return@withContext buildHistory(historyId)
    }
}

suspend fun Database.getHistoryCount(): Long {
    return withContext(Dispatchers.Default) {
        return@withContext choiceHistoryQueries.count().executeAsOne()
    }
}

private suspend fun Database.buildHistory(historyId: Long): HistoryData? {
    return withContext(Dispatchers.Default) {
        try {
            val choiceHistory = choiceHistoryQueries.selectById(historyId).executeAsOne()
            val choiceHistoryLines = choiceHistoryLineQueries.byHistoryId(historyId).executeAsList()
            val originalQuestions =
                GameChoice.getQuestions(
                    ids =
                    choiceHistoryLines.map {
                        it.choiceId
                    }.toTypedArray(),
                    lang = Language.fromCode(choiceHistory.lang)
                )

            originalQuestions.forEachIndexed { index, originalQuestion ->
                originalQuestion.userSelectedAnswer =
                    originalQuestion.answers.firstOrNull { answer ->
                        answer.id == choiceHistoryLines[index].selectedChoiceId
                    }
                originalQuestion.responseTime = choiceHistoryLines[index].responseTime
            }
            return@withContext HistoryData(
                data = originalQuestions,
                id = historyId,
                timestamp = choiceHistory.timestamp,
                category = GameChoice.fromId(choiceHistory.categoryId)
            )
        } catch (ex: Exception) {
            AppLogger.e("Cant get history gameId: $historyId", ex)
        }
        null
    }
}
