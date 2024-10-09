package com.cacd2.cacdgame.android.features.ui.resultbanner

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.features.ui.resultbanner.ui.ResultKOBannerView
import com.cacd2.cacdgame.android.features.ui.resultbanner.ui.ResultOKBannerView
import org.koin.compose.koinInject

/**
 * Created by frankois on 21/05/2023.
 */
@Composable
fun ResultBannerView(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    @StringRes message: Int? = null,
    isValid: Boolean,
    onClose: () -> Unit
) {
    Surface(
        modifier =
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            onClose()
        }
    ) {
        if (isValid) {
            ResultOKBannerView(modifier, message, appContext)
        } else {
            ResultKOBannerView(modifier, message, appContext)
        }
    }
}

@Preview
@Composable
fun GamePlayerResultQuestionOKBannerPreview() {
    AppTheme {
        Surface {
            ResultBannerView(
                isValid = true,
                appContext = AppContext(mUsername = "TEST1")
            ) {
            }
        }
    }
}

@Preview
@Composable
fun GamePlayerResultQuestionKOBannerPreview() {
    AppTheme {
        Surface {
            ResultBannerView(
                isValid = false,
                appContext = AppContext(mUsername = "TEST2")
            ) {
            }
        }
    }
}
