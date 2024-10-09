package com.cacd2.cacdgame.android.features.settings

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.BuildConfig
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.consent.ConsentManagerScreen
import com.cacd2.cacdgame.android.features.settings.accessibility.SettingsUsernameView
import com.cacd2.cacdgame.android.features.trackScreenView
import com.cacd2.cacdgame.android.ui.AppThemeSelectionView
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.ui.LanguageSelectionView
import com.cacd2.cacdgame.android.username
import org.koin.compose.koinInject

/**
 * Created by frankois on 28/05/2023.
 */
@Composable
fun SettingsScreen(
    appContext: AppContext = koinInject(),
    showOnboarding: () -> Unit,
    onSelectAccessibility: () -> Unit
) {
    val (username, setUsername) =
        remember {
            mutableStateOf(TextFieldValue(text = appContext.username.value ?: ""))
        }
    var consentManagerDisplayed by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cacd2.fr/mobile-privacy.html"))

    BackHandler(consentManagerDisplayed) {
        consentManagerDisplayed = false
    }

    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        Box {
            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SettingsUsernameView(appContext, username, setUsername)
                    if (BuildConfig.FOR_TESTING) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
                        ) {
                            Button(
                                onClick = showOnboarding
                            ) {
                                Box(
                                    modifier = Modifier,
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "SHOW ONBOARDING (TEST)")
                                }
                            }
                        }
                    }
                    LanguageSelectionView(
                        modifier =
                        Modifier.fillMaxWidth()
                            .height(56.dp)
                            .padding(start = 8.dp),
                        appContext = appContext
                    )
                    Divider(thickness = 1.dp, startIndent = 70.dp)
                    AppThemeSelectionView(
                        modifier =
                        Modifier.fillMaxWidth()
                            .height(56.dp)
                            .padding(start = 8.dp),
                        appContext = appContext
                    )
                    Divider(thickness = 1.dp, startIndent = 70.dp)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                        Modifier
                            .clickable {
                                consentManagerDisplayed = true
                            }
                            .height(56.dp)
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 8.dp
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                        ) {
                            Image(
                                painter =
                                painterResource(
                                    id = R.drawable.baseline_privacy_tip_24
                                ),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                            )
                            Text(
                                text = stringResource(id = R.string.privacy_setting_button),
                                modifier = Modifier.padding(start = 20.dp),
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                    Divider(thickness = 1.dp, startIndent = 70.dp)
                }
            }
        }
        if (consentManagerDisplayed) {
            trackScreenView(name = "ConsentManagerFromSetting")
            ConsentManagerScreen(
                modifier = Modifier.fillMaxSize(),
                onValidation = {
                    consentManagerDisplayed = false
                }
            ) {
                context.startActivity(intent)
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                onSelectAccessibility = {},
                showOnboarding = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun SettingsScreenDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                onSelectAccessibility = {},
                showOnboarding = {}
            )
        }
    }
}
