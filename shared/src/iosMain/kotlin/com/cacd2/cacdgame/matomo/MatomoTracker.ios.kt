package com.cacd2.cacdgame.matomo

import cocoapods.MatomoTracker.removeWithDimensionAtIndex
import cocoapods.MatomoTracker.setDimension
import cocoapods.MatomoTracker.trackWithEventWithCategory
import cocoapods.MatomoTracker.trackWithView
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
actual class MatomoTracker actual constructor(url: String, siteId: Int) {

    private val tracker: cocoapods.MatomoTracker.MatomoTracker

    init {
        AppLogger.i("Matomo.setup")
        tracker = cocoapods.MatomoTracker.MatomoTracker(
            siteId = "$siteId",
            baseURL = NSURL(string = url),
            userAgent = null
        )
    }

    actual fun trackView(
        path: Array<String>,
        title: String?,
        dimensions: Map<TrackerDimension, String>?
    ) {
        addDimensions(dimensions)
        tracker.trackWithView(view = path.toList(), url = null)
        removeDimensions(dimensions)
    }

    actual fun trackEvent(
        category: String,
        action: String,
        name: String?,
        dimensions: Map<TrackerDimension, String>?
    ) {
        addDimensions(dimensions)
        tracker.trackWithEventWithCategory(category, action, name, null)
        removeDimensions(dimensions)
    }

    private fun addDimensions(dimensions: Map<TrackerDimension, String>?) {
        dimensions?.let {
            it.forEach { dimension ->
                tracker.setDimension(dimension.value, dimension.key.id.toLong())
            }
        }
    }

    private fun removeDimensions(dimension: Map<TrackerDimension, String>?) {
        dimension?.let {
            it.forEach { dimension ->
                tracker.removeWithDimensionAtIndex(dimension.key.id.toLong())
            }
        }
    }

    actual fun trackInstall() {
        // NOT AVAILABLE ON IOS
    }

    actual fun toggle(isEnabled: Boolean) {
        tracker.setIsOptedOut(!isEnabled)
    }
}
