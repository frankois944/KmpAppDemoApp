package com.cacd2.cacdgame.android.features.search

import com.cacd2.cacdgame.CommonParcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterCondition(
    val criteria: String?,
    val design: Boolean,
    val product: Boolean,
    val tech: Boolean
) : CommonParcelable
