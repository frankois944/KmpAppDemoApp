package com.cacd2.cacdgame.android.features.settings.accessibility

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.ui.input.UsernameTextField
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.save
import com.cacd2.cacdgame.android.username
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 12/06/2023.
 */
@Composable
fun SettingsUsernameView(
    appContext: AppContext = koinInject(),
    username: TextFieldValue,
    setUsername: (TextFieldValue) -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val inputError =
        if (username.text.trim().length > 10) {
            R.string.onboarding_5_error_max
        } else if (username.text.trim().length <= 2) {
            R.string.onboarding_5_error_min
        } else {
            null
        }

    val canSaveUsername =
        inputError == null &&
            username.text.trim().isNotEmpty() &&
            appContext.username.value != username.text.trim()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(id = R.string.setting_username_label),
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        UsernameTextField(
            modifier = Modifier,
            username = username,
            setUsername = setUsername,
            inputError = inputError,
            footer = R.string.setting_can_edit_username,
            keyboardActions =
            KeyboardActions {
                if (canSaveUsername) {
                    focusManager.clearFocus()
                    coroutine.launch {
                        appContext.username.value = username.text
                        save()
                    }
                }
            },
            focusRequester = remember { FocusRequester() }
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    focusManager.clearFocus()
                    coroutine.launch {
                        appContext.username.value = username.text
                        save()
                    }
                },
                enabled = canSaveUsername,
                shape = RoundedCornerShape(50)
            ) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.username_update).uppercase())
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsUsernamePreview() {
    AppTheme {
        Surface {
            val appContext = AppContext()
            val (username, setUsername) =
                remember {
                    mutableStateOf(TextFieldValue(text = appContext.username.value ?: ""))
                }
            SettingsUsernameView(
                username = username,
                setUsername = setUsername
            )
        }
    }
}
