package com.cacd2.cacdgame.android.features.gameresult.ui

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R

@Composable
fun ResultAnimationView(modifier: Modifier = Modifier, onCompletion: () -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.result_animation)
    )
    val progress by animateLottieCompositionAsState(
        composition
    )
    LaunchedEffect(progress) {
        if (progress >= 1f) {
            onCompletion()
        }
    }
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        contentScale = ContentScale.FillBounds,
        progress = { progress }
    )
}

@Preview
@Composable
fun ResultAnimationPreview() {
    AppTheme {
        Surface {
            ResultAnimationView(onCompletion = {})
        }
    }
}
