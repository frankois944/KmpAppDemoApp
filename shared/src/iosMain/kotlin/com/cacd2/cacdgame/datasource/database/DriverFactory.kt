package com.cacd2.cacdgame.datasource.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.cacd2.cacdgame.database.AppDatabase
import com.cacd2.cacdgame.tools.logger.AppLogger

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */

actual object DriverFactory {

    // actual val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory("apollo.db")

    actual fun createDriver(inMemory: Boolean): SqlDriver {
        AppLogger.i("NativeSqliteDriver")
        val driver = if (inMemory) {
            NativeSqliteDriver(
                schema = AppDatabase.Schema,
                name = "app.db",
                onConfiguration = { config: DatabaseConfiguration ->
                    return@NativeSqliteDriver config.copy(inMemory = true, name = null)
                }
            )
        } else {
            NativeSqliteDriver(AppDatabase.Schema, name = "app.db")
        }
        return LogSqliteDriver(driver) { log ->
            AppLogger.v(log)
        }
    }
}
