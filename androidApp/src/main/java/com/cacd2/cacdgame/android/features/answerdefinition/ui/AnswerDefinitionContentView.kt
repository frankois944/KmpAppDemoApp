package com.cacd2.cacdgame.android.features.answerdefinition.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.fonts
import com.cacd2.cacdgame.android.model.image
import com.cacd2.cacdgame.model.QuestionChoice

/**
 * Created by frankois on 21/05/2023.
 */
@Composable
fun AnswerDefinitionContentView(modifier: Modifier = Modifier, questionChoice: QuestionChoice) {
    var contentScale by remember { mutableStateOf(ContentScale.Crop) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (questionChoice.illustration != null) {
            SubcomposeAsyncImage(
                model =
                ImageRequest.Builder(LocalContext.current)
                    .data(questionChoice.illustration)
                    .crossfade(true)
                    .size(Size.ORIGINAL.width, Dimension(100))
                    .build(),
                contentDescription = null
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }
                    is AsyncImagePainter.State.Error -> {
                        ThumbnailView(painterResource(id = questionChoice.image), false)
                    }
                    else -> {
                        ThumbnailView(painter, true)
                    }
                }
            }
        } else {
            ThumbnailView(painterResource(id = questionChoice.image), false)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = questionChoice.content,
                    style =
                    TextStyle(
                        fontFamily = fonts,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = questionChoice.detail ?: "NO CONTENT",
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 23.sp
                )
            }
        }
    }
}

@Composable
fun ThumbnailView(image: Painter, custom: Boolean) {
    Image(
        painter = image,
        contentDescription = null,
        modifier =
        Modifier
            .height(100.dp)
            .fillMaxWidth(0.9f)
            .padding(top = 8.dp)
            .clip(
                shape =
                RoundedCornerShape(
                    topStart = 11.dp,
                    topEnd = 4.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 11.dp
                )
            ).clipToBounds(),
        contentScale = if (custom) ContentScale.Fit else ContentScale.Crop
    )
}

@Preview
@Composable
fun AnswerDefinitionContentPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AnswerDefinitionContentView(
                questionChoice = QuestionChoice.dummy,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp)
            )
        }
    }
}
