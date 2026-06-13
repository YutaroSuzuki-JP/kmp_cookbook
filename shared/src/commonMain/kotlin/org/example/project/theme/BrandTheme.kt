package org.example.project.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BrandCustomColorScheme(
    val goldAccent: Color,
    val glassCardBackground: Color,
    val glassBorder: Color,
    val chartGradientStart: Color,
    val chartGradientEnd: Color,
    val defaultBlurRadius: Dp
)

val LocalBrandColorScheme = staticCompositionLocalOf {
    BrandCustomColorScheme(
        goldAccent = Color(0xFFE5A93C),
        glassCardBackground = Color(0x22FFFFFF),
        glassBorder = Color(0x44FFFFFF),
        chartGradientStart = Color(0xFFC5A059),
        chartGradientEnd = Color(0xFFE5C483),
        defaultBlurRadius = 16.dp
    )
}

val LightBrandColorScheme = BrandCustomColorScheme(
    goldAccent = Color(0xFFB58920),
    glassCardBackground = Color(0x15000000),
    glassBorder = Color(0x22000000),
    chartGradientStart = Color(0xFFB58920),
    chartGradientEnd = Color(0xFFE5A93C),
    defaultBlurRadius = 12.dp
)

val DarkBrandColorScheme = BrandCustomColorScheme(
    goldAccent = Color(0xFFE5A93C),
    glassCardBackground = Color(0x18FFFFFF),
    glassBorder = Color(0x33FFFFFF),
    chartGradientStart = Color(0xFFE5A93C),
    chartGradientEnd = Color(0xFFFFFDD0),
    defaultBlurRadius = 20.dp
)

@Composable
fun PremiumAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFE5A93C),
            background = Color(0xFF14141E),
            surface = Color(0xFF1E1E2C),
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFB58920),
            background = Color(0xFFF9F9FB),
            surface = Color(0xFFFFFFFF),
            onPrimary = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }

    val brandColorScheme = if (darkTheme) {
        DarkBrandColorScheme
    } else {
        LightBrandColorScheme
    }

    CompositionLocalProvider(
        LocalBrandColorScheme provides brandColorScheme
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}

object BrandTheme {
    val colorScheme: BrandCustomColorScheme
        @Composable
        get() = LocalBrandColorScheme.current
}
