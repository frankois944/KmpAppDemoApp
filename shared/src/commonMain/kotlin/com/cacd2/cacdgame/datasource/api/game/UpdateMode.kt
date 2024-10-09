package com.cacd2.cacdgame.datasource.api.game

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 09/08/2023.
 */
@Serializable
@CommonParcelize
enum class UpdateMode : CommonParcelable {
    FULL,
    SILENT,
    NOTNEEDED
}
