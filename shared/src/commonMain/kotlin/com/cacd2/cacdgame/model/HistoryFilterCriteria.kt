package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.CommonParcelable
import com.cacd2.cacdgame.CommonParcelize
import kotlinx.serialization.Serializable

/**
 * Created by francois.dabonot@cacd2.fr on 07/08/2023.
 */
@Serializable
@CommonParcelize
data class HistoryFilterCriteria(
    var designSelected: Boolean = true,
    var techSelected: Boolean = true,
    var productSelected: Boolean = true,
    var mixSelected: Boolean = true
) : CommonParcelable {
    fun buildCategoryIdList(): List<String> {
        val result = mutableListOf<String>()
        if (designSelected) {
            result.add(GameChoice.DESIGN.gameId)
        }
        if (techSelected) {
            result.add(GameChoice.TECH.gameId)
        }
        if (productSelected) {
            result.add(GameChoice.PRODUCT.gameId)
        }
        if (mixSelected) {
            result.add(GameChoice.ALL.gameId)
        }
        return result
    }
}
