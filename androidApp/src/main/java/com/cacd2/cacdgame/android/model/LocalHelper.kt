package com.cacd2.cacdgame.android.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.model.Language

/**
 * Created by francois.dabonot@cacd2.fr on 30/06/2023.
 */
@Composable
@ReadOnlyComposable
fun getLanguageLabelFoLocal(lang: Language): String {
    return if (lang == Language.FRENCH) {
        stringResource(id = R.string.french_lang_selector)
    } else {
        stringResource(id = R.string.english_lang_selector)
    }
}
