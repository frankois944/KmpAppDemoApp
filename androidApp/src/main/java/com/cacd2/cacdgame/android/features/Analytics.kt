package com.cacd2.cacdgame.android.features

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalInspectionMode
import com.cacd2.cacdgame.matomo.Tracker
import com.cacd2.cacdgame.matomo.TrackerDimension
import com.cacd2.cacdgame.tools.logger.AppLogger
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by francois.dabonot@cacd2.fr on 09/06/2023.
 */
@SuppressLint("ComposableNaming")
@Composable
fun trackScreenView(name: String, extra: Map<String, String>? = null) {
    if (!LocalInspectionMode.current) {
        DisposableEffect(Unit) {
            AppLogger.d("ENTER: $name")
            Tracker.instance.trackView(
                arrayOf(name),
                title = name,
                dimensions =
                extra?.mapNotNull { row ->
                    val dimension = TrackerDimension.fromStringKey(row.key)
                    dimension?.let { current ->
                        Pair(current, row.value)
                    } ?: run {
                        null
                    }
                }?.toMap()
            )
            onDispose {
                // This block will be called when the effect is disposed
                AppLogger.d("EXIT: $name")
            }
        }
    }
}

fun trackEvent(name: String, extra: Map<String, String>? = null) {
    Tracker.instance.trackEvent(
        FirebaseAnalytics.Event.SELECT_CONTENT,
        FirebaseAnalytics.Param.CONTENT,
        name,
        dimensions =
        extra?.mapNotNull { row ->
            val dimension = TrackerDimension.fromStringKey(row.key)
            dimension?.let { current ->
                Pair(current, row.value)
            } ?: run {
                null
            }
        }?.toMap()
    )
}
