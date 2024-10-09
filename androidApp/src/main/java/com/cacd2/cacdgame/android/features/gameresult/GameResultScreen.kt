package com.cacd2.cacdgame.android.features.gameresult

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.gameresult.loader.GameResultLoaderView
import com.cacd2.cacdgame.android.features.gameresult.part2.GameResultPart2View
import com.cacd2.cacdgame.android.features.gameresult.ui.GameResultView
import com.cacd2.cacdgame.android.features.gameresult.ui.ResultAnimationView
import com.cacd2.cacdgame.android.forceHideBottomBar
import com.cacd2.cacdgame.android.forceHideFabButton
import com.cacd2.cacdgame.android.isAppBarVisible
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.getHistory
import com.cacd2.cacdgame.model.HistoryData
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */

enum class GameResultActions {
    SHOW_OK_QUESTIONS,
    SHOW_KO_QUESTIONS,
    OPEN_WEBSITE,
    SHOW_CONTACT,
    SHOW_DETAIL,
    SHARE
}

@Composable
fun GameResultScreen(
    appContext: AppContext = koinInject(),
    historyId: Long,
    onClose: () -> Unit,
    onShowContact: () -> Unit,
    onShowAnswersHistory: (Boolean) -> Unit,
    shouldDisplayResultBanner: Boolean = true,
    shouldDisplayLoaderView: Boolean = true,
    testQuestions: HistoryData? = null
) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cacd2.fr"))
    var displayResultBanner by rememberSaveable { mutableStateOf(shouldDisplayResultBanner) }
    var showCompanyDetail by rememberSaveable { mutableStateOf(false) }
    var showResultLoaded by rememberSaveable { mutableStateOf(shouldDisplayLoaderView) }
    var questions by rememberSaveable { mutableStateOf(testQuestions) }
    val scrollState = rememberScrollState()
    var scrollToPosition by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    appContext.forceHideBottomBar.value = showResultLoaded
    appContext.forceHideFabButton.value = showResultLoaded

    LaunchedEffect(Unit) {
        if (questions == null) {
            questions = Database.data.getHistory(historyId)
        }
    }

    if (showResultLoaded) {
        appContext.isAppBarVisible.value = false
        GameResultLoaderView(
            targetSeconds = 3,
            modifier = Modifier.fillMaxSize()
        ) {
            showResultLoaded = false
        }
    } else {
        Box {
            appContext.isAppBarVisible.value = true
            val backHandlingEnabled by remember { mutableStateOf(true) }
            BackHandler(backHandlingEnabled) {
                if (showCompanyDetail) {
                    showCompanyDetail = false
                } else {
                    onClose()
                }
            }
            Box(
                contentAlignment = Alignment.BottomCenter
            ) {
                CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
                    questions?.let {
                        Column(
                            modifier = Modifier.verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GameResultView(
                                appContext = appContext,
                                history = it,
                                onAction = {
                                    displayResultBanner = false
                                    when (it) {
                                        GameResultActions.SHOW_OK_QUESTIONS ->
                                            onShowAnswersHistory(
                                                true
                                            )

                                        GameResultActions.SHOW_KO_QUESTIONS ->
                                            onShowAnswersHistory(
                                                false
                                            )

                                        GameResultActions.SHARE -> {}
                                        GameResultActions.SHOW_DETAIL -> {
                                            coroutineScope.launch {
                                                scrollState.animateScrollTo(
                                                    scrollToPosition.roundToInt()
                                                )
                                            }
                                        }

                                        else -> {}
                                    }
                                },
                                modifier = Modifier
                            )
                            GameResultPart2View(
                                modifier =
                                Modifier.onGloballyPositioned { coordinates ->
                                    scrollToPosition = coordinates.positionInRoot().y
                                }
                            ) {
                                displayResultBanner = false
                                when (it) {
                                    GameResultActions.OPEN_WEBSITE -> context.startActivity(intent)
                                    GameResultActions.SHOW_CONTACT -> {
                                        onShowContact()
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
            if (displayResultBanner) {
                ResultAnimationView(
                    modifier = Modifier.fillMaxSize(),
                    onCompletion = {
                        displayResultBanner = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun GameResultPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultScreen(
                appContext = AppContext(),
                historyId = 1,
                onShowAnswersHistory = {
                },
                onClose = {},
                shouldDisplayResultBanner = false,
                shouldDisplayLoaderView = false,
                onShowContact = {},
                testQuestions = HistoryData.dummy
            )
        }
    }
}

@Preview
@Composable
fun GameResultWithBannerPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                historyId = 1,
                onClose = {
                },
                onShowAnswersHistory = {},
                shouldDisplayResultBanner = true,
                shouldDisplayLoaderView = false,
                onShowContact = {},
                testQuestions = HistoryData.dummy
            )
        }
    }
}

@Preview
@Composable
fun GameResultWithLoaderPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultScreen(
                appContext = AppContext(),
                historyId = 1,
                onClose = {
                },
                onShowAnswersHistory = {},
                shouldDisplayResultBanner = true,
                shouldDisplayLoaderView = true,
                onShowContact = {},
                testQuestions = HistoryData.dummy
            )
        }
    }
}
