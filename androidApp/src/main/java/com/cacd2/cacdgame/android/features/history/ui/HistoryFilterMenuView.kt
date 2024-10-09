package com.cacd2.cacdgame.android.features.history.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.robotoFonts
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.model.HistoryFilterCriteria

/**
 * Created by francois.dabonot@cacd2.fr on 07/08/2023.
 */
@Composable
fun HistoryFilterMenuView(
    expanded: Boolean,
    filterCriteria: HistoryFilterCriteria,
    onUpdate: (HistoryFilterCriteria) -> Unit,
    onDismiss: (HistoryFilterCriteria) -> Unit
) {

    var design by remember { mutableStateOf(filterCriteria.designSelected) }
    var tech by remember { mutableStateOf(filterCriteria.techSelected) }
    var product by remember { mutableStateOf(filterCriteria.productSelected) }
    var mix by remember { mutableStateOf(filterCriteria.mixSelected) }
    var availableCategory by remember { mutableStateOf<List<GameChoice>>(emptyList()) }

    LaunchedEffect(Unit) {
        availableCategory = GameChoice.getAvailableHistoryChoice()
    }


    fun forwardData() {
        onUpdate(
            HistoryFilterCriteria(
                designSelected = design,
                techSelected = tech,
                productSelected = product,
                mixSelected = mix
            )
        )
    }

    fun dismissFilter() {
        onDismiss(
            HistoryFilterCriteria(
                designSelected = design,
                techSelected = tech,
                productSelected = product,
                mixSelected = mix
            )
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { dismissFilter() },
        offset = DpOffset(x = (-10).dp, y = (-30).dp)
    ) {
        if (availableCategory.contains(GameChoice.DESIGN)) {
            DropdownMenuItem(
                enabled = true,
                onClick = {
                    design = !design
                    forwardData()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.padding(top = 2.dp),
                        checked = design,
                        onCheckedChange = null,
                        colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary,
                            disabledIndeterminateColor = MaterialTheme.colors.primary,
                            uncheckedColor = MaterialTheme.colors.primary
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.selection_quiz_design),
                        modifier = Modifier,
                        style =
                        TextStyle(
                            fontFamily = robotoFonts,
                            fontSize = 15.sp,
                            platformStyle =
                            PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        lineHeight = 18.sp
                    )
                }
            }
        }
        if (availableCategory.contains(GameChoice.TECH)) {
            DropdownMenuItem(
                enabled = true,
                onClick = {
                    tech = !tech
                    forwardData()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.padding(top = 2.dp),
                        checked = tech,
                        onCheckedChange = null,
                        colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary,
                            disabledIndeterminateColor = MaterialTheme.colors.primary,
                            uncheckedColor = MaterialTheme.colors.primary
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.selection_quiz_tech),
                        modifier = Modifier,
                        style =
                        TextStyle(
                            fontFamily = robotoFonts,
                            fontSize = 15.sp,
                            platformStyle =
                            PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        lineHeight = 18.sp
                    )
                }
            }
        }
        if (availableCategory.contains(GameChoice.PRODUCT)) {
            DropdownMenuItem(
                enabled = true,
                onClick = {
                    product = !product
                    forwardData()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.padding(top = 2.dp),
                        checked = product,
                        onCheckedChange = null,
                        colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary,
                            disabledIndeterminateColor = MaterialTheme.colors.primary,
                            uncheckedColor = MaterialTheme.colors.primary
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.selection_quiz_produit),
                        modifier = Modifier,
                        style =
                        TextStyle(
                            fontFamily = robotoFonts,
                            fontSize = 15.sp,
                            platformStyle =
                            PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        lineHeight = 18.sp
                    )
                }
            }
        }
        if (availableCategory.contains(GameChoice.ALL)) {
            DropdownMenuItem(
                enabled = true,
                onClick = {
                    mix = !mix
                    forwardData()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.padding(top = 2.dp),
                        checked = mix,
                        onCheckedChange = null,
                        colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary,
                            disabledIndeterminateColor = MaterialTheme.colors.primary,
                            uncheckedColor = MaterialTheme.colors.primary
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.selection_mix),
                        modifier = Modifier,
                        style =
                        TextStyle(
                            fontFamily = robotoFonts,
                            fontSize = 15.sp,
                            platformStyle =
                            PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryFilterMenuPreview() {
    AppTheme {
        HistoryFilterMenuView(
            expanded = true,
            filterCriteria =
            HistoryFilterCriteria(
                designSelected = true,
                techSelected = true,
                productSelected = true,
                mixSelected = false
            ),
            onUpdate = {
                print(it)
            },
            onDismiss = {
                print(it)
            }
        )
    }
}
