package com.moodrhythm.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF119ae8), // Key buttons and highlighted elements
    secondary = Color(0xFFfea203), // Secondary buttons or elements that need attention
    tertiary = Color(0xFFfe1f4e), // Warning icons, critical actions, or error messages
    background = Color(0xFF121212), // Main background of the app
    surface = Color(0xFF1E1E1E), // Cards, containers, and surfaces
    onPrimary = Color.White, // Text on primary elements like buttons or highlighted text
    onSecondary = Color(0xFF121212), // Text for secondary buttons or secondary text elements
    onTertiary = Color.White, // Text on elements with the red tertiary color
    onBackground = Color(0xFFf5f4f0), // General text on the dark background
    onSurface = Color(0xFFf5f4f0) // Text on surfaces (cards, containers)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0f8de7),
    secondary = Color(0xFFfba105),
    tertiary = Color(0xFFf32848),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color(0xFF1C1B1F),
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun MoodRhythmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}