package com.cacd2.cacdgame.android.features.gamedifficulty.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.model.Difficulty

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameDifficultySelectionView(
    modifier: Modifier = Modifier,
    selectedDifficulty: Difficulty?,
    onSelectDifficulty: (Difficulty) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        GameDifficultyChoiceView(
            modifier = Modifier.padding(start = 14.dp, end = 60.dp),
            title = stringResource(id = R.string.level_1),
            content = stringResource(id = R.string.level_novice),
            image = painterResource(id = R.drawable.niveau1),
            selectedImage = painterResource(id = R.drawable.lvl1_selected),
            isSelected = selectedDifficulty == Difficulty.EASY,
            onSelection = { onSelectDifficulty(Difficulty.EASY) }
        )
        GameDifficultyChoiceView(
            modifier = Modifier.padding(start = 30.dp, end = 50.dp),
            title = stringResource(id = R.string.level_2),
            content = stringResource(id = R.string.level_confirm),
            image = painterResource(id = R.drawable.niveau2),
            selectedImage = painterResource(id = R.drawable.lvl2_selected),
            isSelected = selectedDifficulty == Difficulty.MEDIUM,
            onSelection = { onSelectDifficulty(Difficulty.MEDIUM) }
        )
        GameDifficultyChoiceView(
            modifier = Modifier.padding(start = 50.dp, end = 30.dp),
            title = stringResource(id = R.string.level_3),
            content = stringResource(id = R.string.level_expert),
            image = painterResource(id = R.drawable.niveau3),
            selectedImage = painterResource(id = R.drawable.lvl3_selected),
            isSelected = selectedDifficulty == Difficulty.HARD,
            onSelection = { onSelectDifficulty(Difficulty.HARD) }
        )
    }
}

@Preview(device = "id:Nexus 4")
@Composable
fun GameDifficultySelectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            GameDifficultySelectionView(selectedDifficulty = Difficulty.HARD) {}
        }
    }
}
