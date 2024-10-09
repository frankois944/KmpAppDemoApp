package com.cacd2.cacdgame.android.features.gameplay.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.username
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 02/06/2023.
 */
@Composable
fun GamePlayTimeoutView(modifier: Modifier = Modifier, appContext: AppContext = koinInject()) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =
                stringResource(
                    id = R.string.timeout_message,
                    formatArgs = arrayOf(appContext.username.value ?: "DEBUG")
                ),
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 37.sp
                ),
                textAlign = TextAlign.Center
            )
            Image(painter = painterResource(id = R.drawable.time_out), contentDescription = null)
        }
    }
}

@Preview
@Composable
fun GamePlayTimeoutPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayTimeoutView(appContext = AppContext(mUsername = "toto"))
        }
    }
}
