package com.cacd2.cacdgame.android.features.gameresult.part2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.gameresult.GameResultActions

/**
 * Created by frankois on 29/05/2023.
 */
@Composable
fun CompanyDetailView(onClose: () -> Unit, onAction: (GameResultActions) -> Unit) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface.copy(alpha = 0.7f))
    ) {
        Surface(
            modifier = Modifier.padding(12.dp).shadow(2.dp),
            shape = RoundedCornerShape(6f)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.padding(end = 12.dp, top = 0.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                        )
                    }
                }
                GameResultPart2View(onAction = onAction)
            }
        }
    }
}

@Preview
@Composable
fun CompanyDetailPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CompanyDetailView(onClose = {}, onAction = {})
        }
    }
}
