package com.cacd2.cacdgame.android.utility

import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.core.text.HtmlCompat

/**
 * Annotated string resource
 *
 * @param id
 * @param formatArgs
 * @param styles Map<Typeface, SpanStyle> ex: Typeface.BOLD
 * @return
 */
@Composable
fun annotatedStringResource(
    @StringRes id: Int,
    vararg formatArgs: Any,
    styles: Map<Int, SpanStyle>? = null
): AnnotatedString {
    val text =
        if (formatArgs.isNotEmpty()) {
            stringResource(id, *formatArgs)
        } else {
            stringResource(id)
        }
    val spanned =
        remember(text) {
            HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    val primaryColor = MaterialTheme.colors.primary

    return remember(spanned) {
        buildAnnotatedString {
            append(spanned.toString())
            spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
                val start = spanned.getSpanStart(span)
                val end = spanned.getSpanEnd(span)
                when (span) {
                    is StyleSpan ->
                        when (span.style) {
                            Typeface.ITALIC -> {
                                val spanStyle =
                                    styles?.get(Typeface.ITALIC) ?: kotlin.run {
                                        SpanStyle(
                                            color = primaryColor,
                                            fontStyle = FontStyle.Normal,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                addStyle(spanStyle, start, end)
                            }

                            Typeface.BOLD -> {
                                val spanStyle =
                                    styles?.get(Typeface.BOLD) ?: kotlin.run {
                                        SpanStyle(
                                            fontStyle = FontStyle.Normal,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                addStyle(spanStyle, start, end)
                            }
                        /*Typeface.BOLD_ITALIC ->
                            addStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                ),
                                start,
                                end,
                            )*/
                        }
                    /*is UnderlineSpan ->
                        addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
                    is ForegroundColorSpan ->
                        addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)*/
                }
            }
        }
    }
}
