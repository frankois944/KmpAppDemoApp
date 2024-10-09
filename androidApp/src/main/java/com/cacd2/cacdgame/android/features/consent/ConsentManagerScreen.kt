package com.cacd2.cacdgame.android.features.consent

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.SharedApp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.analyticsEnabled
import com.cacd2.cacdgame.android.consentManagerDisplayed
import com.cacd2.cacdgame.android.crashEnabled
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.performanceEnabled
import com.cacd2.cacdgame.android.robotoFonts
import com.cacd2.cacdgame.android.save
import com.cacd2.cacdgame.android.ui.LanguageSelectionView
import com.cacd2.cacdgame.datasource.settings.ConsentManagerData
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 19/06/2023.
 */
@Composable
fun ConsentManagerScreen(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    onValidation: () -> Unit,
    openConfidentialityPolicy: () -> Unit
) {
    val data by remember {
        mutableStateOf(ConsentManagerData())
    }
    val (analytics, setAnalytics) =
        remember {
            mutableStateOf(appContext.analyticsEnabled.value)
        }
    val (crash, setCrash) =
        remember {
            mutableStateOf(appContext.crashEnabled.value)
        }
    val (performance, setPerformance) =
        remember {
            mutableStateOf(appContext.performanceEnabled.value)
        }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(analytics, crash, performance) {
        data.analytics = analytics
        data.crash = crash
        data.performance = performance
    }

    fun validateSetting(data: ConsentManagerData) {
        SharedApp.updateAnalytics(data)
        coroutineScope.launch {
            appContext.crashEnabled.value = data.crash
            appContext.analyticsEnabled.value = data.analytics
            appContext.performanceEnabled.value = data.performance
            appContext.consentManagerDisplayed.value = true
            save()
            onValidation()
        }
    }

    Surface(modifier = modifier, color = MaterialTheme.colors.surface.copy(alpha = 0.7f)) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.cookie),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 33.dp)
                )
                Surface(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(
                            rememberScrollState()
                        ),
                    color = MaterialTheme.colors.surface,
                    elevation = if (MaterialTheme.colors.isLight) 0.dp else 1.dp,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                ) {
                    Column(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column {
                            LanguageSelectionView(appContext = appContext, leftIcnSpace = 16.dp)
                            TextButton(onClick = openConfidentialityPolicy) {
                                Text(
                                    text = stringResource(R.string.consent_private),
                                    style =
                                    TextStyle(
                                        fontFamily = robotoFonts,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = stringResource(id = R.string.consent_title),
                                color = MaterialTheme.colors.primary,
                                style =
                                TextStyle(
                                    fontFamily = fonts,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.consent_description),
                                style =
                                TextStyle(
                                    fontFamily = fonts,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 15.sp
                                ),
                                lineHeight = 23.sp
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier =
                                Modifier
                                    .toggleable(
                                        value = analytics,
                                        role = Role.Checkbox,
                                        onValueChange = {
                                            setAnalytics(!analytics)
                                        }
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    modifier = Modifier.padding(top = 2.dp),
                                    checked = analytics,
                                    onCheckedChange = null,
                                    colors =
                                    SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colors.primary,
                                        uncheckedTrackAlpha = 0.08f,
                                        checkedTrackAlpha = 0.38f
                                    )
                                )
                                Text(
                                    text = stringResource(id = R.string.consent_analytics),
                                    modifier = Modifier,
                                    style =
                                    TextStyle(
                                        fontFamily = fonts,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    lineHeight = 18.sp
                                )
                            }
                            Row(
                                modifier =
                                Modifier
                                    .toggleable(
                                        value = crash,
                                        role = Role.Checkbox,
                                        onValueChange = {
                                            setCrash(!crash)
                                        }
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    modifier = Modifier.padding(top = 2.dp),
                                    checked = crash,
                                    onCheckedChange = null,
                                    colors =
                                    SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colors.primary,
                                        uncheckedTrackAlpha = 0.08f,
                                        checkedTrackAlpha = 0.38f
                                    )
                                )
                                Text(
                                    text = stringResource(id = R.string.consent_crash),
                                    modifier = Modifier,
                                    style =
                                    TextStyle(
                                        fontFamily = fonts,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    lineHeight = 18.sp
                                )
                            }
                            Row(
                                modifier =
                                Modifier
                                    .toggleable(
                                        value = performance,
                                        role = Role.Checkbox,
                                        onValueChange = {
                                            setPerformance(!performance)
                                        }
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    modifier = Modifier.padding(top = 2.dp),
                                    checked = performance,
                                    onCheckedChange = null,
                                    colors =
                                    SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colors.primary,
                                        uncheckedTrackAlpha = 0.08f,
                                        checkedTrackAlpha = 0.38f
                                    )
                                )
                                Text(
                                    text = stringResource(id = R.string.consent_performance),
                                    modifier = Modifier,
                                    style =
                                    TextStyle(
                                        fontFamily = fonts,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            Button(
                                onClick = {
                                    validateSetting(
                                        ConsentManagerData()
                                    )
                                },
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = R.string.consent_take_all
                                    ).uppercase()
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    validateSetting(data)
                                },
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = R.string.consent_take_selected
                                    ).uppercase()
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    validateSetting(
                                        ConsentManagerData(
                                            analytics = false,
                                            crash = false,
                                            performance = false
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = R.string.consent_refuse_all
                                    ).uppercase()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4", showSystemUi = true)
@Composable
fun ConsentManagerPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Green) {
            ConsentManagerScreen(
                modifier = Modifier.fillMaxSize(),
                appContext = AppContext(),
                onValidation = {},
                openConfidentialityPolicy = {}
            )
        }
    }
}

@Preview(
    device = "id:pixel_4",
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
    locale = "fr"
)
@Composable
fun ConsentManagerDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Green) {
            ConsentManagerScreen(
                modifier = Modifier.fillMaxSize(),
                appContext = AppContext(),
                onValidation = {},
                openConfidentialityPolicy = {}
            )
        }
    }
}
