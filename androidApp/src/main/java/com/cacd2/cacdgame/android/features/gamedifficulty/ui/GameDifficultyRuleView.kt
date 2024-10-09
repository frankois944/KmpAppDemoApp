package com.cacd2.cacdgame.android.features.gamedifficulty.ui

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.utility.annotatedStringResource

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameDifficultyRuleView(modifier: Modifier = Modifier, username: String) {
    Text(
        text =
        annotatedStringResource(
            id = R.string.difficulty_description,
            styles =
            mapOf(
                Typeface.ITALIC to
                    SpanStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
            ),
            formatArgs = arrayOf(username)
        ),
        modifier = modifier
    )
}

@Preview
@Composable
fun GameDifficultyRulePreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameDifficultyRuleView(username = "CYCLMOTOEUR")
        }
    }
}
