package com.cacd2.cacdgame.model

/**
 * Created by francois.dabonot@cacd2.fr on 21/06/2023.
 */
data class SearchLexicalContext(
    var text: String = "",
    var productSelected: Boolean = true,
    var designSelected: Boolean = true,
    var techSelected: Boolean = true
)
