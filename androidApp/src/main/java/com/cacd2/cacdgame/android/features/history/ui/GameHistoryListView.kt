package com.cacd2.cacdgame.android.features.history.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.model.HistoryData

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameHistoryListView(
    modifier: Modifier = Modifier,
    items: List<HistoryData>,
    onSelectRow: (history: HistoryData) -> Unit
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 72.dp)) {
        items(items) { item ->
            GameHistoryItemView(
                item = item,
                modifier =
                Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 10.dp
                ),
                onSelectItem = {
                    onSelectRow(it)
                }
            )
        }
    }
}

@Preview
@Composable
fun GameHistoryListPreview() {
    AppTheme {
        Surface {
            GameHistoryListView(items = listOf(HistoryData.dummy, HistoryData.dummy)) {}
        }
    }
}
