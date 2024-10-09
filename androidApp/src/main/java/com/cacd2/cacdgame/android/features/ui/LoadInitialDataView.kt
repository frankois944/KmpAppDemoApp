package com.cacd2.cacdgame.android.features.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.UIState
import com.cacd2.cacdgame.android.ui.loading.LoadingScreen
import com.cacd2.cacdgame.datasource.api.game.DatoCMSAPI
import com.cacd2.cacdgame.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@Composable
fun LoadQuestionsDataView(language: Language? = null, onCompleted: @Composable () -> Unit) {
    var uiState by remember { mutableStateOf<UIState<Unit>>(UIState.Loading) }
    var runLoading by remember { mutableStateOf(true) }
    LaunchedEffect(runLoading) {
        if (!runLoading) {
            return@LaunchedEffect
        }
        uiState = UIState.Loading
        uiState =
            try {
                withContext(Dispatchers.Default) {
                    DatoCMSAPI.fetchAllData(language)
                }
                UIState.Success(Unit)
            } catch (ex: Exception) {
                UIState.Error(ex)
            } finally {
                runLoading = false
            }
    }
    when (uiState) {
        UIState.Loading ->
            LoadingScreen(
                error = null
            ) // no response yet
        is UIState.Error ->
            LoadingScreen(
                error = uiState as UIState.Error,
                onErrorAction = {
                    runLoading = true
                }
            )

        else -> onCompleted()
    }
}

@Preview
@Composable
fun LoadInitialDataPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoadQuestionsDataView {}
        }
    }
}
