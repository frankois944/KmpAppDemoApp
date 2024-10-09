package com.cacd2.cacdgame.android.features.gameresult.loader

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.utility.TimerTicks
import com.cacd2.cacdgame.android.utility.annotatedStringResource

/**
 * Created by francois.dabonot@cacd2.fr on 02/06/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameResultLoaderView(modifier: Modifier = Modifier, targetSeconds: Int, onTimeOut: () -> Unit) {
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
                    id = R.string.result_loader_message,
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
                    label = "Countdown result",
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
                    val current = targetSeconds - targetCount
                    if (current <= 0) {
                        onTimeOut()
                    } else {
                        Text(
                            text = current.toString(),
                            style =
                            TextStyle(
                                fontFamily = fonts,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 96.sp,
                                lineHeight = 0.sp
                            ),
                            color = MaterialTheme.colors.primary,
                            lineHeight = 10.sp
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.great_idea),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(width = 112.dp, height = 44.dp)
        )
        Spacer(modifier = Modifier)
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun GameResultLoaderPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultLoaderView(targetSeconds = 3) {
            }
        }
    }
}
