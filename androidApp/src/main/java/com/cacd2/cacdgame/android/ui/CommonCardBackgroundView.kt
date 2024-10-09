package com.cacd2.cacdgame.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.android.AppTheme

/**
 * Created by francois.dabonot@cacd2.fr on 22/05/2023.
 */
@Composable
fun CommonCardBackgroundView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier
            /*.padding(top = 8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )*/
    ) {
        content()
    }
}

@Preview
@Composable
fun CommonCardBackgroundPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
                Text(text = "CONTENT")
            }
        }
    }
}
