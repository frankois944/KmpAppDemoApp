package com.cacd2.cacdgame.android.features.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.history.ui.GameHistoryView
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.model.HistoryData
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 12/06/2023.
 */
@Composable
fun HomeScreen(
    appContext: AppContext = koinInject(),
    testQuestions: List<HistoryData>? = null,
    onSelectionHistory: (HistoryData) -> Unit,
    onGoToNewGame: () -> Unit
) {
    CommonCardBackgroundView(modifier = Modifier.fillMaxWidth()) {
        GameHistoryView(
            modifier =
            Modifier.fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top)
                .fillMaxSize(),
            testQuestions = testQuestions,
            onSelectionHistory = onSelectionHistory
        )
    }
}

@Preview(device = "id:pixel_4", showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                appContext = AppContext(),
                testQuestions =
                listOf(
                    HistoryData.dummy,
                    HistoryData.dummy,
                    HistoryData.dummy,
                    HistoryData.dummy
                ),
                onSelectionHistory = {},
                onGoToNewGame = {}
            )
        }
    }
}
