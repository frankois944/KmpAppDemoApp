package com.cacd2.cacdgame.android.features.questionlisting.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
@Composable
fun QuestionListingListView(
    modifier: Modifier = Modifier,
    items: List<QuestionChoice>,
    state: LazyListState = rememberLazyListState(),
    onSelectQuestionId: (String) -> Unit
) {
    LazyColumn(modifier = modifier, state = state) {
        items(items = items, key = {
            it.id
        }) { item ->
            QuestionListingRowView(
                modifier =
                Modifier.height(56.dp).clickable {
                    onSelectQuestionId(item.id)
                },
                item = item
            )
            if (item.userSelectedAnswer == null) {
                Divider(startIndent = 40.dp)
            } else {
                Divider(startIndent = 65.dp)
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun QuestionListingListPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            QuestionListingListView(
                onSelectQuestionId = {},
                items =
                listOf(
                    QuestionChoice(
                        id = "1",
                        content = "Une question",
                        detail = null,
                        game = GameChoice.DESIGN,
                        answers = emptyList(),
                        responseTime = 0L,
                        lang = currentLanguage,
                        illustration = null,
                        userSelectedAnswer = AnswerChoice(
                            id = "1",
                            content = "1",
                            isCorrect = false
                        )
                    ),
                    QuestionChoice(
                        id = "2",
                        content = "Une autre question",
                        detail = null,
                        game = GameChoice.DESIGN,
                        answers = emptyList(),
                        responseTime = 0L,
                        lang = currentLanguage,
                        illustration = null,
                        userSelectedAnswer = AnswerChoice(
                            id = "1",
                            content = "1",
                            isCorrect = true
                        )
                    ),
                    QuestionChoice(
                        id = "3",
                        content = "Une autre question",
                        detail = null,
                        game = GameChoice.DESIGN,
                        answers = emptyList(),
                        responseTime = 0L,
                        lang = currentLanguage,
                        illustration = null,
                        userSelectedAnswer = null
                    )
                )
            )
        }
    }
}
