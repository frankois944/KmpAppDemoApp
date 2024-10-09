package com.cacd2.cacdgame

import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.initDatabaseSettings
import com.cacd2.cacdgame.datasource.settings.ConsentManagerData
import com.cacd2.cacdgame.firebase.Firebase
import com.cacd2.cacdgame.matomo.Tracker
import com.cacd2.cacdgame.tools.logger.AppLogger

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */
expect fun startPlatform()

object SharedApp {
    suspend fun start(context: Any? = null) {
        KContext = context
        AppLogger.i("Firebase.initialize")
        Firebase.initFirebase(KContext)
        AppLogger.i("platformSetup")
        startPlatform()
        AppLogger.i("initDatabaseSettings")
        Database.data.initDatabaseSettings()
        updateAnalytics(ConsentManagerData.fromDB())
    }

    fun updateAnalytics(consent: ConsentManagerData) {
        if (!isDebug) {
            Firebase.toggleAnalytics(consent)
            Tracker.instance.toggle(consent.analytics)
        } else {
            AppLogger.i("Debug mode is enabled, deactivating all analytics")
            Firebase.toggleAnalytics(
                ConsentManagerData(
                    analytics = false,
                    crash = false,
                    performance = false
                )
            )
            Tracker.instance.toggle(false)
        }
    }
}
