package com.cacd2.cacdgame.android.features.history.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.AppEvent
import com.cacd2.cacdgame.EventBus
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.getAllHistory
import com.cacd2.cacdgame.datasource.database.dao.getHistoryCount
import com.cacd2.cacdgame.model.HistoryData
import com.cacd2.cacdgame.model.HistoryFilterCriteria
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameHistoryView(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    testQuestions: List<HistoryData>? = null,
    onSelectionHistory: (HistoryData) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }
    var hasNoHistory by remember { mutableStateOf(true) }
    var filter by rememberSaveable { mutableStateOf(HistoryFilterCriteria()) }
    var latestCriteria by rememberSaveable { mutableStateOf<HistoryFilterCriteria?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var questions by remember { mutableStateOf(testQuestions) }

    /*
    The Flow with SQLdelight is buggy, refreshed in infinite loop not every DB update
    val mQuestions = Database.data.getAllHistoryWithFlow(latestCriteria)
    val questions: List<HistoryData>? by mQuestions.collectAsStateWithLifecycle(
        testQuestions
    )*/

    suspend fun loadContent(forceLoading: Boolean = false) {
        if (questions == null || (forceLoading && testQuestions == null)) {
            questions = testQuestions ?: Database.data.getAllHistory(latestCriteria)
        }
    }

    LaunchedEffect(filter) {
        if (filter != latestCriteria) {
            latestCriteria = filter
            // mQuestions.retry()
            loadContent(forceLoading = true)
        }
    }

    LaunchedEffect(Unit) {
        loadContent()
        hasNoHistory = testQuestions.isNullOrEmpty() == true &&
            Database.data.getHistoryCount() == 0L
        EventBus.subscribe<AppEvent> {
            when (it) {
                AppEvent.TOGGLE_FILTER_HISTORY -> {
                    coroutineScope.launch {
                        filterExpanded = !filterExpanded
                    }
                }
                else -> {}
            }
        }
    }

    if (hasNoHistory) {
        GameHistoryEmptyView(modifier = Modifier.fillMaxSize())
    } else {
        Column(modifier = modifier) {
            Box(modifier = Modifier.fillMaxWidth()) {
                HistoryFilterMenuView(
                    expanded = filterExpanded,
                    filterCriteria = filter,
                    onUpdate = { criteria ->
                        filter = criteria
                    },
                    onDismiss = {
                        filterExpanded = false
                    }
                )
            }
            GameHistoryListView(
                items = questions ?: emptyList(),
                modifier = Modifier.fillMaxSize()
            ) { data ->
                onSelectionHistory(data)
            }
        }
    }
}

@Preview
@Composable
fun GameHistoryPreview() {
    AppTheme {
        Surface {
            GameHistoryView(
                appContext = AppContext(),
                testQuestions =
                listOf(
                    HistoryData.dummy,
                    HistoryData.dummy
                )
            ) {}
        }
    }
}

@Preview
@Composable
fun GameHistoryNoContentPreview() {
    AppTheme {
        Surface {
            GameHistoryView(
                appContext = AppContext(),
                testQuestions = listOf()
            ) {}
        }
    }
}
