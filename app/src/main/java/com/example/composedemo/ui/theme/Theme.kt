package com.example.composedemo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ── Light Color Scheme ─────────────────────────────────────
private val LightColorScheme = lightColorScheme(

    // Primary
    primary = Blue40,
    onPrimary = White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,

    // Secondary
    secondary = Teal40,
    onSecondary = White,
    secondaryContainer = Teal90,
    onSecondaryContainer = Teal20,

    // Tertiary — gold accent
    tertiary = Gold40,
    onTertiary = White,
    tertiaryContainer = Gold90,
    onTertiaryContainer = Gold20,

    // Error
    error = Red40,
    onError = White,
    errorContainer = Red90,
    onErrorContainer = Red40,

    // Background
    background = Grey99,
    onBackground = Grey10,

    // Surface
    surface = White,
    onSurface = Grey10,
    surfaceVariant = GreyBlue90,
    onSurfaceVariant = GreyBlue30,

    // Outline
    outline = GreyBlue50,
    outlineVariant = GreyBlue80,
)

// ── Dark Color Scheme ──────────────────────────────────────
private val DarkColorScheme = darkColorScheme(

    // Primary
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,

    // Secondary
    secondary = Teal80,
    onSecondary = Teal20,
    secondaryContainer = Teal30,
    onSecondaryContainer = Teal90,

    // Tertiary
    tertiary = Gold80,
    onTertiary = Gold20,
    tertiaryContainer = Gold30,
    onTertiaryContainer = Gold90,

    // Error
    error = Red80,
    onError = Red40,
    errorContainer = Red40,
    onErrorContainer = Red90,

    // Background
    background = Grey10,
    onBackground = Grey90,

    // Surface
    surface = Grey20,
    onSurface = Grey90,
    surfaceVariant = GreyBlue20,
    onSurfaceVariant = GreyBlue80,

    // Outline
    outline = GreyBlue50,
    outlineVariant = GreyBlue30,
)

// ── Extended Colors — semantic colors not in Material 3 ───
// For positive/negative rate indicators
data class ExtendedColors(
    val positive: Color,
    val onPositive: Color,
    val negative: Color,
    val onNegative: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        positive = Color.Unspecified,
        onPositive = Color.Unspecified,
        negative = Color.Unspecified,
        onNegative = Color.Unspecified,
    )
}

private val LightExtendedColors = ExtendedColors(
    positive = Green40,
    onPositive = White,
    negative = Red40,
    onNegative = White,
)

private val DarkExtendedColors = ExtendedColors(
    positive = Green80,
    onPositive = Grey10,
    negative = Red80,
    onNegative = Grey10,
)

// ── Theme Entry Point ──────────────────────────────────────
@Composable
fun ComposeDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color uses wallpaper colors on Android 12+
    // Disabled by default to preserve our brand colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

// ── Theme Accessor ─────────────────────────────────────────
// Use MaterialTheme.extended.positive instead of LocalExtendedColors.current
object AppTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}