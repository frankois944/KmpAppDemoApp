package com.cacd2.cacdgame.android.features.onboarding.ui

import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.utility.annotatedStringResource
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@Composable
fun OnBoardingPage1View(
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject(),
    nextPage: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(rememberScrollState()).height(IntrinsicSize.Max),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.onbaording1),
            contentDescription = null,
            modifier =
            Modifier
                .fillMaxWidth(0.70f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text =
                annotatedStringResource(
                    id = R.string.onboarding_1_content,
                    styles =
                    mapOf(
                        Typeface.ITALIC to
                            SpanStyle(
                                color = MaterialTheme.colors.primary,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                    )
                ),
                modifier = Modifier.fillMaxWidth(0.8f),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start,
                lineHeight = 29.sp
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Button(
                    onClick = nextPage,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                ) {
                    Text(text = stringResource(id = R.string.onboarding_next_page).uppercase())
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4", showSystemUi = true)
@Composable
fun OnBoardingPage1Preview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnBoardingPage1View(
                modifier = Modifier.fillMaxSize(),
                appContext = AppContext(),
                nextPage = {}
            )
        }
    }
}
