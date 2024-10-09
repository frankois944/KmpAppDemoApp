package com.cacd2.cacdgame

import com.cacd2.cacdgame.datasource.settings.AppSettingsKeys
import com.cacd2.cacdgame.datasource.settings.SettingsDataSource
import com.cacd2.cacdgame.model.ScreenMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Created by frankois on 21/06/2023.
 */

const val defaultTimeout = 45_000L
const val updateDataHoursInterval = 24
const val updateCallHoursInterval = 12

@Single
class AppContext(
    var mGameId: String? = null,
    var mUsername: String? = null,
    var mAppbarTitle: String? = null,
    var mForceHideBackButton: Boolean? = false,
    var mForceHideBottomBar: Boolean? = false,
    var mShowAppBarContent: Boolean? = false,
    var mHasTimeout: Boolean? = true,
    var mShowCloseScreenButton: Boolean? = false,
    var mMaxTimeout: Long? = defaultTimeout,
    var mAnalyticsEnabled: Boolean? = false,
    var mCrashEnabled: Boolean? = false,
    var mPerformanceEnabled: Boolean? = false,
    var mScreenMode: ScreenMode? = null,
    var mShowSearchScreenButton: Boolean? = null,
    var mShowHideScreenButton: Boolean? = null,
    var mForcedStatusBarColor: Int? = null,
    var mIsAppBarVisible: Boolean? = true,
    var mIsSearchCriteriaOpen: Boolean? = false,
    var mCurrentLanguageCode: String? = null,
    var mConsentManagerDisplayed: Boolean? = false,
    var mShowFilterHistoryButton: Boolean? = false,
    var mForceHideFabButton: Boolean? = false
) {

    companion object : KoinComponent {
        fun context(): AppContext? {
            return try {
                get()
            } catch (_: Exception) {
                null
            }
        }
    }

    suspend fun storeContext(
        username: String?,
        hasTimeout: Boolean?,
        analyticsEnabled: Boolean?,
        crashEnabled: Boolean?,
        performanceEnabled: Boolean?,
        screenMode: ScreenMode?,
        languageCode: String?,
        consentManagerDisplayed: Boolean?
    ) {
        if (username != mUsername) {
            SettingsDataSource.save(
                AppSettingsKeys.USERNAME,
                username
            )
            mUsername = username
        }
        if (mHasTimeout != hasTimeout) {
            SettingsDataSource.save(
                AppSettingsKeys.TIMEOUT_ENABLED,
                if (hasTimeout == true) {
                    "1"
                } else {
                    "0"
                }
            )
            mHasTimeout = hasTimeout
        }
        if (mAnalyticsEnabled != analyticsEnabled) {
            SettingsDataSource.save(
                AppSettingsKeys.ANALYTICS_ENABLED,
                if (analyticsEnabled == true) {
                    "1"
                } else {
                    "0"
                }
            )
            mAnalyticsEnabled = analyticsEnabled
        }
        if (mCrashEnabled != crashEnabled) {
            SettingsDataSource.save(
                AppSettingsKeys.CRASH_ENABLED,
                if (crashEnabled == true) {
                    "1"
                } else {
                    "0"
                }
            )
            mCrashEnabled = crashEnabled
        }
        if (mPerformanceEnabled != performanceEnabled) {
            SettingsDataSource.save(
                AppSettingsKeys.PERFORMANCE_ENABLED,
                if (performanceEnabled == true) {
                    "1"
                } else {
                    "0"
                }
            )
            mPerformanceEnabled = performanceEnabled
        }
        if (mScreenMode != screenMode) {
            SettingsDataSource.save(
                AppSettingsKeys.SCREEN_MODE,
                (screenMode ?: ScreenMode.SYSTEM).id
            )
            mScreenMode = screenMode
        }
        if (mCurrentLanguageCode != languageCode) {
            SettingsDataSource.save(
                AppSettingsKeys.CURRENT_LANGUAGE_CODE,
                languageCode ?: ""
            )
            mCurrentLanguageCode = languageCode
        }
        if (mConsentManagerDisplayed != consentManagerDisplayed) {
            SettingsDataSource.save(
                AppSettingsKeys.CONSENT_MANAGER_DISPLAYED,
                if (consentManagerDisplayed == true) {
                    "1"
                } else {
                    "0"
                }
            )
            mConsentManagerDisplayed = consentManagerDisplayed
        }
    }

    suspend fun loadContextFromDb() {
        withContext(Dispatchers.Default) {
            val dbUsername = SettingsDataSource.get(AppSettingsKeys.USERNAME)
            this@AppContext.mUsername = dbUsername
            val dbHasTimeout = SettingsDataSource.get(AppSettingsKeys.TIMEOUT_ENABLED)
            this@AppContext.mHasTimeout = dbHasTimeout == "1"
            val dbAnalytics = SettingsDataSource.get(AppSettingsKeys.ANALYTICS_ENABLED)
            this@AppContext.mAnalyticsEnabled = dbAnalytics == "1"
            val dbCrash = SettingsDataSource.get(AppSettingsKeys.CRASH_ENABLED)
            this@AppContext.mCrashEnabled = dbCrash == "1"
            val dbPerformance = SettingsDataSource.get(AppSettingsKeys.PERFORMANCE_ENABLED)
            this@AppContext.mPerformanceEnabled = dbPerformance == "1"
            val dbScreenMode = SettingsDataSource.get(AppSettingsKeys.SCREEN_MODE)
            this@AppContext.mScreenMode = ScreenMode.fromId(dbScreenMode)
            val dbLanguageCode = SettingsDataSource.get(AppSettingsKeys.CURRENT_LANGUAGE_CODE)
            this@AppContext.mCurrentLanguageCode = dbLanguageCode
            val dbConsentManager = SettingsDataSource.get(AppSettingsKeys.CONSENT_MANAGER_DISPLAYED)
            this@AppContext.mConsentManagerDisplayed = dbConsentManager == "1"
        }
    }
}
