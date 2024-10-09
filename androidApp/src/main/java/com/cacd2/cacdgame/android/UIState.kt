package com.cacd2.cacdgame.android

import androidx.compose.runtime.Stable

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@Stable
sealed interface UIState<out T> {
    data class Success<T>(val data: T) : UIState<T>

    data class Error(val exception: Throwable? = null) : UIState<Nothing>

    data object Loading : UIState<Nothing>
}
