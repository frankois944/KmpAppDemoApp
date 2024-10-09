package com.cacd2.cacdgame.android.features.ui.input

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.robotoFonts

/**
 * Created by frankois on 28/05/2023.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UsernameTextField(
    modifier: Modifier = Modifier,
    username: TextFieldValue,
    setUsername: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    @StringRes footer: Int = R.string.onboarding_5_tips,
    @StringRes inputError: Int? = null,
    keyboardActions: KeyboardActions
) {
    var isEditing by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = {
                Text(text = stringResource(id = R.string.setting_username_label))
            },
            placeholder = {
                Text(text = stringResource(id = R.string.onboarding_5_placeholder))
            },
            isError = inputError != null,
            keyboardActions = keyboardActions,
            modifier =
            Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isEditing = it.isFocused
                }.focusRequester(focusRequester),
            singleLine = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.edit_pen_icn),
                    contentDescription = null
                )
            }
        )
        inputError?.let {
            Text(
                text = stringResource(id = it),
                color = MaterialTheme.colors.error,
                modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                maxLines = 2,
                style =
                TextStyle(
                    fontFamily = robotoFonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        } ?: run {
            Text(
                text = stringResource(id = footer),
                color =
                if (isEditing) {
                    MaterialTheme.colors.primary
                } else {
                    TextFieldDefaults.outlinedTextFieldColors().placeholderColor(
                        enabled = true
                    ).value
                },
                modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                maxLines = 2,
                style =
                TextStyle(
                    fontFamily = robotoFonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun UsernameTextFieldPreview() {
    AppTheme {
        Surface {
            var input = remember { TextFieldValue("") }
            UsernameTextField(
                username = input,
                setUsername = { input = it },
                keyboardActions = KeyboardActions.Default,
                focusRequester = FocusRequester()
            )
        }
    }
}
