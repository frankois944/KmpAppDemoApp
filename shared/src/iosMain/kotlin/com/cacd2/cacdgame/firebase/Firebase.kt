package com.cacd2.cacdgame.firebase

import co.touchlab.crashkios.crashlytics.enableCrashlytics
import co.touchlab.crashkios.crashlytics.setCrashlyticsUnhandledExceptionHook
import cocoapods.FirebaseAnalytics.FIRAnalytics
import cocoapods.FirebaseCore.FIRApp
import cocoapods.FirebaseCrashlytics.FIRCrashlytics
import com.cacd2.cacdgame.datasource.settings.ConsentManagerData
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Created by francois.dabonot@cacd2.fr on 03/07/2023.
 */
@OptIn(ExperimentalForeignApi::class)
actual object Firebase {

    actual fun initFirebase(context: Any?) {
        setupCrashlytics()
        FIRApp.configure()
    }

    actual fun toggleAnalytics(consent: ConsentManagerData) {
        AppLogger.d("update analytics $consent")
        FIRCrashlytics.crashlytics().setCrashlyticsCollectionEnabled(consent.crash)
        FIRAnalytics.setAnalyticsCollectionEnabled(consent.analytics)
        AppLogger.i("setupCrashlytics")
        setupCrashlytics()
    }

    private fun setupCrashlytics() {
        AppLogger.i("enableCrashlytics")
        enableCrashlytics()
        AppLogger.i("setCrashlyticsUnhandledExceptionHook")
        setCrashlyticsUnhandledExceptionHook()
    }
}
