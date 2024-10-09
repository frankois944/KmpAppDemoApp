package com.cacd2.cacdgame.firebase

import android.content.Context
import com.cacd2.cacdgame.datasource.settings.ConsentManagerData
import com.cacd2.cacdgame.tools.logger.AppLogger
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.initialize

/**
 * Created by francois.dabonot@cacd2.fr on 03/07/2023.
 */
actual object Firebase {

    actual fun initFirebase(context: Any?) {
        (context as? Context)?.let {
            Firebase.initialize(context = context)
        } ?: run {
            error("Context invalid")
        }
    }

    actual fun toggleAnalytics(consent: ConsentManagerData) {
        AppLogger.d("update analytics $consent")
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(consent.crash)
        Firebase.analytics.setAnalyticsCollectionEnabled(consent.analytics)
    }
}
