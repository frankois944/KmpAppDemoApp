package com.cacd2.cacdgame.android.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.model.GameChoice

/**
 * Created by frankois on 15/05/2023.
 */
@DrawableRes
fun GameChoice.image(selectedGameId: GameChoice?): Int {
    val isSelected = selectedGameId?.gameId == this.gameId
    return when (this) {
        GameChoice.DESIGN -> {
            if (isSelected) {
                R.drawable.design_selected
            } else {
                R.drawable.design_interface_img
            }
        }

        GameChoice.PRODUCT -> {
            if (isSelected) {
                R.drawable.product_selected
            } else {
                R.drawable.product_img
            }
        }

        GameChoice.TECH -> {
            if (isSelected) {
                R.drawable.tech_selected
            } else {
                R.drawable.tech_img
            }
        }

        GameChoice.ALL -> {
            if (isSelected) {
                R.drawable.mix_selected
            } else {
                R.drawable.mix
            }
        }
    }
}

val GameChoice.title: Int
    @StringRes
    get() =
        when (this) {
            GameChoice.DESIGN -> {
                R.string.selection_quiz_design
            }

            GameChoice.PRODUCT -> {
                R.string.selection_quiz_produit
            }

            GameChoice.TECH -> {
                R.string.selection_quiz_tech
            }

            GameChoice.ALL -> {
                R.string.selection_mix
            }
        }

val GameChoice.label: Int
    @StringRes
    get() =
        when (this) {
            GameChoice.DESIGN -> {
                R.string.label_design
            }

            GameChoice.PRODUCT -> {
                R.string.label_produit
            }

            GameChoice.TECH -> {
                R.string.label_tech
            }

            GameChoice.ALL -> {
                R.string.label_mix
            }
        }

@Composable
fun GameChoice.color(selectedGameId: GameChoice?): Color {
    val isSelected = selectedGameId?.gameId == this.gameId
    return if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface
    }
}
