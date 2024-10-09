package com.cacd2.cacdgame.android.features.gameselection

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.gameselection.ui.GameSelectionRuleView
import com.cacd2.cacdgame.android.features.gameselection.ui.GameSelectionSelectorView
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.ui.NavigationFooterView
import com.cacd2.cacdgame.android.username
import com.cacd2.cacdgame.model.GameChoice
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@Composable
fun GameSelectionScreen(
    appContext: AppContext = koinInject(),
    onGoBack: () -> Unit,
    onGoToGamePlay: (GameChoice) -> Unit,
    currentSelectedGame: GameChoice? = null
) {
    val scrollState = rememberScrollState()
    var selectedGame by rememberSaveable { mutableStateOf(currentSelectedGame) }

    LaunchedEffect(selectedGame) {
        if (selectedGame != null) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier)
            GameSelectionRuleView(
                username = appContext.username.value!!,
                modifier = Modifier.fillMaxWidth(0.75f).padding(top = 16.dp)
            )
            GameSelectionSelectorView(
                selectedGame = selectedGame,
                modifier =
                Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
            ) {
                selectedGame = it
            }
            NavigationFooterView(
                onGoForward = { selectedGame?.let(onGoToGamePlay) },
                modifier =
                Modifier.fillMaxSize().alpha(
                    if (selectedGame != null) {
                        1f
                    } else {
                        0f
                    }
                )
            )
            Spacer(modifier = Modifier)
        }
    }
}

@Preview(
    device = "id:pixel_4",
    showSystemUi = true
)
@Composable
fun GameSelectionPreview() {
    val context = AppContext(mUsername = "CYCLOMO", mAppbarTitle = "Ma selection")
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameSelectionScreen(
                appContext = context,
                onGoBack = {},
                onGoToGamePlay = {}
            )
        }
    }
}

@Preview(
    device = "id:pixel_4",
    showSystemUi = true
)
@Composable
fun GameSelectionPreviewWithFooter() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameSelectionScreen(
                appContext = AppContext(mUsername = "CYCLOMOTEUR", mAppbarTitle = "Ma selection"),
                currentSelectedGame = GameChoice.TECH,
                onGoBack = {},
                onGoToGamePlay = {}
            )
        }
    }
}

@Preview(
    device = "id:pixel_4",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun GameSelectionDarkPreview() {
    val context = AppContext(mUsername = "CYCLOMO", mAppbarTitle = "Ma selection")
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameSelectionScreen(
                appContext = context,
                onGoBack = {},
                onGoToGamePlay = {}
            )
        }
    }
}

@Preview(
    device = "id:pixel_4",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
    locale = "fr"
)
@Composable
fun GameSelectionDarkPreviewWithFooter() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameSelectionScreen(
                appContext = AppContext(mUsername = "CYCLOMOTEUR", mAppbarTitle = "Ma selection"),
                currentSelectedGame = GameChoice.TECH,
                onGoBack = {},
                onGoToGamePlay = {}
            )
        }
    }
}
