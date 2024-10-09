package com.cacd2.cacdgame.datasource.database

import app.cash.sqldelight.db.SqlDriver

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */
expect object DriverFactory {
    // val sqlNormalizedCacheFactory: SqlNormalizedCacheFactory
    fun createDriver(inMemory: Boolean): SqlDriver
}
