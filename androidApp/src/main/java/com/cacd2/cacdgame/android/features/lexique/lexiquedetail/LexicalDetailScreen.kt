package com.cacd2.cacdgame.android.features.lexique.lexiquedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.cacd2.cacdgame.android.ui.CommonCardBackgroundView
import com.cacd2.cacdgame.android.utility.annotatedStringResource
import com.cacd2.cacdgame.currentLanguage
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 02/06/2023.
 */
enum class LexicalDetailActionScreen {
    OPEN_DEF,
    OPEN_EXP,
    OPEN_TIPS
}

@Composable
fun LexicalDetailScreen(
    appContext: AppContext = koinInject(),
    questionId: String,
    action: (Pair<LexicalDetailActionScreen, String>) -> Unit
) {
    var question by rememberSaveable { mutableStateOf<QuestionChoice?>(null) }

    LaunchedEffect(Unit) {
        if (question == null) {
            question = GameChoice.getQuestion(questionId)
        }
    }
    question?.let {
        CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
            LexicalDetailContentView(
                question = it,
                modifier = Modifier.padding(top = 10.dp),
                action = action
            )
        }
    }
}

@Composable
fun LexicalDetailContentView(
    modifier: Modifier = Modifier,
    question: QuestionChoice,
    action: (Pair<LexicalDetailActionScreen, String>) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fav),
                    contentDescription = null,
                    colorFilter =
                    ColorFilter.tint(
                        MaterialTheme.colors.primary
                    )
                )
                Text(
                    text = stringResource(id = R.string.lexical_header),
                    color = MaterialTheme.colors.primary,
                    style =
                    TextStyle(
                        fontFamily = fonts,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            Text(
                text = question.content,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.fillMaxWidth(0.85f)
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                    Modifier.clickable {
                        action(Pair(LexicalDetailActionScreen.OPEN_DEF, question.id))
                    }.height(56.dp).fillMaxWidth().padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(33.dp),
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lexical_def),
                            contentDescription = null,
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                        Text(
                            annotatedStringResource(
                                id = R.string.lexical_definition
                            ),
                            modifier = Modifier,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.score_detail),
                        contentDescription = null,
                        modifier = Modifier,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                    )
                }
                Divider(thickness = 1.dp, startIndent = 75.dp)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                    Modifier.clickable {
                        action(Pair(LexicalDetailActionScreen.OPEN_TIPS, question.id))
                    }.height(56.dp).fillMaxWidth().padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(33.dp),
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lexical_expert),
                            contentDescription = null,
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                        Text(
                            annotatedStringResource(
                                id = R.string.lexical_expert_tips
                            ),
                            modifier = Modifier,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.score_detail),
                        contentDescription = null,
                        modifier = Modifier,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                    )
                }
                Divider(thickness = 1.dp, startIndent = 75.dp)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                    Modifier.clickable {
                        action(Pair(LexicalDetailActionScreen.OPEN_EXP, question.id))
                    }.height(56.dp).fillMaxWidth().padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(33.dp),
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lexical_experience),
                            contentDescription = null,
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.primary
                            )
                        )
                        Text(
                            annotatedStringResource(
                                id = R.string.lexical_experience
                            ),
                            modifier = Modifier.fillMaxWidth(0.9f),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.score_detail),
                        contentDescription = null,
                        modifier = Modifier,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                    )
                }
                Divider(thickness = 1.dp, startIndent = 75.dp)
            }
        }
        Image(painter = painterResource(id = R.drawable.great_idea), contentDescription = null)
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun LexiqueDetailPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CommonCardBackgroundView(modifier = Modifier.fillMaxSize()) {
                LexicalDetailContentView(
                    modifier = Modifier.padding(top = 10.dp),
                    question =
                    QuestionChoice(
                        id = "1",
                        content = "Une question Une question Une question Une question Une question Une question",
                        detail = null,
                        game = GameChoice.DESIGN,
                        answers = emptyList(),
                        responseTime = 0L,
                        lang = currentLanguage,
                        illustration = null,
                        userSelectedAnswer = AnswerChoice(
                            id = "1",
                            content = "1",
                            isCorrect = false
                        )
                    ),
                    action = {}
                )
            }
        }
    }
}
