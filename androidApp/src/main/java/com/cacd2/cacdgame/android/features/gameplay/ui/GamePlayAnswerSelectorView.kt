package com.cacd2.cacdgame.android.features.gameplay.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.QuestionChoice

/**
 * Created by frankois on 15/05/2023.
 */
@Composable
fun GamePlayAnswerSelectorView(
    modifier: Modifier = Modifier,
    answers: List<AnswerChoice>,
    selectedAnswerChoice: AnswerChoice?,
    onSelectAnswer: (AnswerChoice) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GamePlayAnswerItemView(
                answer = answers[0],
                selectedAnswerChoice = selectedAnswerChoice,
                onSelectAnswer = onSelectAnswer,
                modifier = Modifier.weight(1f)
            )
            GamePlayAnswerItemView(
                answer = answers[1],
                selectedAnswerChoice = selectedAnswerChoice,
                onSelectAnswer = onSelectAnswer,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GamePlayAnswerItemView(
                answer = answers[2],
                selectedAnswerChoice = selectedAnswerChoice,
                onSelectAnswer = onSelectAnswer,
                modifier = Modifier.weight(1f)
            )
            GamePlayAnswerItemView(
                answer = answers[3],
                selectedAnswerChoice = selectedAnswerChoice,
                onSelectAnswer = onSelectAnswer,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun GamePlayAnswerSelectorPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayAnswerSelectorView(
                answers = QuestionChoice.dummy.answers,
                onSelectAnswer = {
                },
                selectedAnswerChoice = null
            )
        }
    }
}
