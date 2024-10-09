package com.cacd2.cacdgame.android.features.gameplay.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.AppEvent
import com.cacd2.cacdgame.EventBus
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.gameplay.ui.cancelgame.GamePlayCancelGameView
import com.cacd2.cacdgame.android.features.ui.resultbanner.ResultBannerView
import com.cacd2.cacdgame.android.hasTimeout
import com.cacd2.cacdgame.android.maxTimeout
import com.cacd2.cacdgame.android.showCloseScreenButton
import com.cacd2.cacdgame.android.utility.TimerTicks
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.QuestionChoice
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by frankois on 21/05/2023.
 */
@Composable
fun GamePlayBoardView(
    appContext: AppContext = koinInject(),
    questions: List<QuestionChoice>,
    onShowResult: (List<QuestionChoice>) -> Unit,
    onCancelGame: () -> Unit,
    onShowDefinition: (QuestionChoice) -> Unit,
    currentState: GamePlayUIState = GamePlayUIState.IN_PROGRESS
) {
    var selectedAnswerChoice by rememberSaveable { mutableStateOf<AnswerChoice?>(null) }
    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var uiState by rememberSaveable { mutableStateOf(currentState) }

    val backHandlingEnabled by remember { mutableStateOf(true) }
    var currentTimeoutProgression by rememberSaveable { mutableFloatStateOf(0f) }
    var cTime by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val coroutine = rememberCoroutineScope()
    var currentTick by rememberSaveable { mutableLongStateOf(0L) }
    var lastTick by rememberSaveable { mutableLongStateOf(0L) }
    val gameboardViewScrollState = rememberScrollState()

    val goToNextQuestion: () -> Unit = {
        if (uiState == GamePlayUIState.TIMEOUT) {
            val firstWrongAnswer =
                questions[currentQuestionIndex].answers.firstOrNull {
                    !it.isCorrect
                }
            questions[currentQuestionIndex].userSelectedAnswer = firstWrongAnswer
            questions[currentQuestionIndex].responseTime = System.currentTimeMillis() - cTime
        }
        if (questions[currentQuestionIndex].userSelectedAnswer != null) {
            if (currentQuestionIndex + 1 >= questions.size) {
                uiState = GamePlayUIState.ENDING
                onShowResult(questions)
            } else {
                currentQuestionIndex += 1
                selectedAnswerChoice = null
                currentTick = 0L
                cTime = System.currentTimeMillis()
                coroutine.launch {
                    gameboardViewScrollState.scrollTo(0)
                }
                uiState = GamePlayUIState.IN_PROGRESS
            }
        }
    }

    LaunchedEffect(Unit) {
        appContext.showCloseScreenButton.value = true
        coroutine.launch {
            EventBus.subscribe<AppEvent> {
                when (it) {
                    AppEvent.CLOSE_SCREEN -> {
                        uiState = GamePlayUIState.CANCELING
                    }

                    AppEvent.NEXT_QUESTION -> {
                        goToNextQuestion()
                    }

                    else -> {}
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            uiState = GamePlayUIState.IN_PROGRESS
            appContext.showCloseScreenButton.value = false
        }
    }

    if (appContext.hasTimeout.value && uiState == GamePlayUIState.IN_PROGRESS) {
        TimerTicks(initTick = currentTick, interval = 100L) { tick ->
            lastTick = tick
            currentTimeoutProgression = (tick / appContext.maxTimeout.value.toFloat())
            if (currentTimeoutProgression > 1f) {
                uiState = GamePlayUIState.TIMEOUT
            }
        }
    }

    BackHandler(backHandlingEnabled) {
        uiState = GamePlayUIState.CANCELING
    }

    if (uiState == GamePlayUIState.TIMEOUT) {
        TimerTicks {
            if (it >= 2_000L) {
                if (uiState != GamePlayUIState.COMPLETED) {
                    goToNextQuestion()
                }
            }
        }
        GamePlayTimeoutView(
            modifier = Modifier.fillMaxSize(),
            appContext = appContext
        )
    } else {
        Box(contentAlignment = Alignment.BottomCenter) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                GamePlayBoardContentView(
                    appContext = appContext,
                    scrollState = gameboardViewScrollState,
                    questions = questions,
                    selectedAnswerChoice = selectedAnswerChoice,
                    currentQuestionIndex = currentQuestionIndex,
                    onSelectAnswerChoice = { selected ->
                        if (selectedAnswerChoice != null) {
                            return@GamePlayBoardContentView
                        }
                        selectedAnswerChoice = selected
                        questions[currentQuestionIndex].responseTime =
                            System.currentTimeMillis() - cTime
                        questions[currentQuestionIndex].userSelectedAnswer = selected
                        coroutine.launch {
                            uiState =
                                if (selected.isCorrect) {
                                    GamePlayUIState.SUCCESS
                                } else {
                                    GamePlayUIState.COMPLETED
                                }
                        }
                    },
                    progression = currentTimeoutProgression,
                    onNextQuestion = goToNextQuestion,
                    onShowDefinition = onShowDefinition
                )
            }
            if (uiState == GamePlayUIState.SUCCESS) {
                TimerTicks(interval = 1_000L) {
                    if (uiState == GamePlayUIState.SUCCESS && it >= 2_000L) {
                        uiState = GamePlayUIState.COMPLETED
                        goToNextQuestion()
                    }
                }
                ResultBannerView(
                    appContext = appContext,
                    modifier = Modifier,
                    isValid = true
                ) {
                    if (uiState != GamePlayUIState.COMPLETED) {
                        uiState = GamePlayUIState.COMPLETED
                        goToNextQuestion()
                    }
                }
            }
        }
        if (uiState == GamePlayUIState.CANCELING) {
            GamePlayCancelGameView(
                appContext = appContext,
                modifier = Modifier.fillMaxSize(),
                onCancel = {
                    uiState = GamePlayUIState.IN_PROGRESS
                    currentTick = lastTick
                },
                onValid = {
                    onCancelGame()
                    uiState = GamePlayUIState.IN_PROGRESS
                }
            )
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun GameBoardWithBannerPreview() {
    val questions = listOf(QuestionChoice.dummy, QuestionChoice.dummy, QuestionChoice.dummy)
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayBoardView(
                appContext = AppContext(mUsername = "TEST SCREEN"),
                questions = questions,
                onShowResult = {},
                currentState = GamePlayUIState.IN_PROGRESS,
                onCancelGame = {},
                onShowDefinition = {}
            )
        }
    }
}
