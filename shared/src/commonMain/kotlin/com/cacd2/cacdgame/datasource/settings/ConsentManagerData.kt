package com.cacd2.cacdgame.datasource.settings

/**
 * Created by francois.dabonot@cacd2.fr on 19/06/2023.
 */
data class ConsentManagerData(
    var analytics: Boolean = true,
    var crash: Boolean = true,
    var performance: Boolean = true
) {
    companion object {
        suspend fun fromDB(): ConsentManagerData {
            val dbAnalytics = SettingsDataSource.get(AppSettingsKeys.ANALYTICS_ENABLED)
            val dbCrash = SettingsDataSource.get(AppSettingsKeys.CRASH_ENABLED)
            val dbPerformance = SettingsDataSource.get(AppSettingsKeys.PERFORMANCE_ENABLED)
            return ConsentManagerData(
                dbAnalytics == "1",
                dbCrash == "1",
                dbPerformance == "1"
            )
        }
    }
}
