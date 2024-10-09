package com.cacd2.cacdgame.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacd2.cacdgame.model.ScreenMode

val fonts =
    FontFamily(
        Font(R.font.poppinsregular),
        Font(R.font.poppinsbold, weight = FontWeight.Bold),
        Font(R.font.poppinsextrabold, weight = FontWeight.ExtraBold),
        Font(R.font.poppinslight, weight = FontWeight.Light),
        Font(R.font.poppinsitalic, style = FontStyle.Italic),
        Font(R.font.poppinssemibold, weight = FontWeight.SemiBold),
        Font(R.font.poppinsblack, weight = FontWeight.Black),
        Font(R.font.poppinsbolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(R.font.poppinsmedium, weight = FontWeight.Medium)
    )

val robotoFonts =
    FontFamily(
        Font(R.font.robotoregular),
        Font(R.font.robotobold, weight = FontWeight.Bold),
        Font(R.font.poppinsextrabold, weight = FontWeight.ExtraBold),
        Font(R.font.robotolight, weight = FontWeight.Light),
        Font(R.font.robotoitalic, style = FontStyle.Italic),
        Font(R.font.poppinssemibold, weight = FontWeight.SemiBold),
        Font(R.font.robotoblack, weight = FontWeight.Black),
        Font(R.font.robotobolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(R.font.robotomedium, weight = FontWeight.Medium)
    )

@Composable
fun AppTheme(screenMode: ScreenMode = ScreenMode.SYSTEM, content: @Composable () -> Unit) {
    val colorMode =
        screenMode == ScreenMode.DARK ||
            (screenMode == ScreenMode.SYSTEM && isSystemInDarkTheme())
    val colors =
        if (colorMode) {
            darkColors(
                primary = Color(0xFF148532),
                primaryVariant = Color(0xFF148532),
                secondary = Color(0xFF8E59FF),
                secondaryVariant = Color(0xFF8E59FF),
                background = Color(0xFF000000),
                surface = Color(0xFF000000),
                error = Color(0xFFF44336),
                onPrimary = Color(0xFFFFFFFF),
                onSecondary = Color(0xFFFFFFFF),
                onBackground = Color(0xFFFFFFFF),
                onSurface = Color(0xFFFFFFFF),
                onError = Color(0xFFFFFFFF)
            )
        } else {
            lightColors(
                primary = Color(0xFF148532),
                primaryVariant = Color(0xFF148532),
                secondary = Color(0xFF8E59FF),
                secondaryVariant = Color(0xFF8E59FF),
                background = Color(0xFFFFFFFF),
                surface = Color(0xFFFFFFFF),
                error = Color(0xFFF44336),
                onPrimary = Color(0xFFFFFFFF),
                onSecondary = Color(0xFFFFFFFF),
                onBackground = Color(0xFF1E1E1E),
                onSurface = Color(0xFF1E1E1E),
                onError = Color(0xFFFFFFFF)
            )
        }
    val typography =
        Typography(
            defaultFontFamily = FontFamily.Default,
            h1 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.SemiBold,
                fontSize = 37.sp
            ),
            h2 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            ),
            h3 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            h4 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 34.sp,
                letterSpacing = 0.25.sp
            ),
            h5 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                letterSpacing = 0.sp
            ),
            h6 =
            TextStyle(
                fontFamily = robotoFonts,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                letterSpacing = 0.15.sp
            ),
            subtitle1 =
            TextStyle(
                fontFamily = robotoFonts,
                fontSize = 16.sp,
                letterSpacing = 0.15.sp
            ),
            subtitle2 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.1.sp
            ),
            body1 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                letterSpacing = 0.sp
            ),
            body2 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                letterSpacing = 0.sp
            ),
            button =
            TextStyle(
                fontFamily = robotoFonts,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 1.25.sp
            ),
            caption =
            TextStyle(
                fontFamily = robotoFonts,
                fontSize = 12.sp,
                letterSpacing = 0.4.sp
            ),
            overline =
            TextStyle(
                fontFamily = robotoFonts,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
            )
        )
    val shapes =
        Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(0.dp)
        )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

val MaterialTheme.unselectedBorderContentColor: Color
    @Composable
    @ReadOnlyComposable
    get() {
        return if (colors.isLight) {
            Color(0xFFE8E8E8)
        } else {
            Color(0xFF505051)
        }
    }

val MaterialTheme.unselectedContentColor: Color
    @Composable
    @ReadOnlyComposable
    get() {
        return if (colors.isLight) {
            Color(0xFFF6F6F6)
        } else {
            Color(0xFFF6F6F6)
        }
    }

val MaterialTheme.defaultStatusBarColor: Color
    @Composable
    get() {
        return LocalElevationOverlay.current?.apply(
            color = colors.primarySurface,
            elevation = 1.dp
        ) ?: colors.primarySurface
    }

val MaterialTheme.emptyStatusBarColor: Color
    @Composable
    @ReadOnlyComposable
    get() {
        return colors.background
    }
