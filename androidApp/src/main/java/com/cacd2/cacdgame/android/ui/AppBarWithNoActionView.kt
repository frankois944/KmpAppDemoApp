package com.cacd2.cacdgame.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.showAppBarContent
import org.koin.compose.koinInject

/**
 * Created by frankois on 23/06/2023.
 */
@Composable
fun AppBarWithNoActionView(appContext: AppContext = koinInject()) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier =
            Modifier
                .size(122.dp, 44.dp)
                .alpha(
                    if (appContext.showAppBarContent.value) {
                        1f
                    } else {
                        0f
                    }
                )
        )
    }
}
