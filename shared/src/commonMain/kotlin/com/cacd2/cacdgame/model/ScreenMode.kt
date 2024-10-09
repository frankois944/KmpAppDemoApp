package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 20/06/2023.
 */
@Serializable
@CommonParcelize
enum class ScreenMode(val id: String) : CommonParcelable {
    SYSTEM("0"),
    LIGHT("1"),
    DARK("2")
    ;

    companion object {
        fun fromId(id: String?): ScreenMode {
            return entries.firstOrNull { it.id == id } ?: SYSTEM
        }
    }
}
