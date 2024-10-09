package com.cacd2.cacdgame.android.features.lexique

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.search.SearchLexicalScreen
import com.cacd2.cacdgame.model.HistoryData
import com.cacd2.cacdgame.model.QuestionChoice
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
@Composable
fun LexicalScreen(
    appContext: AppContext = koinInject(),
    onSelectQuestionId: (String) -> Unit,
    testQuestions: List<QuestionChoice>? = null
) {
    SearchLexicalScreen(
        modifier = Modifier.fillMaxSize(),
        appContext = appContext,
        onSelectQuestionId = onSelectQuestionId
    )
}

@Preview
@Composable
fun LexicalPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LexicalScreen(
                appContext = AppContext(),
                testQuestions = HistoryData.dummy.data,
                onSelectQuestionId = {}
            )
        }
    }
}
