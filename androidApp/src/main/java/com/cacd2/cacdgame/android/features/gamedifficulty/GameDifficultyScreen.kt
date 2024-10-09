package com.cacd2.cacdgame.android.features.gamedifficulty

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.gamedifficulty.ui.GameDifficultyRuleView
import com.cacd2.cacdgame.android.features.gamedifficulty.ui.GameDifficultySelectionView
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.ui.NavigationFooterView
import com.cacd2.cacdgame.android.username
import com.cacd2.cacdgame.model.Difficulty
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.tools.logger.AppLogger
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameDifficultyScreen(
    appContext: AppContext = koinInject(),
    game: GameChoice,
    onGoBack: () -> Unit,
    onSelectionDifficulty: (game: GameChoice, difficulty: Difficulty) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedDifficulty by rememberSaveable { mutableStateOf<Difficulty?>(null) }
    var canStartGame by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDifficulty) {
        selectedDifficulty?.let {
            if (game.hasQuestions(it)) {
                canStartGame = true
                scrollState.scrollTo(scrollState.maxValue)
            } else {
                AppLogger.e("No Question for category ${game.gameId} and difficulty ${it.id}")
                canStartGame = false
            }
        }
    }

    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            GameDifficultyRuleView(
                username = appContext.username.value!!,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            GameDifficultySelectionView(
                selectedDifficulty = selectedDifficulty,
                modifier =
                Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp
                )
            ) {
                selectedDifficulty = it
            }
            NavigationFooterView(
                onGoForward = { onSelectionDifficulty(game, selectedDifficulty!!) },
                onGoBack = onGoBack,
                modifier =
                Modifier
                    .fillMaxSize()
                    .alpha(
                        if (canStartGame) {
                            1f
                        } else {
                            0f
                        }
                    )
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED,
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GameDifficultyPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameDifficultyScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                game = GameChoice.DESIGN,
                onGoBack = {
                },
                onSelectionDifficulty = { _, _ -> }
            )
        }
    }
}
