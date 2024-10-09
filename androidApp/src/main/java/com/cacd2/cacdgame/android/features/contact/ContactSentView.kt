package com.cacd2.cacdgame.android.features.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.fonts

@Composable
fun ContactSentView(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = MaterialTheme.colors.surface.copy(alpha = 0.7f)) {
        ConstraintLayout {
            val (actionContainer, image) = createRefs()
            Surface(
                modifier =
                Modifier.constrainAs(actionContainer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }.shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ).wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text =
                        stringResource(
                            id = R.string.contact_result_message
                        ),
                        modifier =
                        Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 100.dp, bottom = 40.dp),
                        style =
                        TextStyle(
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.thanks),
                contentDescription = null,
                modifier =
                Modifier.constrainAs(image) {
                    bottom.linkTo(actionContainer.top, margin = (-75).dp)
                    start.linkTo(parent.start, margin = 100.dp)
                    end.linkTo(parent.end, margin = 100.dp)
                }
            )
        }
    }
}

@Preview
@Composable
fun ContactSentPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Cyan
        ) {
            ContactSentView(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
