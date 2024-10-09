package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 06/06/2023.
 */
@Serializable
@CommonParcelize
enum class Difficulty(val id: String) : CommonParcelable {
    EASY("Débutant"),
    MEDIUM("Confirmé"),
    HARD("Incollable");

    companion object {
        fun fromId(id: String): Difficulty {
            return entries.first { it.id == id }
        }
    }
}
