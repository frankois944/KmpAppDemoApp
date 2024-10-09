package com.cacd2.cacdgame.android.features.gameselection.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.model.GameChoice

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
@Composable
fun GameSelectionSelectorView(
    modifier: Modifier = Modifier,
    selectedGame: GameChoice?,
    onSelectGame: (GameChoice) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GameSelectionItemView(
                choice = GameChoice.DESIGN,
                selectedGame = selectedGame,
                onSelectionChoice = onSelectGame,
                modifier = Modifier.weight(1f)
            )
            GameSelectionItemView(
                choice = GameChoice.PRODUCT,
                selectedGame = selectedGame,
                onSelectionChoice = onSelectGame,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GameSelectionItemView(
                choice = GameChoice.TECH,
                selectedGame = selectedGame,
                onSelectionChoice = onSelectGame,
                modifier = Modifier.weight(1f)
            )
            GameSelectionItemView(
                choice = GameChoice.ALL,
                selectedGame = selectedGame,
                onSelectionChoice = onSelectGame,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun GameSelectionSelectorPreview() {
    AppTheme {
        Surface {
            GameSelectionSelectorView(selectedGame = GameChoice.DESIGN) {}
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GameSelectionSelectorDarkPreview() {
    AppTheme {
        Surface {
            GameSelectionSelectorView(selectedGame = GameChoice.DESIGN) {}
        }
    }
}
