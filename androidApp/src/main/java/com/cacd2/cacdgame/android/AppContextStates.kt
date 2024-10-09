package com.cacd2.cacdgame.android

import androidx.collection.mutableScatterMapOf
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.defaultTimeout
import com.cacd2.cacdgame.model.Language
import com.cacd2.cacdgame.model.ScreenMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
val externalMap = mutableScatterMapOf<String, MutableState<Any?>>()

@Suppress("UNCHECKED_CAST")
private fun <T> getState(key: String, initialValue: T?): MutableState<T> {
    if (externalMap[key] == null) {
        externalMap[key] = mutableStateOf(initialValue)
    }
    return externalMap[key] as MutableState<T>
}

val AppContext.gameId: MutableState<String?>
    get() = getState("gameId", mGameId)
val AppContext.username: MutableState<String?>
    get() = getState("username", mUsername)
val AppContext.appbarTitle: MutableState<String?>
    get() = getState("appbarTitle", mAppbarTitle)
val AppContext.forceHideBackButton: MutableState<Boolean>
    get() = getState("forceHideBackButton", mForceHideBackButton ?: false)
val AppContext.forceHideBottomBar: MutableState<Boolean>
    get() = getState("forceHideBottomBar", mForceHideBottomBar ?: false)
val AppContext.hasTimeout: MutableState<Boolean>
    get() = getState("hasTimeout", mHasTimeout ?: false)
val AppContext.maxTimeout: MutableState<Long>
    get() = getState("maxTimeout", mMaxTimeout ?: defaultTimeout)
val AppContext.showCloseScreenButton: MutableState<Boolean>
    get() = getState("showCloseScreenButton", mShowCloseScreenButton ?: false)
val AppContext.analyticsEnabled: MutableState<Boolean>
    get() = getState("analyticsEnabled", mAnalyticsEnabled ?: false)
val AppContext.crashEnabled: MutableState<Boolean>
    get() = getState("crashEnabled", mCrashEnabled ?: false)
val AppContext.performanceEnabled: MutableState<Boolean>
    get() = getState("performanceEnabled", mPerformanceEnabled ?: false)
val AppContext.screenMode: MutableState<ScreenMode>
    get() = getState("screenMode", mScreenMode ?: ScreenMode.SYSTEM)
val AppContext.showSearchLexicalButton: MutableState<Boolean>
    get() = getState("showSearchLexicalButton", mShowSearchScreenButton ?: false)
val AppContext.showHideLexicalButton: MutableState<Boolean>
    get() = getState("showHideLexicalButton", mShowHideScreenButton ?: false)
val AppContext.showFilterHistoryButton: MutableState<Boolean>
    get() = getState("showFilterHistoryButton", mShowFilterHistoryButton ?: false)
val AppContext.showAppBarContent: MutableState<Boolean>
    get() = getState("showAppBarContent", mShowAppBarContent ?: false)
val AppContext.forcedStatusBarColor: MutableState<Int?>
    get() = getState("forcedStatusBarColor", mForcedStatusBarColor)
val AppContext.isAppBarVisible: MutableState<Boolean>
    get() = getState("isAppBarVisible", mIsAppBarVisible ?: true)
val AppContext.isSearchCriteriaOpen: MutableState<Boolean>
    get() = getState("isSearchCriteriaOpen", mIsSearchCriteriaOpen ?: false)
val AppContext.currentLanguageCode: MutableState<Language>
    get() = getState("currentLanguageCode", Language.fromCode(mCurrentLanguageCode ?: "?"))
val AppContext.consentManagerDisplayed: MutableState<Boolean>
    get() = getState("consentManagerDisplayed", mConsentManagerDisplayed ?: false)
val AppContext.forceHideFabButton: MutableState<Boolean>
    get() = getState("forceHideFabButton", mForceHideFabButton ?: false)

fun AppContext.loadStats() = runBlocking(Dispatchers.Default) {
    getState("gameId", mGameId)
    getState("username", mUsername)
    getState("appbarTitle", mAppbarTitle)
}

suspend fun save() {
    withContext(Dispatchers.Default) {
        AppContext.context()?.let {
            it.storeContext(
                it.username.value,
                it.hasTimeout.value,
                it.analyticsEnabled.value,
                it.crashEnabled.value,
                it.performanceEnabled.value,
                it.screenMode.value,
                it.currentLanguageCode.value.code,
                it.consentManagerDisplayed.value
            )
        }
    }
}
