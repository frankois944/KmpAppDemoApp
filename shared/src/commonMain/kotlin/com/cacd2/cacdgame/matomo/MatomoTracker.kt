package com.cacd2.cacdgame.matomo

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

@Serializable
@CommonParcelize
enum class TrackerDimension(val id: Int, val keyString: String) : CommonParcelable {
    QUESTION_ID(1, "questionId"),
    GAME_DIFFICULTY(2, "gamedifficulty"),
    GAME(3, "game"),
    QUESTION_FILTER_TYPE(4, "type")
    ;

    companion object {
        fun fromStringKey(key: String): TrackerDimension? {
            return entries.firstOrNull { it.keyString == key }
        }
    }
}

object Tracker {
    val instance = MatomoTracker("https://cacd2.matomo.cloud/matomo.php", 3)
}

expect class MatomoTracker(url: String, siteId: Int) {
    fun trackView(
        path: Array<String>,
        title: String? = null,
        dimensions: Map<TrackerDimension, String>? = null
    )

    fun trackEvent(
        category: String,
        action: String,
        name: String? = null,
        dimensions: Map<TrackerDimension, String>? = null
    )

    fun trackInstall()

    fun toggle(isEnabled: Boolean)
}
