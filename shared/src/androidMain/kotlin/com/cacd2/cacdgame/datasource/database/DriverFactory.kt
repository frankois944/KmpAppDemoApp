package com.cacd2.cacdgame.datasource.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import com.cacd2.cacdgame.KContext
import com.cacd2.cacdgame.database.AppDatabase
import com.cacd2.cacdgame.tools.logger.AppLogger

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */

actual object DriverFactory {

    // actual val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory("apollo.db")

    actual fun createDriver(inMemory: Boolean): SqlDriver {
        (KContext as? Context)?.let {
            AppLogger.i("AndroidSqliteDriver")
            val driver = if (inMemory) {
                AppLogger.i("LOAD IN MEMORY DB")
                AndroidSqliteDriver(AppDatabase.Schema, it, null)
            } else {
                AppLogger.i("LOAD IN FILE DB")
                AndroidSqliteDriver(AppDatabase.Schema, it, "app.db")
            }
            return LogSqliteDriver(sqlDriver = driver) { log ->
                AppLogger.v(log)
            }
        }
        throw Exception("No Android Context found (aka KContext)")
    }
}
