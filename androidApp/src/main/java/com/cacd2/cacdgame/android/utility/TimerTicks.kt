package com.cacd2.cacdgame.android.utility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * Created by francois.dabonot@cacd2.fr on 23/05/2023.
 */
@Composable
fun TimerTicks(
    // must be ms
    initTick: Long = 0L,
    // must be ms
    interval: Long = 1_000L,
    content: @Composable (tickTime: Long) -> Unit
) {
    var ticks by remember(initTick) {
        mutableLongStateOf(initTick)
    }

    content.invoke(ticks)

    LaunchedEffect(ticks) {
        val diff = ticks + interval
        delay(interval)
        ticks = diff
    }
}
