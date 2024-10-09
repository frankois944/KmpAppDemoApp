package com.cacd2.cacdgame.android.ui

import androidx.annotation.IdRes
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.trackEvent
import com.cacd2.cacdgame.android.save
import com.cacd2.cacdgame.android.screenMode
import com.cacd2.cacdgame.model.ScreenMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 04/07/2023.
 */
@Composable
fun AppThemeSelectionView(modifier: Modifier = Modifier, appContext: AppContext = koinInject()) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    @IdRes
    fun getCurrentThemeLabel(screenMode: ScreenMode): Int {
        return when (screenMode) {
            ScreenMode.SYSTEM -> R.string.setting_theme_system
            ScreenMode.LIGHT -> R.string.setting_theme_light
            ScreenMode.DARK -> R.string.setting_theme_dark
        }
    }

    LaunchedEffect(appContext.screenMode.value) {
        expanded = false
    }

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
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.baseline_color_lens_24),
                        contentDescription = null
                    )
                    Text(
                        text =
                        stringResource(
                            id = R.string.current_selected_theme,
                            stringResource(
                                id = getCurrentThemeLabel(appContext.screenMode.value)
                            )
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
                        contentDescription = "Ouvrir choix du theme",
                        tint = MaterialTheme.colors.onSurface
                    )
                } else {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Fermer choix du theme",
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
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    coroutineScope.launch {
                        delay(250L)
                        trackEvent("Change color theme for System")
                        appContext.screenMode.value = ScreenMode.SYSTEM
                        save()
                    }
                }
            ) {
                Text(
                    stringResource(id = R.string.setting_theme_system),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    coroutineScope.launch {
                        delay(250L)
                        trackEvent("Change color theme for Light")
                        appContext.screenMode.value = ScreenMode.LIGHT
                        save()
                    }
                }
            ) {
                Text(
                    stringResource(id = R.string.setting_theme_light),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    coroutineScope.launch {
                        delay(250L)
                        trackEvent("Change color theme for Dark")
                        appContext.screenMode.value = ScreenMode.DARK
                        save()
                    }
                }
            ) {
                Text(
                    stringResource(id = R.string.setting_theme_dark),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Preview
@Composable
fun AppThemeSelectionPreviewView() {
    AppTheme {
        Surface {
            AppThemeSelectionView()
        }
    }
}
