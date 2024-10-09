package com.cacd2.cacdgame.datasource.database

import com.cacd2.cacdgame.database.AppDatabase

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */
class Database {
    companion object {
        val data = Database()
    }

    private val sqlDriver = DriverFactory.createDriver(false)
    private val connection = AppDatabase(sqlDriver)
    internal val categoryQueries = connection.categoryQueries
    internal val questionQueries = connection.questionQueries
    internal val propositionQueries = connection.propositionQueries
    internal val settingQueries = connection.settingQueries
    internal val choiceHistoryQueries = connection.choiceHistoryQueries
    internal val choiceHistoryLineQueries = connection.choiceHistoryLineQueries
}
