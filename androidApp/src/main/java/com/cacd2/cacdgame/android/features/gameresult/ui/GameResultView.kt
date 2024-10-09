package com.cacd2.cacdgame.android.features.gameresult.ui

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.gameresult.GameResultActions
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.model.shortname
import com.cacd2.cacdgame.android.ui.BottomShadow
import com.cacd2.cacdgame.android.utility.annotatedStringResource
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.HistoryData
import com.cacd2.cacdgame.model.QuestionChoice
import com.cacd2.cacdgame.model.averageMsResponseTime
import com.cacd2.cacdgame.model.nbKoResponse
import com.cacd2.cacdgame.model.nbOkResponse
import kotlin.math.roundToInt
import org.koin.compose.koinInject

/**
 * Created by frankois on 25/05/2023.
 */

@Composable
fun GameResultView(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    history: HistoryData,
    onAction: (GameResultActions) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            ConstraintLayout(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                val (results, graph) = createRefs()
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier =
                    Modifier.constrainAs(results) {
                        start.linkTo(graph.end, margin = 10.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val color = MaterialTheme.colors.primary
                        Canvas(modifier = Modifier.size(10.dp)) {
                            drawCircle(color, radius = 5.dp.toPx())
                        }
                        Text(
                            annotatedStringResource(
                                id = R.string.result_correct_answser_color
                            ),
                            style =
                            TextStyle(
                                fontFamily = fonts,
                                fontSize = 12.sp,
                                platformStyle =
                                PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val color = MaterialTheme.colors.error
                        Canvas(modifier = Modifier.size(10.dp)) {
                            drawCircle(color, radius = 5.dp.toPx())
                        }
                        Text(
                            annotatedStringResource(
                                id = R.string.result_bad_answser_color
                            ),
                            style =
                            TextStyle(
                                fontFamily = fonts,
                                fontSize = 12.sp,
                                platformStyle =
                                PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                    Divider(
                        startIndent = 10.dp,
                        thickness = 1.dp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.score_level),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 5.dp),
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                        Text(
                            annotatedStringResource(
                                id = R.string.result_level,
                                formatArgs =
                                arrayOf(
                                    1,
                                    stringResource(
                                        id = history.data.first().difficulty.shortname
                                    )
                                ),
                                styles =
                                mapOf(
                                    Typeface.ITALIC to
                                        SpanStyle(
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                )
                            ),
                            style =
                            TextStyle(
                                fontFamily = fonts,
                                fontSize = 12.sp,
                                platformStyle =
                                PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.score_time),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 5.dp),
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                        Text(
                            annotatedStringResource(
                                id = R.string.result_response_time,
                                formatArgs =
                                arrayOf(
                                    (history.data.averageMsResponseTime() / 1000f).roundToInt()
                                ),
                                styles =
                                mapOf(
                                    Typeface.ITALIC to
                                        SpanStyle(
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                )
                            ),
                            style =
                            TextStyle(
                                fontFamily = fonts,
                                fontSize = 12.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier)
                }
                GameResultChartView(
                    ok = history.data.nbOkResponse(),
                    ko = history.data.nbKoResponse(),
                    modifier =
                    Modifier.constrainAs(graph) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        width = Dimension.value(185.dp)
                        height = Dimension.value(185.dp)
                    },
                    fontSizeTotalQuestion = 2f,
                    fontSizeCorrectQuestion = 5f,
                    withIcons = true
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Column {
                if (history.data.nbOkResponse() > 0) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                        Modifier
                            .clickable {
                                onAction(GameResultActions.SHOW_OK_QUESTIONS)
                            }
                            .height(56.dp)
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.check),
                                contentDescription = null,
                                colorFilter =
                                ColorFilter.tint(
                                    MaterialTheme.colors.primary
                                )
                            )
                            Text(
                                annotatedStringResource(
                                    id = R.string.result_correct_response_count,
                                    formatArgs = arrayOf("${history.data.nbOkResponse()}")
                                ),
                                modifier = Modifier.padding(start = 30.dp),
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.score_detail),
                            contentDescription = null,
                            modifier = Modifier,
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                    }
                    Divider(thickness = 1.dp, startIndent = 80.dp)
                }
                if (history.data.nbKoResponse() > 0) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                        Modifier
                            .clickable {
                                onAction(GameResultActions.SHOW_KO_QUESTIONS)
                            }
                            .height(56.dp)
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.error_check),
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
                                contentDescription = null
                            )
                            Text(
                                annotatedStringResource(
                                    id = R.string.result_bad_response_count,
                                    formatArgs = arrayOf("${history.data.nbKoResponse()}")
                                ),
                                modifier = Modifier.padding(start = 30.dp),
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.score_detail),
                            contentDescription = null,
                            modifier = Modifier,
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                    }
                    Divider(thickness = 1.dp, startIndent = 80.dp)
                }
            }
            BottomShadow(alpha = .15f, height = 10.dp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .clickable {
                    onAction(GameResultActions.SHOW_DETAIL)
                },
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.score_more),
                contentDescription = null,
                colorFilter =
                ColorFilter.tint(
                    MaterialTheme.colors.primary
                )
            )
            Text(
                annotatedStringResource(
                    id = R.string.result_know_more,
                    styles =
                    mapOf(
                        Typeface.ITALIC to
                            SpanStyle(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                    )
                ),
                textAlign = TextAlign.Center,
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier =
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 20.dp)
            )
        }
        Spacer(modifier = Modifier)
    }
}

@Preview(device = "id:pixel_4", locale = "fr")
@Composable
fun GameResultPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultView(
                appContext = AppContext(),
                history =
                HistoryData(
                    id = 42,
                    timestamp = 0,
                    category = GameChoice.DESIGN,
                    data =
                    listOf(
                        QuestionChoice(
                            id = "1",
                            content = "1",
                            detail = null,
                            game = GameChoice.DESIGN,
                            answers = emptyList(),
                            responseTime = 5L,
                            lang = currentLanguage,
                            illustration = null,
                            userSelectedAnswer =
                            AnswerChoice(
                                id = "1",
                                content = "1",
                                isCorrect = false
                            )
                        ),
                        QuestionChoice(
                            id = "1",
                            content = "1",
                            detail = null,
                            game = GameChoice.DESIGN,
                            answers = emptyList(),
                            responseTime = 10L,
                            lang = currentLanguage,
                            illustration = null,
                            userSelectedAnswer =
                            AnswerChoice(
                                id = "1",
                                content = "1",
                                isCorrect = false
                            )
                        ),
                        QuestionChoice(
                            id = "1",
                            content = "1",
                            detail = null,
                            game = GameChoice.DESIGN,
                            answers = emptyList(),
                            responseTime = 15L,
                            lang = currentLanguage,
                            illustration = null,
                            userSelectedAnswer =
                            AnswerChoice(
                                id = "1",
                                content = "1",
                                isCorrect = true
                            )
                        )
                    )
                ),
                onAction = {
                }
            )
        }
    }
}
