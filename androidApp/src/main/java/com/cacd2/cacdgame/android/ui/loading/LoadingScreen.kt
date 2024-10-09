package com.cacd2.cacdgame.android.ui.loading

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.UIState
import com.cacd2.cacdgame.android.ui.LoadingAnimationView
import com.cacd2.cacdgame.datasource.api.game.DatoCMSAPIError

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    error: UIState.Error?,
    onErrorAction: (() -> Unit)? = null
) {
    Column {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (error != null) {
                    val message =
                        when (error.exception) {
                            is DatoCMSAPIError.ApiError -> stringResource(
                                id = R.string.network_error
                            )
                            else -> stringResource(id = R.string.generic_error)
                        }
                    val image =
                        when (error.exception) {
                            is DatoCMSAPIError.ApiError -> R.drawable.server_error
                            else -> R.drawable.network_error
                        }
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier.heightIn(max = 300.dp)
                    )
                    Text(
                        text = message,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        textAlign = TextAlign.Center
                    )
                    onErrorAction?.let { action ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = action, shape = RoundedCornerShape(50)) {
                                Box(
                                    modifier = Modifier,
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(
                                            id = R.string.retry_button
                                        ).uppercase()
                                    )
                                }
                            }
                        }
                    }
                } else {
                    LoadingAnimationView(modifier = Modifier.height(300.dp))
                    Text(
                        stringResource(id = R.string.loading_in_progress),
                        modifier = Modifier.fillMaxWidth(0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.sizeIn(maxWidth = 112.dp, maxHeight = 44.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.branding),
                    contentDescription = null,
                    modifier = Modifier.sizeIn(maxWidth = 200.dp, maxHeight = 80.dp)
                )
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun LoadingScreenPreview() {
    AppTheme {
        Surface {
            LoadingScreen(modifier = Modifier.fillMaxSize(), error = null)
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun LoadingScreenErrorWithError1Preview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoadingScreen(
                error = UIState.Error(DatoCMSAPIError.NetworkError(Exception("msg")))
            ) {}
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun LoadingScreenErrorWithError2Preview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoadingScreen(
                error = UIState.Error(DatoCMSAPIError.ApiError(Exception("msg")))
            ) {}
        }
    }
}

@Preview(
    device = "id:pixel_4",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun LoadingScreenDarkPreview() {
    AppTheme {
        Surface {
            LoadingScreen(modifier = Modifier.fillMaxSize(), error = null)
        }
    }
}

@Preview(
    device = "id:pixel_4",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun LoadingScreenErrorWithError1DarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoadingScreen(
                error = UIState.Error(DatoCMSAPIError.NetworkError(Exception("msg")))
            ) {}
        }
    }
}

@Preview(
    device = "id:pixel_4",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun LoadingScreenErrorWithError2DarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoadingScreen(
                error = UIState.Error(DatoCMSAPIError.ApiError(Exception("msg")))
            ) {}
        }
    }
}
