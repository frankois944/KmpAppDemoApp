package com.cacd2.cacdgame.android.features.gameselection.ui

import android.content.res.Configuration
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
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
@Composable
fun GameSelectionRuleView(modifier: Modifier = Modifier, username: String) {
    Text(
        text =
        annotatedStringResource(
            id = R.string.quiz_selection_content,
            formatArgs = arrayOf(username),
            styles =
            mapOf(
                Typeface.ITALIC to
                    SpanStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
            )
        ),
        modifier = modifier
    )
}

@Composable
@Preview
fun GameSelectionRulePreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameSelectionRuleView(username = "CYCLMOTOEUR")
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GameSelectionRuleDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameSelectionRuleView(username = "CYCLMOTOEUR")
        }
    }
}
