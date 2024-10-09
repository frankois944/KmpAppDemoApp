package com.cacd2.cacdgame.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.features.MainApp
import com.cacd2.cacdgame.android.features.trackEvent
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.model.Language
import com.cacd2.cacdgame.model.ScreenMode
import com.cacd2.cacdgame.tools.logger.AppLogger
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : AppCompatActivity(), KoinComponent {
    val appContext: AppContext = get()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        runBlocking(Dispatchers.Default) {
            appContext.loadContextFromDb()
            appContext.loadStats()
            val lang = Language.fromCode(getCurrentLocale().language)
            if (appContext.currentLanguageCode.value == Language.UNKNOWN) {
                appContext.currentLanguageCode.value =
                    if (lang == Language.UNKNOWN) {
                        Language.ENGLISH
                    } else {
                        lang
                    }
                save()
            }
        }
        setContent {
            KoinAndroidContext {
                AppTheme(screenMode = appContext.screenMode.value) {
                    LaunchedEffect(
                        appContext.screenMode.value,
                        appContext.forcedStatusBarColor.value,
                        appContext.currentLanguageCode.value
                    ) {
                        updateThemeAndLocal(appContext)
                        appContext.forcedStatusBarColor.value?.let {
                            updateStatusBarColor(it)
                        }
                    }
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        MainApp()
                    }
                }
            }
        }
    }

    private fun updateThemeAndLocal(appContext: AppContext) {
        val originalValue = AppCompatDelegate.getDefaultNightMode()
        when (appContext.screenMode.value) {
            ScreenMode.SYSTEM -> {
                if (originalValue != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM &&
                    originalValue != AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                ) {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                    )
                }
            }

            ScreenMode.DARK -> {
                if (originalValue != AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            ScreenMode.LIGHT -> {
                if (originalValue != AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        val appLocale: LocaleListCompat =
            LocaleListCompat.forLanguageTags(
                appContext.currentLanguageCode.value.code
            )
        currentLanguage = Language.fromCode(appLocale.toLanguageTags())
        if (appLocale.toLanguageTags() != getCurrentLocale().language) {
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    private fun getCurrentLocale(): Locale {
        return this.resources.configuration.locales.get(0)
    }

    private fun updateStatusBarColor(color: Int) {
        window.statusBarColor = color
    }

    override fun onStart() {
        super.onStart()
        AppLogger.d("onStart")
        trackEvent("onStart")
    }

    override fun onResume() {
        super.onResume()
        AppLogger.d("onResume")
        trackEvent("onResume")
    }

    override fun onPause() {
        super.onPause()
        AppLogger.d("onPause")
        trackEvent("onPause")
    }

    override fun onStop() {
        super.onStop()
        AppLogger.d("onStop")
        trackEvent("onStop")
    }
}
