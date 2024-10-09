package com.cacd2.cacdgame.android.features.settings.accessibility

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.screenMode
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.ui.SegmentedControl
import com.cacd2.cacdgame.model.ScreenMode
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 20/06/2023.
 */
@Composable
fun AccessibilityScreen(
    appContext: AppContext = koinInject(),
    onUpdateScreenMode: (ScreenMode) -> Unit
) {
    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(start = 24.dp, top = 24.dp)) {
            Text(
                text = stringResource(id = R.string.setting_theme),
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
            SegmentedControl(
                items =
                listOf(
                    stringResource(id = R.string.setting_theme_system),
                    stringResource(id = R.string.setting_theme_light),
                    stringResource(id = R.string.setting_theme_dark)
                ),
                onItemSelection = { selected ->
                    val screenMode =
                        when (selected) {
                            0 -> ScreenMode.SYSTEM
                            1 -> ScreenMode.LIGHT
                            2 -> ScreenMode.DARK
                            else -> ScreenMode.SYSTEM
                        }
                    onUpdateScreenMode(screenMode)
                },
                defaultSelectedItemIndex =
                when (appContext.screenMode.value) {
                    ScreenMode.SYSTEM -> 0
                    ScreenMode.LIGHT -> 1
                    ScreenMode.DARK -> 2
                }
            )
        }
    }
}

@Preview
@Composable
fun AccessibilityPreview() {
    val appContext = remember { AppContext() }
    AppTheme(screenMode = appContext.screenMode.value) {
        Surface(modifier = Modifier.fillMaxSize()) {
            AccessibilityScreen(
                appContext = appContext,
                onUpdateScreenMode = {
                    appContext.screenMode.value = it
                }
            )
        }
    }
}
