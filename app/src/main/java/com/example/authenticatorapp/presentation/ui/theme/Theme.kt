package com.example.authenticatorapp.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MainBlue,
    secondary = Blue,
    tertiary = LightBlue,

    background = Gray1,
    outline = Gray2,
    secondaryContainer = Gray3,
    outlineVariant = Gray3,
    inverseSurface = Gray4,
    inversePrimary = Gray5,
    surface = Gray6,

    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onPrimaryContainer = Color.White,
    onSecondaryContainer = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    secondary = MainBlue,
    tertiary = LightBlue,
    background = Color(0xFF121212),
    surface = Gray4,

    outline = Gray4,
    secondaryContainer = Gray3,
    outlineVariant = Color(0xFF1E1E1E),
    inverseSurface = Gray6,
    inversePrimary = White,

    onPrimary = Gray2,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onPrimaryContainer = Color(0xFF1E1E1E),
    onSecondaryContainer = Color.Black,
)

@Composable
fun AuthenticatorAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}