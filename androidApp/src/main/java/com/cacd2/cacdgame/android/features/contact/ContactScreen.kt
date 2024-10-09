package com.cacd2.cacdgame.android.features.contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.Constants
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.UIState
import com.cacd2.cacdgame.android.features.trackEvent
import com.cacd2.cacdgame.android.features.ui.resultbanner.ResultBannerView
import com.cacd2.cacdgame.android.forceHideBottomBar
import com.cacd2.cacdgame.android.robotoFonts
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.ui.loading.LoadingAnimationDotView
import com.cacd2.cacdgame.android.utility.TimerTicks
import com.cacd2.cacdgame.model.Contact
import java.util.regex.Pattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 30/05/2023.
 */
@Composable
fun ContactScreen(
    appContext: AppContext = koinInject(),
    shouldShowBottomBar: Boolean = true,
    savingState: UIState<Unit>? = null
) {
    var savingState by remember { mutableStateOf(savingState) }
    var triggerSendForm by remember { mutableStateOf(false) }
    val (firstname, setFirstname) =
        remember {
            mutableStateOf(TextFieldValue(text = ""))
        }
    var firstNameError by remember { mutableStateOf<Int?>(null) }
    val (lastname, setLastname) =
        remember {
            mutableStateOf(TextFieldValue(text = ""))
        }
    var lastnameError by remember { mutableStateOf<Int?>(null) }
    val (email, setEmail) =
        remember {
            mutableStateOf(TextFieldValue(text = ""))
        }
    var emailError by remember { mutableStateOf<Int?>(null) }
    val (phone, setPhone) =
        remember {
            mutableStateOf(TextFieldValue(text = ""))
        }
    val (message, setMessage) =
        remember {
            mutableStateOf(TextFieldValue(text = ""))
        }
    var messageError by remember { mutableStateOf<Int?>(null) }
    val (check, setCheck) =
        remember {
            mutableStateOf(false)
        }

    appContext.forceHideBottomBar.value = (savingState is UIState.Error) ||
        savingState is UIState.Loading &&
        shouldShowBottomBar == false

    fun clearFields() {
        setFirstname(TextFieldValue(text = ""))
        setLastname(TextFieldValue(text = ""))
        setEmail(TextFieldValue(text = ""))
        setPhone(TextFieldValue(text = ""))
        setMessage(TextFieldValue(text = ""))
        setCheck(false)
    }

    LaunchedEffect(triggerSendForm) {
        if (!triggerSendForm) {
            return@LaunchedEffect
        }
        firstNameError =
            if (firstname.text.isEmpty() || firstname.text.length > 100) {
                R.string.mandatory_field
            } else {
                null
            }
        lastnameError =
            if (lastname.text.isEmpty() || lastname.text.length > 100) {
                R.string.mandatory_field
            } else {
                null
            }
        emailError =
            if (email.text.isEmpty() || email.text.length > 100) {
                R.string.mandatory_field
            } else if (!Pattern.compile(Constants.EMAIL_REGEX).matcher(email.text).matches()) {
                R.string.adresse_email_invalide
            } else {
                null
            }
        messageError =
            if (message.text.isEmpty() || message.text.length > 500) {
                R.string.mandatory_message
            } else {
                null
            }
        val hasError =
            !check ||
                firstNameError != null ||
                lastnameError != null ||
                emailError != null ||
                messageError != null
        if (!hasError) {
            savingState = UIState.Loading
            withContext(Dispatchers.IO) {
                val isOk =
                    Contact.createLead(
                        firstname.text,
                        lastname.text,
                        email.text,
                        message.text,
                        phone.text
                    )
                withContext(Dispatchers.Main) {
                    savingState =
                        if (isOk) {
                            UIState.Success(Unit)
                        } else {
                            UIState.Error(Exception("Generic error"))
                        }
                }
            }
        }
        triggerSendForm = false
    }

    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Column(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val focusManager = LocalFocusManager.current
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = firstname,
                            onValueChange = setFirstname,
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.contact_firstname))
                            },
                            keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            isError = firstNameError != null,
                            enabled = savingState != UIState.Loading
                        )
                        firstNameError?.let {
                            Text(
                                text = stringResource(id = it),
                                color = MaterialTheme.colors.error,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = lastname,
                            onValueChange = setLastname,
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.contact_lastname))
                            },
                            keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            isError = lastnameError != null,
                            enabled = savingState != UIState.Loading
                        )
                        lastnameError?.let {
                            Text(
                                text = stringResource(id = it),
                                color = MaterialTheme.colors.error,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = email,
                            onValueChange = setEmail,
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.contact_email))
                            },
                            keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            isError = emailError != null,
                            enabled = savingState != UIState.Loading
                        )
                        emailError?.let {
                            Text(
                                text = stringResource(id = it),
                                color = MaterialTheme.colors.error,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = phone,
                            onValueChange = setPhone,
                            singleLine = true,
                            label = {
                                Text(text = stringResource(id = R.string.contact_phone))
                            },
                            keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Phone
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            enabled = savingState != UIState.Loading
                        )
                    }
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = message,
                            onValueChange = setMessage,
                            minLines = 5,
                            label = {
                                Text(text = stringResource(id = R.string.contact_message))
                            },
                            keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.clearFocus(true)
                                }
                            ),
                            isError = messageError != null,
                            enabled = savingState != UIState.Loading
                        )
                        messageError?.let {
                            Text(
                                text = stringResource(id = it),
                                color = MaterialTheme.colors.error,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }
                    }
                    Row(
                        modifier =
                        Modifier.toggleable(
                            value = check,
                            role = Role.Checkbox,
                            onValueChange = {
                                if (savingState != UIState.Loading) {
                                    setCheck(!check)
                                }
                            }
                        ).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            modifier = Modifier.padding(top = 2.dp),
                            checked = check,
                            onCheckedChange = null,
                            colors =
                            CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colors.primary,
                                disabledIndeterminateColor = MaterialTheme.colors.primary,
                                uncheckedColor = MaterialTheme.colors.primary
                            ),
                            enabled = savingState != UIState.Loading
                        )
                        Text(
                            text = stringResource(id = R.string.contact_require),
                            modifier = Modifier,
                            style =
                            TextStyle(
                                fontFamily = robotoFonts,
                                fontSize = 15.sp
                            ),
                            lineHeight = 18.sp
                        )
                    }
                    if (savingState == UIState.Loading) {
                        LoadingAnimationDotView()
                    } else {
                        Button(
                            onClick = {
                                trackEvent(name = "Send Message")
                                triggerSendForm = true
                            },
                            shape = RoundedCornerShape(50),
                            enabled = check
                        ) {
                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = stringResource(id = R.string.contact_send))
                            }
                        }
                    }
                }
            }
            if (savingState !== null && savingState != UIState.Loading) {
                if (savingState is UIState.Error) {
                    TimerTicks(initTick = 0, interval = 2_000L) {
                        if (it >= 2_000L) {
                            savingState = null
                        }
                    }
                    ResultBannerView(
                        isValid = false,
                        appContext = appContext,
                        message = R.string.generic_error
                    ) {
                        savingState = null
                    }
                } else if (savingState is UIState.Success) {
                    TimerTicks(initTick = 0, interval = 4_000L) {
                        if (it >= 4_000L) {
                            clearFields()
                            savingState = null
                        }
                    }
                    ContactSentView(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                clearFields()
                                savingState = null
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ContactPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ContactScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                savingState = null
            )
        }
    }
}

@Preview
@Composable
fun ContactLoadingPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ContactScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                savingState = UIState.Loading
            )
        }
    }
}

@Preview
@Composable
fun ContactOKPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ContactScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                savingState = UIState.Success(Unit)
            )
        }
    }
}

@Preview
@Composable
fun ContactKOPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ContactScreen(
                appContext = AppContext(mUsername = "DEBUG"),
                savingState = UIState.Error()
            )
        }
    }
}
