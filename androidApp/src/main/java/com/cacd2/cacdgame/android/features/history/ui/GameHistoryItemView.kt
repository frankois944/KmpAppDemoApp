package com.cacd2.cacdgame.android.features.history.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.gameresult.ui.GameResultChartView
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.model.label
import com.cacd2.cacdgame.android.model.title
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.model.HistoryData
import com.cacd2.cacdgame.model.nbKoResponse
import com.cacd2.cacdgame.model.nbOkResponse
import java.text.DateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameHistoryItemView(
    modifier: Modifier = Modifier,
    item: HistoryData,
    onSelectItem: (HistoryData) -> Unit
) {
    Surface(
        modifier = modifier.clickable { onSelectItem(item) },
        elevation = if (MaterialTheme.colors.isLight) 2.dp else 10.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier) {
                GameResultChartView(
                    ok = item.data.nbOkResponse(),
                    ko = item.data.nbKoResponse(),
                    modifier = Modifier.size(70.dp),
                    fontSizeCorrectQuestion = 1.5f,
                    fontSizeTotalQuestion = 1.5f,
                    withIcons = false
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = stringResource(id = item.category.title),
                        style =
                        TextStyle(
                            fontFamily = fonts,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.history_description),
                        style =
                        TextStyle(
                            fontFamily = fonts,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light
                        ),
                        modifier = Modifier.padding(end = 28.dp),
                        lineHeight = 15.sp
                    )
                }
            }
            Divider(modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 4.dp))
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(35.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colors.primary.copy(alpha = 0.38f),
                        shape = RoundedCornerShape(size = 2.dp),
                        modifier = Modifier.requiredSize(45.dp, 15.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = item.category.label),
                                color = MaterialTheme.colors.onPrimary,
                                modifier =
                                Modifier.padding(
                                    top = 1.dp,
                                    bottom = 1.dp,
                                    start = 5.dp,
                                    end = 5.dp
                                ),
                                style =
                                TextStyle(
                                    fontStyle = FontStyle(R.font.robotoregular),
                                    fontSize = 10.sp,
                                    platformStyle =
                                    PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                ),
                                lineHeight = 16.sp
                            )
                        }
                    }
                    Text(
                        text =
                        DateFormat.getDateInstance(
                            DateFormat.SHORT,
                            Locale(currentLanguage.code)
                        ).format(
                            Date(item.timestamp)
                        ),
                        style =
                        TextStyle(
                            fontFamily = fonts,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                }
                /*Icon(
                    imageVector = Icons.Default.Share,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = null
                )*/
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun GameHistoryItemPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            GameHistoryItemView(
                item = HistoryData.dummy
            ) {
            }
        }
    }
}
