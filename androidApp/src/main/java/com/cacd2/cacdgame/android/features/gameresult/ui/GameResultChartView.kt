package com.cacd2.cacdgame.android.features.gameresult.ui

import android.content.res.Configuration
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.toSpanned
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

/**
 * Created by frankois on 25/05/2023.
 */
@Composable
fun GameResultChartView(
    ok: Long,
    ko: Long,
    modifier: Modifier = Modifier,
    fontSizeCorrectQuestion: Float,
    fontSizeTotalQuestion: Float,
    withIcons: Boolean
) {
    val okColor = MaterialTheme.colors.primary
    val koColor = MaterialTheme.colors.error

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PieChart(ctx).apply {
                layoutParams =
                    LinearLayout.LayoutParams(
                        // on below line we are specifying layout
                        // params as MATCH PARENT for height and width.
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                val entries = ArrayList<PieEntry>()
                entries.add(
                    if (withIcons) {
                        PieEntry(
                            ko.toFloat(),
                            AppCompatResources.getDrawable(context, R.drawable.star_empty)
                        )
                    } else {
                        PieEntry(
                            ko.toFloat()
                        )
                    }
                )
                entries.add(
                    if (withIcons) {
                        PieEntry(
                            ok.toFloat(),
                            AppCompatResources.getDrawable(context, R.drawable.star_full)
                        )
                    } else {
                        PieEntry(
                            ok.toFloat()
                        )
                    }
                )
                val dataSet = PieDataSet(entries, "")
                dataSet.setDrawIcons(true)
                dataSet.sliceSpace = 0f
                dataSet.selectionShift = 0f

                val colors = ArrayList<Int>()
                colors.add(koColor.toArgb())
                colors.add(okColor.toArgb())
                dataSet.colors = colors

                data = PieData(dataSet)
                highlightValues(null)

                holeRadius = 130f / 2f
                transparentCircleRadius = 145 / 2f
                setTransparentCircleColor(Color(0xFF1E1E1E).toArgb())
                setTransparentCircleAlpha(0x26)
                description.isEnabled = false
                legend.isEnabled = false
                setHoleColor(Color.Transparent.toArgb())
                data.apply {
                    setValueTextSize(0f)
                }

                fun generateCenterSpannableText(): Spanned {
                    val first = SpannableString("$ok")
                    first.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        first.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    first.setSpan(
                        ForegroundColorSpan(
                            okColor.toArgb()
                        ),
                        0,
                        first.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    first.setSpan(
                        RelativeSizeSpan(fontSizeCorrectQuestion),
                        0,
                        first.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    first.setSpan(
                        object : TypefaceSpan(null) {
                            override fun updateDrawState(ds: TextPaint) {
                                ds.typeface =
                                    Typeface.create(
                                        ResourcesCompat.getFont(context, R.font.poppinsextrabold),
                                        Typeface.NORMAL
                                    ) // To change according to your need
                            }
                        },
                        0,
                        first.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val second = SpannableString("/${ok + ko}")
                    second.setSpan(
                        StyleSpan(Typeface.NORMAL),
                        0,
                        second.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    second.setSpan(
                        ForegroundColorSpan(okColor.toArgb()),
                        0,
                        second.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    second.setSpan(
                        RelativeSizeSpan(fontSizeTotalQuestion),
                        0,
                        second.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    second.setSpan(
                        object : TypefaceSpan(null) {
                            override fun updateDrawState(ds: TextPaint) {
                                ds.typeface =
                                    Typeface.create(
                                        ResourcesCompat.getFont(context, R.font.poppinslight),
                                        Typeface.NORMAL
                                    ) // To change according to your need
                            }
                        },
                        0,
                        second.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    return SpannableStringBuilder(first).append(second).toSpanned()
                }
                centerText = generateCenterSpannableText()
                setDrawCenterText(true)
            }
        }
    )
}

@Preview
@Composable
fun GameResultChartPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultChartView(
                ok = 4,
                ko = 1,
                modifier = Modifier.size(150.dp),
                fontSizeCorrectQuestion = 3f,
                fontSizeTotalQuestion = 1.5f,
                withIcons = true
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED)
@Composable
fun GameResultChartDarkPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GameResultChartView(
                ok = 4,
                ko = 6,
                modifier = Modifier.size(150.dp),
                fontSizeCorrectQuestion = 3f,
                fontSizeTotalQuestion = 1.5f,
                withIcons = true
            )
        }
    }
}
