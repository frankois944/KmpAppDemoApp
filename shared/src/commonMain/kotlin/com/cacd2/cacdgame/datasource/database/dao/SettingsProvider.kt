package com.cacd2.cacdgame.datasource.database.dao

import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.settings.AppSettingsKeys
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by francois.dabonot@cacd2.fr on 24/05/2023.
 */

internal suspend fun Database.initDatabaseSettings() {
    return withContext(Dispatchers.Default) {
        try {
            AppLogger.i("buildSettings if not present")
            AppSettingsKeys.entries.forEach {
                if (settingQueries.get(it.name).executeAsOneOrNull() == null) {
                    AppLogger.i("inserting ${it.name} with initial value ${it.initialData}")
                    settingQueries.insert(it.name, it.initialData)
                }
            }
        } catch (ex: Exception) {
            AppLogger.e("CANT SAVE IN DB", ex)
        }
    }
}
