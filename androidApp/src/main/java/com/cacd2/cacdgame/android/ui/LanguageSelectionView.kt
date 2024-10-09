package com.cacd2.cacdgame.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.currentLanguageCode
import com.cacd2.cacdgame.android.features.trackEvent
import com.cacd2.cacdgame.android.model.getLanguageLabelFoLocal
import com.cacd2.cacdgame.android.save
import com.cacd2.cacdgame.model.Language
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 30/06/2023.
 */
@Composable
fun LanguageSelectionView(
    modifier: Modifier = Modifier,
    leftIcnSpace: Dp = 30.dp,
    appContext: AppContext = koinInject()
) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        TextButton(onClick = { expanded = true }, modifier = Modifier) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(leftIcnSpace),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.language),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Text(
                        text =
                        stringResource(
                            id = R.string.current_selected_lang,
                            getLanguageLabelFoLocal(appContext.currentLanguageCode.value)
                        ),
                        style =
                        MaterialTheme.typography.subtitle1.apply {
                            PlatformTextStyle(
                                includeFontPadding = false
                            )
                        },
                        color = MaterialTheme.colors.onSurface
                    )
                }
                if (expanded) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Ouvrir Choix de la langue",
                        tint = MaterialTheme.colors.onSurface
                    )
                } else {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Fermer Choix de la langue",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                coroutineScope.launch {
                    delay(250L)
                    trackEvent("Change language for French")
                    appContext.currentLanguageCode.value = Language.FRENCH
                    save()
                }
            }) {
                Text(
                    stringResource(id = R.string.french_lang_selector),
                    modifier = Modifier,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            DropdownMenuItem(onClick = {
                expanded = false
                coroutineScope.launch {
                    delay(250L)
                    trackEvent("Change language for English")
                    appContext.currentLanguageCode.value = Language.ENGLISH
                    save()
                }
            }) {
                Text(
                    stringResource(id = R.string.english_lang_selector),
                    modifier = Modifier,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Preview(locale = "fr")
@Composable
fun LanguageSelectionFRPreview() {
    AppTheme {
        Surface {
            LanguageSelectionView(appContext = AppContext())
        }
    }
}

@Preview
@Composable
fun LanguageSelectionENPreview() {
    AppTheme {
        Surface {
            LanguageSelectionView(appContext = AppContext())
        }
    }
}
