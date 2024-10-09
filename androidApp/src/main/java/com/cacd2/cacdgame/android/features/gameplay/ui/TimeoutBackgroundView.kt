package com.cacd2.cacdgame.android.features.gameplay.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R

/**
 * Created by francois.dabonot@cacd2.fr on 13/06/2023.
 */
@Composable
fun TimeoutBackgroundView(modifier: Modifier = Modifier, progression: Float) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wave))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val constraintSet =
        ConstraintSet {
            val animation = createRefFor("animation")
            val footer = createRefFor("footer")

            constrain(footer) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.percent(progression)
            }

            constrain(animation) {
                bottom.linkTo(footer.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.value(200.dp)
            }
        }

    BoxWithConstraints(modifier = modifier, propagateMinConstraints = true) {
        ConstraintLayout(
            modifier =
            Modifier.fillMaxSize().alpha(
                if (MaterialTheme.colors.isLight) 0.20f else 0.20f
            ),
            constraintSet = constraintSet,
            animateChanges = false
        ) {
            LottieAnimation(
                modifier = Modifier.layoutId("animation"),
                composition = composition,
                progress = { progress },
                alignment = Alignment.BottomCenter
            )
            Surface(
                modifier = Modifier.layoutId("footer"),
                color = MaterialTheme.colors.primary
            ) {}
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun TimeoutBackgroundPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TimeoutBackgroundView(
                modifier = Modifier.fillMaxSize(),
                progression = 1.0f
            )
        }
    }
}
