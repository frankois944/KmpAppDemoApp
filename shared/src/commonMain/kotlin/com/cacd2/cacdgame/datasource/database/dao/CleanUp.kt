package com.cacd2.cacdgame.datasource.database.dao

import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.model.Language
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */
internal suspend fun Database.clearQuestions(language: Language? = null) {
    return withContext(Dispatchers.Default) {
        categoryQueries.transaction {
            AppLogger.i("categoryQueries.removeAllCategories")
            language?.let {
                categoryQueries.removeAllForLang(it.code)
            } ?: run {
                categoryQueries.removeAll()
            }
        }
        questionQueries.transaction {
            AppLogger.i("questionQueries.removeAllQuestions")
            language?.let {
                questionQueries.removeAllForLang(it.code)
            } ?: run {
                questionQueries.removeAll()
            }
        }
        propositionQueries.transaction {
            AppLogger.i("answerQueries.removeAllPropositions")
            language?.let {
                propositionQueries.removeAllForLang(it.code)
            } ?: run {
                propositionQueries.removeAll()
            }
        }
    }
}

internal suspend fun Database.clearHistory() {
    return withContext(Dispatchers.Default) {
        choiceHistoryQueries.transaction {
            AppLogger.i("choiceHistoryQueries.removeAll")
            choiceHistoryQueries.removeAll()
        }
        choiceHistoryLineQueries.transaction {
            AppLogger.i("choiceHistoryLineQueries.removeAll")
            choiceHistoryLineQueries.removeAll()
        }
    }
}
