package com.cacd2.cacdgame.datasource.settings

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 24/05/2023.
 */

@Serializable
@CommonParcelize
enum class AppSettingsKeys(val initialData: String?) : CommonParcelable {
    USERNAME(null),
    ON_BOARDING_DONE("0"),
    TIMEOUT_ENABLED("1"),
    ANALYTICS_ENABLED("1"),
    CRASH_ENABLED("1"),
    PERFORMANCE_ENABLED("1"),
    SCREEN_MODE("0"),
    CURRENT_LANGUAGE_CODE(""),
    CONSENT_MANAGER_DISPLAYED("0"),
    LAST_CALL_DATA_FETCH_DATETIME_FR(null),
    LAST_DATA_UPDATE_DATETIME_FR(null),
    LAST_CALL_DATA_FETCH_DATETIME_EN(null),
    LAST_DATA_UPDATE_DATETIME_EN(null)
}

object SettingsDataSource {
    suspend fun save(key: AppSettingsKeys, value: String?) {
        return withContext(Dispatchers.Default) {
            try {
                Database.data.settingQueries.update(value, key.name)
            } catch (ex: Exception) {
                AppLogger.e("CANT SAVE SETTING", ex)
            }
        }
    }

    suspend fun get(key: AppSettingsKeys): String? {
        return withContext(Dispatchers.Default) {
            try {
                return@withContext Database.data.settingQueries.get(key.name).executeAsOne().value_
            } catch (ex: Exception) {
                AppLogger.e("CANT GET SETTING", ex)
            }
            return@withContext null
        }
    }
}
