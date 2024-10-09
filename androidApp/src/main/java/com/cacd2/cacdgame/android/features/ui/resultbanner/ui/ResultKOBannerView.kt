package com.cacd2.cacdgame.android.features.ui.resultbanner.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.username
import com.cacd2.cacdgame.android.utility.annotatedStringResource
import org.koin.compose.koinInject

/**
 * Created by frankois on 21/05/2023.
 */
@Composable
fun ResultKOBannerView(
    modifier: Modifier = Modifier,
    @StringRes message: Int? = null,
    appContext: AppContext = koinInject()
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colors.error.copy(0.12f),
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.error_check),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 30.dp, top = 4.dp),
                text =
                annotatedStringResource(
                    id = message ?: R.string.game_banner_loose,
                    formatArgs =
                    arrayOf(
                        appContext.username.value!!
                    )
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Preview
@Composable
fun GamePlayerResultKOBannerPreview() {
    AppTheme {
        Surface {
            ResultKOBannerView(appContext = AppContext(mUsername = "Charly"))
        }
    }
}
