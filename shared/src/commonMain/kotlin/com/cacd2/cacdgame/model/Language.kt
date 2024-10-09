package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 05/07/2023.
 */
@Serializable
@CommonParcelize
enum class Language(val code: String) : CommonParcelable {
    FRENCH("fr"),
    ENGLISH("en"),
    UNKNOWN("?")
    ;

    companion object {
        fun fromCode(code: String): Language {
            return entries.firstOrNull { it.code == code } ?: UNKNOWN
        }
    }
}
