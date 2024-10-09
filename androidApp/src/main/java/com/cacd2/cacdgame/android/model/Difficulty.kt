package com.cacd2.cacdgame.android.model

import androidx.annotation.StringRes
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.model.Difficulty

/**
 * Created by francois.dabonot@cacd2.fr on 06/06/2023.
 */

@get:StringRes
val Difficulty.label: Int
    get() =
        when (this) {
            Difficulty.EASY -> R.string.game_niveau_1_debutant
            Difficulty.MEDIUM -> R.string.game_niveau_2_confirme
            Difficulty.HARD -> R.string.game_niveau_3_incollable
        }

@get:StringRes
val Difficulty.shortname: Int
    get() =
        when (this) {
            Difficulty.EASY -> R.string.level_novice_name
            Difficulty.MEDIUM -> R.string.level_confirm_name
            Difficulty.HARD -> R.string.level_expert_name
        }
