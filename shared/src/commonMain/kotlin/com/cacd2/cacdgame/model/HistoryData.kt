package com.cacd2.cacdgame.model

import com.apollographql.apollo3.mpp.currentTimeMillis
import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Serializable
@CommonParcelize
data class HistoryData(
    val id: Long,
    val timestamp: Long,
    val category: GameChoice,
    val data: List<QuestionChoice>
) :
    CommonParcelable {
    companion object {
        val dummy: HistoryData
            get() =
                HistoryData(
                    id = 1,
                    timestamp = currentTimeMillis(),
                    category = GameChoice.DESIGN,
                    data =
                    listOf(
                        QuestionChoice(
                            id = "1",
                            content = "What is a server sdfdfs sddfsfdsdfs  dsfdsdfsdf ?",
                            detail = null,
                            game = GameChoice.DESIGN,
                            answers = emptyList(),
                            responseTime = 0L,
                            illustration = null,
                            userSelectedAnswer =
                            AnswerChoice(
                                id = "1",
                                content = "une question",
                                isCorrect = false
                            )
                        ),
                        QuestionChoice(
                            id = "1",
                            content = "What is server development?",
                            detail = null,
                            game = GameChoice.DESIGN,
                            answers = emptyList(),
                            responseTime = 0L,
                            illustration = null,
                            userSelectedAnswer =
                            AnswerChoice(
                                id = "1",
                                content = "1",
                                isCorrect = false
                            )
                        ),
                        QuestionChoice(
                            id = "1",
                            content = "What is a server room?",
                            detail = null,
                            game = GameChoice.DESIGN,
                            answers = emptyList(),
                            responseTime = 0L,
                            illustration = null,
                            userSelectedAnswer =
                            AnswerChoice(
                                id = "1",
                                content = "1",
                                isCorrect = true
                            )
                        )
                    )
                )
    }
}
