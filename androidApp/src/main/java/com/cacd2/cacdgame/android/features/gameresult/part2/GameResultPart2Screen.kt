package com.cacd2.cacdgame.android.features.gameresult.part2

import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.gameresult.GameResultActions
import com.cacd2.cacdgame.android.features.trackEvent
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.utility.annotatedStringResource

/**
 * Created by frankois on 28/05/2023.
 */
@Composable
fun GameResultPart2View(modifier: Modifier = Modifier, onAction: (GameResultActions) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        Row(
            modifier =
            Modifier.background(
                MaterialTheme.colors.primary.copy(
                    alpha = if (MaterialTheme.colors.isLight) 0.12f else 0.06f
                )
            ).fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.info_icon),
                contentDescription = null,
                modifier =
                Modifier
                    .padding(start = 20.dp, top = 20.dp),
                colorFilter =
                ColorFilter.tint(
                    MaterialTheme.colors.primary
                )
            )
            Text(
                text =
                annotatedStringResource(
                    id = R.string.description_full_company,
                    styles =
                    mapOf(
                        Typeface.ITALIC to
                            SpanStyle(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.SemiBold
                            ),
                        Typeface.BOLD to
                            SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                    )
                ),
                modifier =
                Modifier.padding(
                    start = 10.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            /*Text(
                text = stringResource(id = R.string.asterix_campany_name),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.90f),
                style = TextStyle(
                    fontFamily = fonts,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            )*/
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Button(
                    onClick = {
                        trackEvent(name = "OPEN WEBSITE")
                        onAction(GameResultActions.OPEN_WEBSITE)
                    },
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_north_east_24),
                        contentDescription = null
                    )
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.go_to_website).uppercase())
                    }
                }
                Button(
                    onClick = { onAction(GameResultActions.SHOW_CONTACT) },
                    shape = RoundedCornerShape(50)
                ) {
                    Box(
                        modifier = Modifier.height(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.go_to_contact).uppercase())
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun GameResultPart2Preview() {
    AppTheme {
        Surface(modifier = Modifier) {
            GameResultPart2View {}
        }
    }
}
