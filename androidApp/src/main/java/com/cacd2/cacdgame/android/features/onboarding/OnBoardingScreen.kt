package com.cacd2.cacdgame.android.features.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.onboarding.ui.OnBoardingPage1View
import com.cacd2.cacdgame.android.features.onboarding.ui.OnBoardingPage2View
import com.cacd2.cacdgame.android.features.onboarding.ui.OnBoardingPage3View
import com.cacd2.cacdgame.android.features.onboarding.ui.OnBoardingPage4View
import com.cacd2.cacdgame.android.features.trackScreenView
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun OnBoardingScreen(
    appContext: AppContext = koinInject(),
    onGoToSelectionScreen: (String) -> Unit
) {
    val pagerState =
        rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) { 4 }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var latestPage by rememberSaveable { mutableIntStateOf(0) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            AppLogger.d("Page change : Page changed to $page")
            latestPage = page
            coroutineScope.launch {
                try {
                    if (page == 3) {
                        delay(250)
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    } else {
                        focusRequester.freeFocus()
                        keyboardController?.hide()
                    }
                } catch (ex: Exception) {
                    AppLogger.v("NOT FOCUSED, IGNORE IT", ex)
                }
            }
        }
    }
    val goToNextPage: () -> Unit = {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
    val goToPrevPage: () -> Unit = {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                trackScreenView("OnBoardingPage1View")
                OnBoardingPage1View(
                    appContext = appContext,
                    modifier = Modifier.fillMaxSize(),
                    nextPage = goToNextPage
                )
            }

            1 -> {
                trackScreenView("OnBoardingPage2View")
                OnBoardingPage2View(
                    appContext = appContext,
                    modifier = Modifier.fillMaxSize(),
                    prevPage = goToPrevPage,
                    nextPage = goToNextPage
                )
            }

            2 -> {
                trackScreenView("OnBoardingPage4View")
                OnBoardingPage3View(
                    appContext = appContext,
                    modifier = Modifier.fillMaxSize(),
                    prevPage = goToPrevPage,
                    nextPage = goToNextPage
                )
            }

            3 -> {
                trackScreenView("OnBoardingPage4View")
                OnBoardingPage4View(
                    appContext = appContext,
                    modifier = Modifier.fillMaxSize(),
                    prevPage = goToPrevPage,
                    focusRequester = focusRequester,
                    nextPage = {
                        focusRequester.freeFocus()
                        onGoToSelectionScreen(it)
                    }
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun OnBoardingScreenPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            OnBoardingScreen(appContext = AppContext()) {}
        }
    }
}
