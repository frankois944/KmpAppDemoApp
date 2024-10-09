package com.cacd2.cacdgame.android.features.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.AppEvent
import com.cacd2.cacdgame.EventBus
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.questionlisting.ui.QuestionListingListView
import com.cacd2.cacdgame.android.isSearchCriteriaOpen
import com.cacd2.cacdgame.android.model.label
import com.cacd2.cacdgame.android.showHideLexicalButton
import com.cacd2.cacdgame.android.showSearchLexicalButton
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.getHistoryCount
import com.cacd2.cacdgame.datasource.database.dao.searchQuestions
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 20/06/2023.
 */

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchLexicalScreen(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    testQuestions: List<QuestionChoice> = emptyList(),
    onSelectQuestionId: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val (criteria, setCriteria) = rememberSaveable { mutableStateOf("") }
    val (design, setDesign) = rememberSaveable { mutableStateOf(true) }
    val (product, setProduct) = rememberSaveable { mutableStateOf(true) }
    val (tech, setTech) = rememberSaveable { mutableStateOf(true) }
    var lastFilterCondition by rememberSaveable { mutableStateOf<FilterCondition?>(null) }
    val scaffoldState =
        rememberBackdropScaffoldState(
            if (appContext.isSearchCriteriaOpen.value) BackdropValue.Revealed else BackdropValue.Concealed
        )
    var questions by rememberSaveable { mutableStateOf(testQuestions) }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val rememberLazyListState = rememberLazyListState()
    var hasNoHistory by remember { mutableStateOf(true) }

    LaunchedEffect(criteria, design, product, tech) {
        searchJob?.cancel()
        searchJob = null
        searchJob =
            coroutineScope.launch {
                if (lastFilterCondition?.criteria != criteria) {
                    delay(250L)
                } else if (lastFilterCondition ==
                    FilterCondition(
                        criteria,
                        design,
                        product,
                        tech
                    )
                ) {
                    return@launch
                }
                if (this.isActive) {
                    questions =
                        Database.data.searchQuestions(
                            criteria = criteria,
                            designSelected = design,
                            productSelected = product,
                            techSelected = tech
                        )
                    rememberLazyListState.scrollToItem(0)
                    lastFilterCondition =
                        FilterCondition(
                            criteria,
                            design,
                            product,
                            tech
                        )
                }
            }
    }

    LaunchedEffect(scaffoldState.isConcealed) {
        if (scaffoldState.isConcealed) {
            try {
                keyboardController?.hide()
                focusRequester.freeFocus()
                focusManager.clearFocus(true)
            } catch (ex: Exception) {
            }
        }
        appContext.isSearchCriteriaOpen.value = scaffoldState.isRevealed
    }

    LaunchedEffect(Unit) {
        hasNoHistory = testQuestions.isEmpty() == true && Database.data.getHistoryCount() == 0L
        coroutineScope.launch {
            EventBus.subscribe<AppEvent> {
                when (it) {
                    AppEvent.TOGGLE_SEARCH_LEXICAL -> {
                        coroutineScope.launch {
                            if (scaffoldState.isConcealed) {
                                scaffoldState.reveal()
                            } else {
                                scaffoldState.conceal()
                            }
                            appContext.showSearchLexicalButton.value = scaffoldState.isConcealed
                            appContext.showHideLexicalButton.value = scaffoldState.isRevealed
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
        if (hasNoHistory) {
            SearchLexicalEmptyView(modifier)
        } else {
            BackdropScaffold(
                modifier = modifier,
                scaffoldState = scaffoldState,
                backLayerBackgroundColor = MaterialTheme.colors.surface,
                headerHeight = 0.dp,
                peekHeight = 0.dp,
                persistentAppBar = false,
                appBar = {
                    TopAppBar(elevation = 0.dp, backgroundColor = Color.Transparent) {
                    }
                },
                backLayerContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            modifier =
                            Modifier
                                .padding(12.dp)
                                .focusRequester(focusRequester)
                                .fillMaxWidth(),
                            value = criteria,
                            onValueChange = setCriteria,
                            singleLine = true,
                            maxLines = 1,
                            label = {
                                Text(stringResource(id = R.string.search_definition_label))
                            },
                            placeholder = {
                                Text(
                                    text =
                                    stringResource(
                                        id = R.string.search_definition_placeholder
                                    )
                                )
                            }
                        )
                        Divider()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                            modifier =
                            Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier =
                                Modifier
                                    .toggleable(
                                        value = product,
                                        role = Role.Checkbox,
                                        onValueChange = setProduct
                                    )
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = product,
                                    onCheckedChange = null,
                                    colors =
                                    CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.primary,
                                        disabledIndeterminateColor = MaterialTheme.colors.primary,
                                        uncheckedColor = MaterialTheme.colors.primary
                                    )
                                )
                                Text(
                                    text = stringResource(id = GameChoice.PRODUCT.label),
                                    style =
                                    MaterialTheme.typography.button.apply {
                                        PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    }
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier =
                                Modifier
                                    .toggleable(
                                        value = tech,
                                        role = Role.Checkbox,
                                        onValueChange = setTech
                                    )
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = tech,
                                    onCheckedChange = null,
                                    colors =
                                    CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.primary,
                                        disabledIndeterminateColor = MaterialTheme.colors.primary,
                                        uncheckedColor = MaterialTheme.colors.primary
                                    )
                                )
                                Text(
                                    text = stringResource(id = GameChoice.TECH.label),
                                    style =
                                    MaterialTheme.typography.button.apply {
                                        PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    }
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier =
                                Modifier
                                    .toggleable(
                                        value = design,
                                        role = Role.Checkbox,
                                        onValueChange = setDesign
                                    )
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = design,
                                    onCheckedChange = null,
                                    colors =
                                    CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.primary,
                                        disabledIndeterminateColor = MaterialTheme.colors.primary,
                                        uncheckedColor = MaterialTheme.colors.primary
                                    )
                                )
                                Text(
                                    text = stringResource(id = GameChoice.DESIGN.label),
                                    style =
                                    MaterialTheme.typography.button.apply {
                                        PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                frontLayerContent = {
                    Surface {
                        QuestionListingListView(
                            modifier = Modifier.fillMaxSize(),
                            items = questions,
                            state = rememberLazyListState,
                            onSelectQuestionId = {
                                onSelectQuestionId(it)
                            }
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun SearchLexicalScreenPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SearchLexicalScreen(appContext = AppContext()) {}
        }
    }
}

@Preview
@Composable
fun SearchLexicalOpenScreenPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SearchLexicalScreen(appContext = AppContext(mIsSearchCriteriaOpen = true)) {}
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun SearchLexicalScreenDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SearchLexicalScreen(appContext = AppContext()) {}
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun SearchLexicalOpenScreenDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SearchLexicalScreen(appContext = AppContext(mIsSearchCriteriaOpen = true)) {}
        }
    }
}
