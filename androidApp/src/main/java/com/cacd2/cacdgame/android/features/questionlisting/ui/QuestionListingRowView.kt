package com.cacd2.cacdgame.android.features.questionlisting.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.QuestionChoice

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
@Composable
fun QuestionListingRowView(modifier: Modifier = Modifier, item: QuestionChoice) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (image, text, detail) = createRefs()
        when (item.userSelectedAnswer?.isCorrect) {
            true -> {
                Image(
                    painter = painterResource(id = R.drawable.check),
                    colorFilter =
                    ColorFilter.tint(
                        MaterialTheme.colors.primary
                    ),
                    contentDescription = null,
                    modifier =
                    Modifier.constrainAs(image) {
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(text.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }

            false -> {
                Image(
                    painter = painterResource(id = R.drawable.error_check),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
                    contentDescription = null,
                    modifier =
                    Modifier.constrainAs(image) {
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(text.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }

            else -> {
                Spacer(
                    modifier =
                    Modifier.constrainAs(image) {
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(text.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.value(24.dp)
                    }
                )
            }
        }
        Box(
            modifier =
            Modifier.constrainAs(text) {
                start.linkTo(image.end, margin = 32.dp)
                end.linkTo(detail.end, margin = 10.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = item.content,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle1
            )
        }
        Image(
            painter = painterResource(id = R.drawable.score_detail),
            contentDescription = null,
            modifier =
            Modifier.constrainAs(detail) {
                start.linkTo(text.end)
                end.linkTo(parent.end, margin = 16.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
        )
    }
}

@Preview
@Composable
fun QuestionListingRowOKPreview() {
    AppTheme {
        Surface {
            QuestionListingRowView(
                modifier = Modifier.height(56.dp),
                item = QuestionChoice.dummy
            )
        }
    }
}

@Preview
@Composable
fun QuestionListingRowKOPreview() {
    AppTheme {
        Surface {
            QuestionListingRowView(
                modifier = Modifier.height(56.dp),
                item =
                QuestionChoice(
                    id = "1",
                    content = "une question une question une question une question une question une question une question",
                    detail = null,
                    game = GameChoice.DESIGN,
                    answers = emptyList(),
                    responseTime = 0L,
                    illustration = null,
                    userSelectedAnswer = AnswerChoice(id = "1", content = "1", isCorrect = false)
                )
            )
        }
    }
}

@Preview
@Composable
fun QuestionListingRowShortKOPreview() {
    AppTheme {
        Surface {
            QuestionListingRowView(
                modifier = Modifier.height(56.dp),
                item =
                QuestionChoice(
                    id = "1",
                    content = "une question",
                    detail = null,
                    game = GameChoice.DESIGN,
                    answers = emptyList(),
                    responseTime = 0L,
                    illustration = null,
                    userSelectedAnswer = AnswerChoice(id = "1", content = "1", isCorrect = true)
                )
            )
        }
    }
}
