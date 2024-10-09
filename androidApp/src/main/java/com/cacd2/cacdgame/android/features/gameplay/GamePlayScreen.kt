package com.cacd2.cacdgame.android.features.gameplay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.gameplay.ui.GamePlayBoardView
import com.cacd2.cacdgame.android.features.gameplay.ui.GamePlayLaunchView
import com.cacd2.cacdgame.android.isAppBarVisible
import com.cacd2.cacdgame.model.Difficulty
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
@Composable
fun GamePlayScreen(
    appContext: AppContext = koinInject(),
    gameChoice: GameChoice,
    gameDifficulty: Difficulty,
    onShowResult: (List<QuestionChoice>) -> Unit,
    onShowDefinition: (QuestionChoice) -> Unit,
    onCancelGame: () -> Unit
) {
    var questions by rememberSaveable { mutableStateOf<List<QuestionChoice>?>(null) }
    var displayGameLoader by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (questions == null) {
            questions = gameChoice.getQuestions(gameDifficulty)
        }
    }

    if (displayGameLoader) {
        appContext.isAppBarVisible.value = false
        GamePlayLaunchView(
            appContext = appContext,
            modifier = Modifier.fillMaxSize(),
            initialSeconds = 3
        ) {
            displayGameLoader = false
        }
    } else {
        questions?.let {
            appContext.isAppBarVisible.value = true
            GamePlayBoardView(
                appContext = appContext,
                questions = it,
                onShowResult = onShowResult,
                onCancelGame = onCancelGame,
                onShowDefinition = onShowDefinition
            )
        }
    }
}

@Preview
@Composable
fun GamePlayPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayScreen(
                appContext = AppContext(mUsername = "DEBUG", mShowCloseScreenButton = true),
                gameChoice = GameChoice.DESIGN,
                gameDifficulty = Difficulty.EASY,
                onShowResult = {},
                onCancelGame = {},
                onShowDefinition = {}
            )
        }
    }
}
