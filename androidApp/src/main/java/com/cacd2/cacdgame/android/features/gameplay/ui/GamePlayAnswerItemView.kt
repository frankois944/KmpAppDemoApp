package com.cacd2.cacdgame.android.features.gameplay.ui

import AutoSizeText
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.unselectedBorderContentColor
import com.cacd2.cacdgame.android.unselectedContentColor
import com.cacd2.cacdgame.model.AnswerChoice
import com.cacd2.cacdgame.model.QuestionChoice

/**
 * Created by frankois on 15/05/2023.
 */
@Composable
fun GamePlayAnswerItemView(
    modifier: Modifier = Modifier,
    answer: AnswerChoice,
    selectedAnswerChoice: AnswerChoice?,
    onSelectAnswer: (AnswerChoice) -> Unit
) {
    val isSelected = selectedAnswerChoice == answer
    val forceSelectAnswer =
        (selectedAnswerChoice != null && !selectedAnswerChoice.isCorrect) &&
            answer.isCorrect
    var multiplier by remember { mutableFloatStateOf(1f) }
    Surface(
        shape =
        RoundedCornerShape(
            topStart = 11.dp,
            topEnd = 4.dp,
            bottomStart = 4.dp,
            bottomEnd = 11.dp
        ),
        color =
        if (isSelected || forceSelectAnswer) {
            if (answer.isCorrect) {
                MaterialTheme.colors.primary.copy(
                    alpha = if (MaterialTheme.colors.isLight) 0.12f else 0.06f
                )
            } else {
                MaterialTheme.colors.error.copy(0.12f)
            }
        } else {
            if (MaterialTheme.colors.isLight) {
                MaterialTheme.unselectedContentColor
            } else {
                MaterialTheme.colors.surface
            }
        },
        border = BorderStroke(
            1.dp,
            if (isSelected || forceSelectAnswer) {
                if (answer.isCorrect) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.error
                }
            } else {
                MaterialTheme.unselectedBorderContentColor
            }
        ),
        modifier =
        modifier.clickable(
            onClick = {
                onSelectAnswer(answer)
            }
        ).aspectRatio(1f),
        elevation =
        if (isSelected || forceSelectAnswer) {
            0.dp
        } else {
            10.dp
        }
    ) {
        val constraintSet =
            ConstraintSet {
                val check = createRefFor("check")
                val text = createRefFor("text")

                constrain(check) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }

                constrain(text) {
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                }
                createVerticalChain(
                    check,
                    text,
                    chainStyle = ChainStyle.SpreadInside
                )
                // val horizontalChain = createHorizontalChain(check, text)
            }
        ConstraintLayout(constraintSet = constraintSet) {
            Image(
                painter =
                if (answer.isCorrect) {
                    painterResource(
                        id = R.drawable.check
                    )
                } else {
                    painterResource(
                        id = R.drawable.error_check
                    )
                },
                colorFilter =
                if (!answer.isCorrect) {
                    ColorFilter.tint(
                        MaterialTheme.colors.error
                    )
                } else {
                    ColorFilter.tint(
                        MaterialTheme.colors.primary
                    )
                },
                contentDescription = null,
                alpha =
                if (isSelected || forceSelectAnswer) {
                    1f
                } else {
                    0f
                },
                modifier = Modifier.layoutId("check").padding(top = 8.dp)
            )
            Box(
                modifier =
                Modifier
                    .layoutId("text")
                    .padding(bottom = 18.dp)
                    .heightIn(min = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                AutoSizeText(
                    text = answer.content,
                    textAlign = TextAlign.Center,
                    style =
                    if (isSelected && answer.isCorrect || forceSelectAnswer) {
                        TextStyle(
                            fontFamily = fonts,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    } else {
                        TextStyle(
                            fontFamily = fonts,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    },
                    color =
                    if (isSelected || forceSelectAnswer) {
                        if (answer.isCorrect) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.error
                        }
                    } else {
                        MaterialTheme.colors.onSurface
                    }
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun GamePlayAnswerItemNormalPreview() {
    AppTheme {
        Surface {
            GamePlayAnswerItemView(
                answer = QuestionChoice.dummy.answers[0],
                selectedAnswerChoice = null
            ) {
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GamePlayAnswerItemSelectOKPreview() {
    AppTheme {
        Surface {
            GamePlayAnswerItemView(
                answer = QuestionChoice.dummy.answers[0],
                selectedAnswerChoice = QuestionChoice.dummy.answers[0]
            ) {
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GamePlayAnswerItemSelectKOPreview() {
    val answer = AnswerChoice("42", "performances applicatives\n".repeat(40), false)
    AppTheme {
        Surface {
            GamePlayAnswerItemView(
                answer = answer,
                selectedAnswerChoice = answer
            ) {
            }
        }
    }
}
