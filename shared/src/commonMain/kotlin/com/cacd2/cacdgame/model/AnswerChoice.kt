package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import com.cacd2.cacdgame.database.Proposition
import kotlinx.serialization.Serializable

/**
 * Created by frankois on 15/05/2023.
 */
@Serializable
@CommonParcelize
data class AnswerChoice(
    val id: String,
    val content: String,
    val isCorrect: Boolean
) : CommonParcelable {
    companion object {
        fun fromDatabase(data: Proposition): AnswerChoice {
            return AnswerChoice(
                id = data.id,
                content = data.content,
                isCorrect = data.isCorrect == 1L
            )
        }
    }
}
