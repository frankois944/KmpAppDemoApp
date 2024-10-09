package com.cacd2.cacdgame.android.features.gameplay.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.hasTimeout
import com.cacd2.cacdgame.android.ui.NavigationFooterView
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.QuestionChoice
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 19/06/2023.
 */
@Composable
fun GamePlayBoardContentView(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    scrollState: ScrollState = rememberScrollState(),
    questions: List<QuestionChoice>,
    currentQuestionIndex: Int,
    selectedAnswerChoice: AnswerChoice?,
    onSelectAnswerChoice: (AnswerChoice) -> Unit,
    onShowDefinition: (QuestionChoice) -> Unit,
    onNextQuestion: () -> Unit,
    progression: Float
) {
    val currentQuestion = questions[currentQuestionIndex]
    val coroutineContext = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        GamePlayProgressView(
            modifier = Modifier.padding(top = 8.dp),
            currentQuestion = currentQuestionIndex + 1,
            totalQuestion = questions.size
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
        ) {
            if (appContext.hasTimeout.value) {
                TimeoutBackgroundView(
                    modifier = Modifier.fillMaxSize(),
                    progression = progression
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                Modifier.fillMaxHeight().verticalScroll(scrollState).height(
                    IntrinsicSize.Max
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = currentQuestion.content,
                        style =
                        TextStyle(
                            fontFamily = fonts,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth(0.95f).padding(top = 16.dp)
                    )
                    GamePlayAnswerSelectorView(
                        selectedAnswerChoice = selectedAnswerChoice,
                        answers = currentQuestion.answers,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        coroutineContext.launch {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                        onSelectAnswerChoice(it)
                    }
                    Button(
                        onClick = {
                            onShowDefinition(questions[currentQuestionIndex])
                        },
                        modifier =
                        Modifier.alpha(
                            if (selectedAnswerChoice?.isCorrect == false) {
                                1f
                            } else {
                                0f
                            }
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = R.string.see_definition).uppercase()
                            )
                        }
                    }
                    NavigationFooterView(
                        modifier =
                        Modifier.fillMaxWidth().alpha(
                            if (selectedAnswerChoice != null && !selectedAnswerChoice.isCorrect) {
                                1f
                            } else {
                                0f
                            }
                        ),
                        onGoForward = {
                            onNextQuestion()
                        }
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun GameBoardWithOKAnswerPreview() {
    val selectedAnswerChoice =
        remember {
            mutableStateOf<AnswerChoice?>(QuestionChoice.dummy.answers[1])
        }
    val currentQuestionIndex = remember { mutableIntStateOf(0) }

    val questions = listOf(QuestionChoice.dummy, QuestionChoice.dummy, QuestionChoice.dummy)
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayBoardContentView(
                questions = questions,
                selectedAnswerChoice = selectedAnswerChoice.value,
                currentQuestionIndex = currentQuestionIndex.value,
                onSelectAnswerChoice = { selectedAnswerChoice.value = it },
                progression = 0.50f,
                appContext = AppContext(),
                onShowDefinition = {},
                onNextQuestion = {}
            )
        }
    }
}

@Preview(device = "id:Nexus One")
@Composable
fun GameBoardWithKOAnswerPreview() {
    val selectedAnswerChoice =
        remember {
            mutableStateOf<AnswerChoice?>(QuestionChoice.dummy.answers[0])
        }
    val currentQuestionIndex = remember { mutableIntStateOf(0) }

    val questions = listOf(QuestionChoice.dummy, QuestionChoice.dummy, QuestionChoice.dummy)
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayBoardContentView(
                questions = questions,
                selectedAnswerChoice = selectedAnswerChoice.value,
                currentQuestionIndex = currentQuestionIndex.value,
                onSelectAnswerChoice = { selectedAnswerChoice.value = it },
                progression = 0f,
                appContext = AppContext(),
                onShowDefinition = {},
                onNextQuestion = {}
            )
        }
    }
}
