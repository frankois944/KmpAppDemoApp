package com.cacd2.cacdgame.android.features.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.appbarTitle
import com.cacd2.cacdgame.android.forceHideBackButton
import com.cacd2.cacdgame.android.isAppBarVisible
import com.cacd2.cacdgame.android.ui.AppBarWithActionView
import com.cacd2.cacdgame.android.ui.AppBarWithNoActionView
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
@Composable
fun AppBarView(
    appContext: AppContext = koinInject(),
    canShowBackButton: Boolean = false,
    onBackPressed: () -> Unit
) {
    val hasBackButton = canShowBackButton && !appContext.forceHideBackButton.value

    if (appContext.isAppBarVisible.value) {
        if (appContext.appbarTitle.value?.isNotEmpty() == true ||
            hasBackButton
        ) {
            AppBarWithActionView(
                appContext = appContext,
                hasBackButton = hasBackButton,
                onBackPressed = onBackPressed
            )
        } else {
            AppBarWithNoActionView(
                appContext = appContext
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun AppBarPreviewWithContent() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Scaffold(
                topBar = {
                    AppBarView(
                        canShowBackButton = true,
                        appContext =
                        AppContext(
                            mAppbarTitle = "A long title",
                            mForceHideBackButton = false
                        )
                    ) {}
                }
            ) { innerPadding ->
                Text("CONTENT", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun AppBarPreviewWithNoContent() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Scaffold(
                topBar = { AppBarView(AppContext()) {} }
            ) { innerPadding ->
                Text("CONTENT", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
