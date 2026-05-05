package com.example.zoomrad.ui.theme

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
    primary = GreenPrimary,
    onPrimary = Color.White,
    background = DarkBg,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = Color(0xFF37474F),
    onSurfaceVariant = DarkTextSecondary,
    secondaryContainer = Color(0xFF1D5A4E), // Gradient Start
    onSecondaryContainer = Color(0xFF144238), // Gradient End
    outline = Color(0xFF455A64)
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    background = LightBg,
    onBackground = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = TextGray,
    secondaryContainer = Color(0xFFD6F5EF), // Gradient Start
    onSecondaryContainer = Color(0xFFA7E6D9), // Gradient End
    outline = BorderColor
)

@Composable
fun QuoteReminderTheme(
    appTheme: AppTheme = AppTheme.System,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to prioritize our custom colors
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when (appTheme) {
        is AppTheme.Light -> LightColorScheme
        is AppTheme.Dark -> DarkColorScheme
        is AppTheme.System -> {
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (darkTheme) DarkColorScheme else LightColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}