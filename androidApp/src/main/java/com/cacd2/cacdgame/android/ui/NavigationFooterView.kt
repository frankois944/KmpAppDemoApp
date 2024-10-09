package com.cacd2.cacdgame.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */
@Composable
fun NavigationFooterView(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.End,
    onGoForward: (() -> Unit)?,
    onGoBack: (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        onGoBack?.let {
            IconButton(onClick = it) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Go Next"
                )
            }
        }
        onGoForward?.let {
            IconButton(onClick = it, modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_next),
                    contentDescription = "Go Next"
                )
            }
        }
    }
}

@Preview
@Composable
fun GameSelectionFooterPreview() {
    AppTheme {
        Surface {
            NavigationFooterView(onGoBack = { }, onGoForward = { })
        }
    }
}
