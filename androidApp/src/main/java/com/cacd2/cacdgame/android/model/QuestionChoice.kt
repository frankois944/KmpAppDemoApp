package com.cacd2.cacdgame.android.model

import androidx.annotation.DrawableRes
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice

/**
 * Created by francois.dabonot@cacd2.fr on 14/06/2023.
 */

val QuestionChoice.image: Int
    @DrawableRes
    get() =
        when (this.game) {
            GameChoice.DESIGN -> R.drawable.rectangle_1
            GameChoice.PRODUCT -> R.drawable.rectangle_2
            GameChoice.TECH -> R.drawable.rectangle_3
            GameChoice.ALL -> R.drawable.rectangle_2
        }
