package com.cacd2.cacdgame

import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
@Serializable
@CommonParcelize
enum class AppEvent : CommonParcelable {
    SHARE_CONTENT,
    CLOSE_SCREEN,
    NEXT_QUESTION,
    TOGGLE_SEARCH_LEXICAL,
    TOGGLE_FILTER_HISTORY
}
