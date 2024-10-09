package com.cacd2.cacdgame.matomo

import android.content.Context
import com.cacd2.cacdgame.KContext
import com.cacd2.cacdgame.tools.logger.AppLogger
import org.matomo.sdk.Matomo
import org.matomo.sdk.Tracker
import org.matomo.sdk.TrackerBuilder
import org.matomo.sdk.extra.TrackHelper

actual class MatomoTracker actual constructor(url: String, siteId: Int) {

    private val tracker: Tracker

    init {
        AppLogger.i("Matomo.setup")
        tracker = TrackerBuilder.createDefault(url, siteId).build(
            Matomo.getInstance(KContext as Context)
        )
    }

    actual fun trackView(
        path: Array<String>,
        title: String?,
        dimensions: Map<TrackerDimension, String>?
    ) {
        TrackHelper.track()
            .screen(path.joinToString("/", "/"))
            .also {
                if (title != null) {
                    it.title(title)
                }
            }
            .also { event ->
                dimensions?.forEach { dimension ->
                    event.dimension(dimension.key.id, dimension.value)
                }
            }
            .with(
                tracker
            )
    }

    actual fun trackEvent(
        category: String,
        action: String,
        name: String?,
        dimensions: Map<TrackerDimension, String>?
    ) {
        TrackHelper.track()
            .also { event ->
                dimensions?.forEach { dimension ->
                    event.dimension(dimension.key.id, dimension.value)
                }
            }
            .event(category, action)
            .also {
                if (name != null) {
                    it.name(name)
                }
            }
            .with(
                tracker
            )
    }

    actual fun trackInstall() {
        TrackHelper.track().download().with(tracker)
    }

    actual fun toggle(isEnabled: Boolean) {
        tracker.isOptOut = !isEnabled
    }
}
