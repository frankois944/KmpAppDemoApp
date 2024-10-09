package com.cacd2.cacdgame.android.ui

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@Composable
fun LoadingAnimationView(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress }
    )
}

@Preview
@Composable
fun LoadingAnimationPreview() {
    AppTheme {
        Surface {
            LoadingAnimationView()
        }
    }
}
