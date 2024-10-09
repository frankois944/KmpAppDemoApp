package com.cacd2.cacdgame.android.utility

import androidx.compose.ui.Modifier

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
fun Modifier.conditional(
    condition: Boolean,
    ifTrue: (Modifier.() -> Modifier)? = null,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition && ifTrue != null) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}
