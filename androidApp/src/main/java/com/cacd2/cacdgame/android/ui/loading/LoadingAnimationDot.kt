package com.cacd2.cacdgame.android.ui.loading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import kotlinx.coroutines.delay

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
@Composable
fun LoadingAnimationDotView(
    circleColor: Color = MaterialTheme.colors.primary,
    circleSize: Dp = 36.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {
    // 3 circles
    val circles =
        listOf(
            remember {
                Animatable(initialValue = initialAlpha)
            },
            remember {
                Animatable(initialValue = initialAlpha)
            },
            remember {
                Animatable(initialValue = initialAlpha)
            }
        )

    circles.forEachIndexed { index, animatable ->

        LaunchedEffect(Unit) {
            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec =
                infiniteRepeatable(
                    animation =
                    tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // container for circles
    Row(
        modifier = Modifier
        // .border(width = 2.dp, color = Color.Magenta)
    ) {
        // adding each circle
        circles.forEachIndexed { index, animatable ->

            // gap between the circles
            if (index != 0) {
                Spacer(modifier = Modifier.width(width = 6.dp))
            }

            Box(
                modifier =
                Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(
                        color =
                        circleColor
                            .copy(alpha = animatable.value)
                    )
            ) {
            }
        }
    }
}

@Preview
@Composable
fun LoadingAnimationDotPreview() {
    AppTheme {
        LoadingAnimationDotView()
    }
}
