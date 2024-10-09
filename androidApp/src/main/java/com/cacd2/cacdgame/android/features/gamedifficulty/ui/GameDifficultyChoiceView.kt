package com.cacd2.cacdgame.android.features.gamedifficulty.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.unselectedBorderContentColor
import com.cacd2.cacdgame.android.unselectedContentColor

/**
 * Created by francois.dabonot@cacd2.fr on 05/06/2023.
 */
@Composable
fun GameDifficultyChoiceView(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    image: Painter,
    selectedImage: Painter,
    isSelected: Boolean,
    onSelection: () -> Unit
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSelection()
            }
    ) {
        Image(
            painter =
            if (!isSelected) {
                image
            } else {
                selectedImage
            },
            contentDescription = null,
            modifier = Modifier.padding(top = 7.dp)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = title,
                style =
                TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                ),
                color =
                if (isSelected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onSurface
                }
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                shape = RoundedCornerShape(size = 8.dp),
                border =
                BorderStroke(
                    width = 1.dp,
                    color =
                    if (isSelected) {
                        MaterialTheme.colors.primary
                    } else {
                        MaterialTheme.unselectedBorderContentColor
                    }
                ),
                elevation = if (MaterialTheme.colors.isLight) 0.dp else 10.dp
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = content,
                            style =
                            if (isSelected) {
                                MaterialTheme.typography.subtitle1.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                MaterialTheme.typography.subtitle1
                            },
                            color =
                            if (isSelected) {
                                MaterialTheme.colors.primary
                            } else {
                                MaterialTheme.colors.onSurface
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                if (isSelected) {
                                    MaterialTheme.colors.primary
                                } else {
                                    if (MaterialTheme.colors.isLight) {
                                        Color.LightGray
                                    } else {
                                        Color.DarkGray
                                    }
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun GameDifficultyChoicePreview() {
    AppTheme {
        Surface {
            GameDifficultyChoiceView(
                title = stringResource(id = R.string.level_1),
                content = stringResource(id = R.string.level_novice),
                image = painterResource(id = R.drawable.niveau1),
                selectedImage = painterResource(id = R.drawable.lvl1_selected),
                isSelected = false,
                onSelection = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GameDifficultySelectedChoicePreview() {
    AppTheme {
        Surface {
            GameDifficultyChoiceView(
                title = stringResource(id = R.string.level_1),
                content = stringResource(id = R.string.level_novice),
                image = painterResource(id = R.drawable.niveau1),
                selectedImage = painterResource(id = R.drawable.lvl1_selected),
                isSelected = true,
                onSelection = {}
            )
        }
    }
}
