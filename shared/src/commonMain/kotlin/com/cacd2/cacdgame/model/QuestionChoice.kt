package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.database.Proposition
import com.cacd2.cacdgame.database.Question
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlinx.serialization.Serializable

/**
 * Created by frankois on 15/05/2023.
 */
@Serializable
@CommonParcelize
data class QuestionChoice(
    val id: String,
    val content: String,
    val detail: String?,
    val game: GameChoice,
    val answers: List<AnswerChoice>,
    var userSelectedAnswer: AnswerChoice? = null,
    val difficulty: Difficulty = Difficulty.EASY,
    val lang: Language = currentLanguage,
    val illustration: String?,
    var responseTime: Long
) : CommonParcelable {
    companion object {
        fun fromDatabase(
            game: GameChoice,
            data: Question,
            propositions: List<Proposition>
        ): QuestionChoice {
            val answers =
                propositions.map {
                    AnswerChoice.fromDatabase(it)
                }
            return QuestionChoice(
                id = data.id,
                content = data.content,
                detail = data.detail,
                answers = answers,
                game = game,
                difficulty = Difficulty.fromId(data.difficulty),
                lang = Language.fromCode(data.lang),
                responseTime = 0L,
                illustration = data.illustration
            )
        }

        val dummy: QuestionChoice
            get() {
                return QuestionChoice(
                    id = Random.nextLong().toString(),
                    content = "Qu'est-ce que l'UI/UX ?",
                    detail = "L'UI (Interface Utilisateur) se réfère à la conception visuelle et fonctionnelle d'une application, tandis que l'UX (Expérience Utilisateur) englobe l'ensemble de l'expérience de l'utilisateur, y compris la navigation, l'interactivité et la convivialité. Une bonne UI/UX garantit une expérience utilisateur positive et facilite l'accomplissement des tâches.",
                    game = GameChoice.DESIGN,
                    difficulty = Difficulty.EASY,
                    responseTime = 0L,
                    illustration = null,
                    answers =
                    listOf(
                        AnswerChoice(
                            id = "2",
                            content = "Navigation et interactivité",
                            isCorrect = false
                        ),
                        AnswerChoice(
                            id = "1",
                            content = "Conception visuelle et fonctionnelle",
                            isCorrect = true
                        ),
                        AnswerChoice(
                            id = "3",
                            content = "Analyse des besoins utilisateurs",
                            isCorrect = false
                        ),
                        AnswerChoice(
                            id = "4",
                            content = "Optimisation des performances applicatives",
                            isCorrect = false
                        )
                    )
                )
            }
    }
}

fun List<QuestionChoice>.nbOkResponse(): Long {
    return this.filter { it.userSelectedAnswer?.isCorrect == true }.size.toLong()
}

fun List<QuestionChoice>.nbKoResponse(): Long {
    return this.filter { it.userSelectedAnswer?.isCorrect == false }.size.toLong()
}

fun List<QuestionChoice>.averageMsResponseTime(): Long {
    val totalTime =
        (
            this.sumOf {
                it.responseTime
            }.toFloat() / this.size.toFloat()
            ).roundToLong()
    return if (totalTime < 1_000L) {
        1_000L
    } else {
        totalTime
    }
}
