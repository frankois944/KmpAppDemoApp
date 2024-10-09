package com.cacd2.cacdgame.android.features.onboarding.ui

import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.ui.input.UsernameTextField
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.ui.NavigationFooterView
import com.cacd2.cacdgame.android.username
import com.cacd2.cacdgame.android.utility.annotatedStringResource
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 22/05/2023.ssdf
 */
@Composable
fun OnBoardingPage4View(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    focusRequester: FocusRequester,
    prevPage: () -> Unit,
    nextPage: (String) -> Unit
) {
    val (username, setUsername) =
        remember {
            mutableStateOf(TextFieldValue(text = appContext.username.value ?: ""))
        }
    var inputError by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(username) {
        inputError =
            if (username.text.length > 10) {
                R.string.onboarding_5_error_max
            } else if (username.text.length == 2) {
                R.string.onboarding_5_error_min
            } else {
                null
            }
    }

    CommonCardBackgroundView(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =
                annotatedStringResource(
                    id = R.string.onbaording_5_content,
                    styles =
                    mapOf(
                        Typeface.ITALIC to
                            SpanStyle(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                    )
                ),
                modifier =
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 40.dp)
            )
            Column(
                modifier =
                Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UsernameTextField(
                    modifier = Modifier,
                    username = username,
                    setUsername = setUsername,
                    inputError = inputError,
                    focusRequester = focusRequester,
                    keyboardActions =
                    KeyboardActions {
                        if (inputError == null && username.text.isNotEmpty()) {
                            nextPage(username.text)
                        }
                    }
                )
            }
            Button(
                modifier = Modifier.padding(top = 40.dp),
                onClick = {
                    nextPage(username.text)
                },
                shape = RoundedCornerShape(50),
                enabled = inputError == null && username.text.isNotEmpty()
            ) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.onbaording_5_button).uppercase())
                }
            }
            NavigationFooterView(
                horizontalArrangement = Arrangement.Start,
                onGoForward = null,
                onGoBack = {
                    prevPage()
                },
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 24.dp)
            )
        }
    }
}

@Preview
@Composable
fun OnBoardingPage5Preview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnBoardingPage4View(
                appContext = AppContext(),
                prevPage = {},
                nextPage = {},
                focusRequester = FocusRequester()
            )
        }
    }
}
