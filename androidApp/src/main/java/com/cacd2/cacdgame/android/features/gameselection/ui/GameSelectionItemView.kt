package com.cacd2.cacdgame.android.features.gameselection.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.model.color
import com.cacd2.cacdgame.android.model.image
import com.cacd2.cacdgame.android.model.title
import com.cacd2.cacdgame.android.unselectedBorderContentColor
import com.cacd2.cacdgame.android.unselectedContentColor
import com.cacd2.cacdgame.model.GameChoice

/**
 * Created by frankois on 15/05/2023.
 */
@Composable
fun GameSelectionItemView(
    modifier: Modifier = Modifier,
    choice: GameChoice,
    selectedGame: GameChoice?,
    onSelectionChoice: (GameChoice) -> Unit
) {
    val isSelected = choice.gameId == selectedGame?.gameId

    Surface(
        modifier =
        modifier.clickable(
            onClick = { onSelectionChoice(choice) }
        ).aspectRatio(1f),
        shape =
        RoundedCornerShape(
            topStart = 11.dp,
            topEnd = 4.dp,
            bottomStart = 4.dp,
            bottomEnd = 11.dp
        ),
        border =
        if (isSelected) {
            BorderStroke(1.dp, MaterialTheme.colors.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.unselectedBorderContentColor)
        },
        color =
        if (isSelected) {
            MaterialTheme.colors.primary.copy(
                alpha = if (MaterialTheme.colors.isLight) 0.12f else 0.06f
            )
        } else {
            if (MaterialTheme.colors.isLight) {
                MaterialTheme.unselectedContentColor
            } else {
                MaterialTheme.colors.surface
            }
        },
        elevation = if (MaterialTheme.colors.isLight) 0.dp else 10.dp
    ) {
        ConstraintLayout {
            val (bgImage, title, check) = createRefs()
            Image(
                painter =
                painterResource(
                    id = R.drawable.check
                ),
                modifier =
                Modifier.constrainAs(check) {
                    top.linkTo(parent.top, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    width = Dimension.fillToConstraints
                },
                colorFilter =
                ColorFilter.tint(
                    MaterialTheme.colors.primary
                ),
                contentDescription = null,
                alpha =
                if (isSelected) {
                    1f
                } else {
                    0f
                }
            )
            Image(
                painter =
                painterResource(
                    id = choice.image(selectedGame)
                ),
                contentDescription = null,
                modifier =
                Modifier.constrainAs(bgImage) {
                    centerTo(parent)
                    top.linkTo(check.bottom, margin = 5.dp)
                    bottom.linkTo(title.top, margin = 6.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.preferredWrapContent
                }
            )
            Text(
                text = stringResource(id = choice.title),
                modifier =
                Modifier.constrainAs(title) {
                    start.linkTo(parent.start, margin = 5.dp)
                    end.linkTo(parent.end, margin = 5.dp)
                    top.linkTo(bgImage.bottom)
                    bottom.linkTo(parent.bottom)
                },
                style =
                if (isSelected) {
                    TextStyle(
                        fontStyle = FontStyle(R.font.robotobold),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                } else {
                    TextStyle(
                        fontStyle = FontStyle(R.font.robotoregular),
                        fontSize = 15.sp
                    )
                },
                color = choice.color(selectedGameId = selectedGame)
            )
        }
    }
}

@Composable
@Preview
fun GameSelectionItemPreview() {
    AppTheme {
        Surface(modifier = Modifier.size(200.dp)) {
            GameSelectionItemView(choice = GameChoice.DESIGN, selectedGame = GameChoice.DESIGN) {
            }
        }
    }
}

@Composable
@Preview
fun GameSelectionItemSelectedPreview() {
    AppTheme {
        Surface(modifier = Modifier.size(200.dp)) {
            GameSelectionItemView(choice = GameChoice.DESIGN, selectedGame = null) {
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GameSelectionItemDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.size(200.dp)) {
            GameSelectionItemView(choice = GameChoice.DESIGN, selectedGame = GameChoice.DESIGN) {
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GameSelectionItemSelectedDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.size(200.dp)) {
            GameSelectionItemView(choice = GameChoice.DESIGN, selectedGame = null) {
            }
        }
    }
}
