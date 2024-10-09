package com.cacd2.cacdgame.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.AppEvent
import com.cacd2.cacdgame.EventBus
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.appbarTitle
import com.cacd2.cacdgame.android.showCloseScreenButton
import com.cacd2.cacdgame.android.showFilterHistoryButton
import com.cacd2.cacdgame.android.showHideLexicalButton
import com.cacd2.cacdgame.android.showSearchLexicalButton
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Created by frankois on 23/06/2023.
 */
@Composable
fun AppBarWithActionView(
    appContext: AppContext = koinInject(),
    hasBackButton: Boolean,
    onBackPressed: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val hasAction = appContext.showSearchLexicalButton.value ||
        appContext.showHideLexicalButton.value ||
        appContext.showFilterHistoryButton.value ||
        appContext.showCloseScreenButton.value

    CenterTopAppBar(
        title = {
            Text(
                text = appContext.appbarTitle.value ?: "",
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        },
        navigationIcon =
        {
            if (hasBackButton) {
                AppBarBackButton {
                    onBackPressed()
                }
            }
        },
        actions = if (hasAction) {
            {
                if (appContext.showSearchLexicalButton.value) {
                    IconButton(onClick = {
                        coroutine.launch { EventBus.publish(AppEvent.TOGGLE_SEARCH_LEXICAL) }
                    }) {
                        Icon(
                            Icons.Default.Search,
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            contentDescription = "Ouvrir critère de recherche des questions"
                        )
                    }
                } else if (appContext.showHideLexicalButton.value) {
                    IconButton(onClick = {
                        coroutine.launch { EventBus.publish(AppEvent.TOGGLE_SEARCH_LEXICAL) }
                    }) {
                        Icon(
                            Icons.Default.Close,
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            contentDescription = "Fermer critère de recherche des questions"
                        )
                    }
                } else if (appContext.showFilterHistoryButton.value) {
                    IconButton(onClick = {
                        coroutine.launch { EventBus.publish(AppEvent.TOGGLE_FILTER_HISTORY) }
                    }) {
                        Icon(
                            painterResource(id = R.drawable.filter),
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            contentDescription = "Filtrer l'historique"
                        )
                    }
                } else if (appContext.showCloseScreenButton.value) {
                    IconButton(
                        onClick = {
                            coroutine.launch {
                                EventBus.publish(AppEvent.CLOSE_SCREEN)
                            }
                        },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Default.Close,
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                            contentDescription = "Fermer l'écran"
                        )
                    }
                }
            }
        } else {
            { }
        }
    )
}

@Composable
fun AppBarBackButton(modifier: Modifier = Modifier, onBackPressed: () -> Unit) {
    IconButton(
        onClick = onBackPressed,
        modifier = modifier
    ) {
        Icon(
            painterResource(id = R.drawable.chevron_left),
            tint = if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            contentDescription = "Revenir écran précedent"
        )
    }
}
