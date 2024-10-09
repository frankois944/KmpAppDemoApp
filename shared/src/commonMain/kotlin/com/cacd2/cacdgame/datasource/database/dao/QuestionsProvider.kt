package com.cacd2.cacdgame.datasource.database.dao

import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.database.Question
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.graphql.AllDataFromDateQuery
import com.cacd2.cacdgame.model.Difficulty
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.Language
import com.cacd2.cacdgame.model.QuestionChoice
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */

internal suspend fun Database.clearAllQuestions(language: Language? = null) {
    try {
        AppLogger.i("clearing question in DB for lang $language")
        Database.data.clearQuestions(language)
    } catch (ex: Exception) {
        AppLogger.i("No data in DB")
    }
}

internal suspend fun Database.saveQuestions(
    allCategories: List<AllDataFromDateQuery.AllCategory>,
    lang: String
): Boolean {
    return withContext(Dispatchers.Default) {
        try {
            AppLogger.i("initDatabaseContent")
            allCategories.forEach { category ->
                AppLogger.i("categoryQueries.insert ${category.id}")
                categoryQueries.insert(
                    id = category.id.toString(),
                    name = category.name.toString(),
                    lang = lang
                )
                category._allReferencingQuestions.forEach { question ->
                    AppLogger.i("questionQueries.insert ${question.id}")
                    questionQueries.insert(
                        id = question.id.toString(),
                        content = question.label.toString(),
                        detail = question.explanation ?: "",
                        lang = lang,
                        categoryId = category.id.toString(),
                        correctPorpositionId =
                        question.choices.first {
                            it.correct == true
                        }.id.toString(),
                        difficulty = question.difficulty ?: Difficulty.EASY.name,
                        illustration = question.illustration?.url
                    )
                    question.choices.forEach { proposition ->
                        AppLogger.i("propositionQueries.insert ${proposition.id}")
                        propositionQueries.insert(
                            id = proposition.id.toString(),
                            content = proposition.proposition.toString(),
                            isCorrect =
                            if (proposition.correct == true) {
                                1L
                            } else {
                                0L
                            },
                            questionId = question.id.toString(),
                            lang = lang
                        )
                    }
                }
            }
            return@withContext true
        } catch (ex: Exception) {
            AppLogger.e("CANT SAVE IN DB", ex)
            return@withContext false
        }
    }
}

internal suspend fun Database.hasContentInDB(forLang: Language? = null): Boolean {
    return withContext(Dispatchers.Default) {
        try {
            val categoryCount =
                forLang?.let {
                    categoryQueries.countForLang(forLang.code).executeAsOneOrNull() ?: 0
                } ?: kotlin.run {
                    categoryQueries.count().executeAsOneOrNull() ?: 0
                }
            val questionCount =
                if (categoryCount > 0) {
                    forLang?.let {
                        questionQueries.countForLang(forLang.code).executeAsOneOrNull() ?: 0
                    } ?: kotlin.run {
                        questionQueries.count().executeAsOneOrNull() ?: 0
                    }
                } else {
                    0
                }
            categoryCount > 0 && questionCount > 0
        } catch (ex: Exception) {
            AppLogger.e("DB ERROR", ex)
            false
        }
    }
}

internal suspend fun Database.getQuestions(
    game: GameChoice,
    gameDifficulty: Difficulty,
    maxResult: Long,
    lang: Language = currentLanguage
): List<QuestionChoice> {
    return withContext(Dispatchers.Default) {
        val questions =
            if (game == GameChoice.ALL) {
                questionQueries.selectRandomWithLimit(
                    difficulty = gameDifficulty.id,
                    size = maxResult,
                    lang = lang.code
                ).executeAsList()
            } else {
                questionQueries.byCategoryRandomQuestionWithLimit(
                    game.gameId,
                    gameDifficulty.id,
                    lang.code,
                    maxResult
                ).executeAsList()
            }
        buildQuestionChoice(game, questions)
    }
}

internal suspend fun Database.getNbQuestions(
    game: GameChoice,
    gameDifficulty: Difficulty,
    lang: Language = currentLanguage
): Long {
    return withContext(Dispatchers.Default) {
        val questions =
            if (game == GameChoice.ALL) {
                questionQueries.count().executeAsOne()
            } else {
                questionQueries.countByCategory(
                    game.gameId,
                    gameDifficulty.id,
                    lang.code
                ).executeAsOne()
            }
        questions
    }
}

suspend fun Database.getQuestions(
    vararg ids: String,
    lang: Language = currentLanguage
): List<QuestionChoice> {
    return withContext(Dispatchers.Default) {
        val questions = questionQueries.selectInside(ids.toList(), lang.code).executeAsList()
        val ordered = mutableListOf<Question>()
        ids.forEach { id ->
            ordered.add(questions.first { it.id == id })
        }
        buildQuestionChoice(GameChoice.fromId(ordered[0].categoryId), ordered, lang = lang)
    }
}

suspend fun Database.getQuestion(
    questionId: String,
    lang: Language = currentLanguage
): QuestionChoice {
    return withContext(Dispatchers.Default) {
        val question = questionQueries.select(questionId, lang.code).executeAsOne()
        val propositions =
            propositionQueries.selectForQuestionId(
                questionId = question.id,
                lang = lang.code
            ).executeAsList()
        QuestionChoice.fromDatabase(
            game = GameChoice.fromId(question.categoryId),
            data = question,
            propositions = propositions
        )
    }
}

private suspend fun Database.buildQuestionChoice(
    game: GameChoice?,
    questions: List<Question>,
    withProposition: Boolean = true,
    lang: Language = currentLanguage
): List<QuestionChoice> {
    return withContext(Dispatchers.Default) {
        questions.map { question ->
            val propositions =
                if (withProposition) {
                    propositionQueries.selectForQuestionIdAndRandom(
                        questionId = question.id,
                        lang = lang.code
                    ).executeAsList()
                } else {
                    emptyList()
                }
            QuestionChoice.fromDatabase(
                game = game ?: GameChoice.ALL,
                data = question,
                propositions = propositions
            )
        }
    }
}

suspend fun Database.searchQuestions(
    criteria: String,
    productSelected: Boolean,
    designSelected: Boolean,
    techSelected: Boolean,
    lang: Language = currentLanguage
): List<QuestionChoice> {
    return withContext(Dispatchers.Default) {
        val trimmedCriteria = criteria.trim()
        val categoriesListIds = mutableListOf<String>()
        if (productSelected) {
            categoriesListIds.add(GameChoice.PRODUCT.gameId)
        }
        if (designSelected) {
            categoriesListIds.add(GameChoice.DESIGN.gameId)
        }
        if (techSelected) {
            categoriesListIds.add(GameChoice.TECH.gameId)
        }
        if (categoriesListIds.isEmpty()) {
            categoriesListIds.add(GameChoice.PRODUCT.gameId)
            categoriesListIds.add(GameChoice.DESIGN.gameId)
            categoriesListIds.add(GameChoice.TECH.gameId)
        }
        val questions =
            if (trimmedCriteria.isEmpty()) {
                questionQueries.byCategoriesId(categoriesListIds, lang.code).executeAsList()
            } else {
                questionQueries.byCategoriesIdAndCriteria(
                    categoriesListIds,
                    trimmedCriteria,
                    lang.code
                ).executeAsList()
            }
        buildQuestionChoice(GameChoice.ALL, questions, withProposition = false)
    }
}

suspend fun Database.getAllQuestions(
    withProposition: Boolean = true,
    lang: Language = currentLanguage
): List<QuestionChoice> {
    return withContext(Dispatchers.Default) {
        val questions = questionQueries.selectAll(lang.code).executeAsList()
        buildQuestionChoice(GameChoice.ALL, questions, withProposition)
    }
}
