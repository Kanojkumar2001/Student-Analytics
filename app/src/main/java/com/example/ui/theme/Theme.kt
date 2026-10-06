package com.example.ui.theme

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
    primary = CyanGlow,
    onPrimary = Slate950,
    primaryContainer = IndigoAccent,
    onPrimaryContainer = Color.White,
    secondary = CyanNeon,
    onSecondary = Slate950,
    secondaryContainer = Slate800,
    onSecondaryContainer = Slate200,
    tertiary = AmberWarning,
    onTertiary = Slate950,
    background = Slate950,
    onBackground = Slate50,
    surface = SurfaceCard,
    onSurface = Slate50,
    surfaceVariant = Slate850,
    onSurfaceVariant = Slate400,
    outline = SurfaceCardBorder,
    error = RoseRisk,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Slate950,
    secondary = CyanNeon,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Slate950,
    tertiary = AmberWarning,
    onTertiary = Slate950,
    background = Color(0xFFF8FAFC),
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Slate600,
    outline = Color(0xFFCBD5E1),
    error = RoseRisk,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek analytics dark palette
    dynamicColor: Boolean = false,
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
