package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.getAllQuestions
import com.cacd2.cacdgame.datasource.database.dao.getHistoryCount
import com.cacd2.cacdgame.datasource.database.dao.getHistoryDoneCategory
import com.cacd2.cacdgame.datasource.database.dao.getNbQuestions
import com.cacd2.cacdgame.datasource.database.dao.getQuestion
import com.cacd2.cacdgame.datasource.database.dao.getQuestions
import kotlinx.serialization.Serializable

/**
 * Created by frankois on 15/05/2023.
 */
@Serializable
@CommonParcelize
enum class GameChoice(val gameId: String) : CommonParcelable {
    DESIGN("105396726"),
    PRODUCT("105396727"),
    TECH("105396728"),
    ALL("424242")
    ;

    suspend fun getQuestions(
        gameDifficulty: Difficulty,
        maxQuestion: Long? = null
    ): List<QuestionChoice> {
        return Database.data.getQuestions(
            game = this,
            gameDifficulty = gameDifficulty,
            maxResult = maxQuestion ?: GameChoice.maxQuestion
        )
    }

    suspend fun hasQuestions(gameDifficulty: Difficulty): Boolean {
        return Database.data.getNbQuestions(this, gameDifficulty) > 0
    }

    companion object {
        const val maxQuestion = 10L

        fun fromId(id: String): GameChoice {
            return entries.first { it.gameId == id }
        }

        suspend fun getAllQuestions(withProposition: Boolean = true): List<QuestionChoice> {
            return Database.data.getAllQuestions(withProposition)
        }

        suspend fun getAvailableHistoryChoice(): List<GameChoice> {
            return Database.data.getHistoryDoneCategory()
        }

        suspend fun getQuestion(id: String): QuestionChoice {
            return Database.data.getQuestion(id)
        }

        suspend fun getQuestions(
            vararg ids: String,
            lang: Language = currentLanguage
        ): List<QuestionChoice> {
            return Database.data.getQuestions(ids = ids, lang = lang)
        }
    }
}
