package com.cacd2.cacdgame.android.features.questionlisting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.questionlisting.ui.QuestionListingListView
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.getHistory
import com.cacd2.cacdgame.model.HistoryData
import com.cacd2.cacdgame.model.QuestionChoice
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
@Composable
fun QuestionListingScreen(
    appContext: AppContext = koinInject(),
    historyId: Long,
    isSuccess: Boolean? = null,
    onSelectQuestionId: (String) -> Unit,
    testQuestion: List<QuestionChoice>? = null
) {
    var questions by rememberSaveable { mutableStateOf(testQuestion) }
    var hasNoQuiz by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (questions == null) {
            val content = Database.data.getHistory(historyId)
            questions =
                if (isSuccess != null) {
                    content?.data?.filter {
                        it.userSelectedAnswer?.isCorrect == isSuccess
                    }
                } else {
                    content?.data
                }
        }
    }
    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        questions?.let { items ->
            QuestionListingListView(
                modifier = Modifier.padding(top = 10.dp),
                items = items,
                onSelectQuestionId = {
                    onSelectQuestionId(it)
                }
            )
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun QuestionListingPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            QuestionListingScreen(
                appContext = AppContext(),
                historyId = 1,
                onSelectQuestionId = {},
                testQuestion = HistoryData.dummy.data
            )
        }
    }
}
