package com.cacd2.cacdgame.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by francois.dabonot@cacd2.fr on 10/07/2023.
 */
@Composable
fun BottomShadow(alpha: Float = 0.1f, height: Dp = 8.dp) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush =
                Brush.verticalGradient(
                    colors =
                    listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Transparent
                    )
                )
            )
    )
}
