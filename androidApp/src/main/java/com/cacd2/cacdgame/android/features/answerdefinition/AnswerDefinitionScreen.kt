package com.cacd2.cacdgame.android.features.answerdefinition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.answerdefinition.ui.AnswerDefinitionContentView
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
@Composable
fun AnswerDefinitionScreen(
    questionId: String,
    appContext: AppContext = koinInject(),
    testQuestion: QuestionChoice? = null,
    nextQuestion: (() -> Unit)?
) {
    var question by rememberSaveable { mutableStateOf(testQuestion) }
    LaunchedEffect(Unit) {
        if (question == null) {
            question = GameChoice.getQuestion(questionId)
        }
    }
    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        question?.let {
            Box(contentAlignment = Alignment.BottomCenter) {
                AnswerDefinitionContentView(
                    questionChoice = it,
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            start = 16.dp,
                            top = 10.dp,
                            end = 16.dp,
                            bottom = if (nextQuestion == null) 16.dp else 80.dp
                        )
                )
                nextQuestion?.let {
                    Button(
                        onClick = it,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = stringResource(id = R.string.next_question).uppercase())
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AnswerDefinitionPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AnswerDefinitionScreen(
                questionId = "1",
                appContext = AppContext(),
                testQuestion =
                QuestionChoice.dummy.copy(
                    detail = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    illustration = "https://www.datocms-assets.com/98442/1694790941-sdk.png"
                ),
                nextQuestion = null
            )
        }
    }
}

@Preview
@Composable
fun AnswerDefinitionFromQuestionPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AnswerDefinitionScreen(
                questionId = "1",
                appContext = AppContext(),
                testQuestion =
                QuestionChoice.dummy.copy(
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    detail = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                ),
                nextQuestion = { }
            )
        }
    }
}
