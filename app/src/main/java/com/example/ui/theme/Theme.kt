package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = JoyPinkPrimary,
    onPrimary = Color.White,
    primaryContainer = JoyPinkDark,
    onPrimaryContainer = Color.White,
    secondary = JoyCyan,
    onSecondary = Color.Black,
    secondaryContainer = JoyPurpleDark,
    onSecondaryContainer = Color.White,
    tertiary = JoyGold,
    onTertiary = Color.Black,
    background = JoyDarkBackground,
    onBackground = JoyDarkOnBackground,
    surface = JoyDarkSurface,
    onSurface = JoyDarkOnSurface,
    surfaceVariant = JoyDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC7BED8)
)

private val LightColorScheme = lightColorScheme(
    primary = JoyPinkPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDDE4),
    onPrimaryContainer = JoyPinkDark,
    secondary = JoyPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE9DCF8),
    onSecondaryContainer = JoyPurpleDark,
    tertiary = JoyOrange,
    onTertiary = Color.White,
    background = JoyLightBackground,
    onBackground = JoyLightOnBackground,
    surface = JoyLightSurface,
    onSurface = JoyLightOnSurface,
    surfaceVariant = JoyLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF675F75)
)

@Composable
fun JoyLiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
