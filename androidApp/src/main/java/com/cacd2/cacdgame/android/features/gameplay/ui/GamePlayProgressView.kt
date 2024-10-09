package com.cacd2.cacdgame.android.features.gameplay.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cacd2.cacdgame.android.AppTheme

/**
 * Created by frankois on 15/05/2023.
 */
@Composable
fun GamePlayProgressView(modifier: Modifier = Modifier, currentQuestion: Int, totalQuestion: Int) {
    val progressionValue = currentQuestion.toFloat() / totalQuestion.toFloat()

    Column(modifier = modifier.fillMaxWidth()) {
        /*Row(
            modifier = Modifier.fillMaxWidth().padding(end = 10.dp, top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "$currentQuestion",
                color = MaterialTheme.colors.primary,
                style = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )
            Text(
                "/$totalQuestion",
                style = TextStyle(
                    fontFamily = fonts,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }*/
        LinearProgressIndicator(
            progress = progressionValue,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(10.dp),
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview
@Composable
fun GamePlayProgressPreview() {
    AppTheme {
        Surface {
            GamePlayProgressView(currentQuestion = 10, totalQuestion = 20)
        }
    }
}
