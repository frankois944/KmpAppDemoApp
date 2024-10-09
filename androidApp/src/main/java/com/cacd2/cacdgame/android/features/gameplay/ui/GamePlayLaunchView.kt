package com.cacd2.cacdgame.android.features.gameplay.ui

import android.graphics.Typeface
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.username
import com.cacd2.cacdgame.android.utility.TimerTicks
import com.cacd2.cacdgame.android.utility.annotatedStringResource
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 23/05/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GamePlayLaunchView(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    initialSeconds: Int,
    onTimeOut: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier)
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =
                annotatedStringResource(
                    id = R.string.game_launcher_header,
                    formatArgs =
                    arrayOf(
                        appContext.username.value!!
                    ),
                    styles =
                    mapOf(
                        Typeface.ITALIC to
                            SpanStyle(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                    )
                ),
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 37.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            TimerTicks(initTick = 0L) { tick ->
                AnimatedContent(
                    targetState = (tick / 1000).toInt(),
                    transitionSpec = {
                        // Compare the incoming number with the previous number.
                        if (targetState > initialState) {
                            // If the target number is larger, it slides up and fades in
                            // while the initial (smaller) number slides up and fades out.
                            fadeIn() togetherWith fadeOut()
                        } else {
                            // If the target number is smaller, it slides down and fades in
                            // while the initial number slides down and fades out.
                            fadeIn() togetherWith fadeOut()
                        }.using(
                            // Disable clipping since the faded slide-in/out should
                            // be displayed out of bounds.
                            SizeTransform(clip = false)
                        )
                    }
                ) { targetCount ->
                    val current = initialSeconds - targetCount
                    if (current <= 0) {
                        onTimeOut()
                    } else {
                        Text(
                            text = current.toString(),
                            style =
                            TextStyle(
                                fontFamily = fonts,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 96.sp
                            ),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.game_launcher),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Image(
            painter = painterResource(id = R.drawable.game_launcher_footer),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun GamePlayLaunchPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GamePlayLaunchView(initialSeconds = 3, appContext = AppContext(mUsername = "Charly")) {}
        }
    }
}
