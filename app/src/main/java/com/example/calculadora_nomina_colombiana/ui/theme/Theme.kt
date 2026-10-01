package com.example.calculadora_nomina_colombiana.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TwitchPurple,
    onPrimary = ButtonWhite,
    primaryContainer = TwitchPurpleFocus,
    onPrimaryContainer = ButtonWhite,
    inversePrimary = TwitchPurpleLight,
    secondary = TwitchPurpleFocus,
    onSecondary = ButtonWhite,
    secondaryContainer = TwitchSurfaceElevated,
    onSecondaryContainer = TwitchWhite,
    tertiary = TwitchPurpleLight,
    onTertiary = PitchBlack,
    tertiaryContainer = TwitchSurfaceElevated,
    onTertiaryContainer = TwitchWhite,
    background = PitchBlack,
    onBackground = TwitchWhite,
    surface = PitchBlack,
    onSurface = TwitchWhite,
    surfaceVariant = TwitchSurfaceElevated,
    onSurfaceVariant = TwitchTextSecondary,
    surfaceContainerLowest = PitchBlack,
    surfaceContainerLow = PitchBlack,
    surfaceContainer = TwitchSurface,
    surfaceContainerHigh = TwitchSurfaceElevated,
    surfaceContainerHighest = TwitchSurfaceElevated,
    surfaceTint = TwitchPurple,
    inverseSurface = TwitchWhite,
    inverseOnSurface = PitchBlack,
    outline = TwitchTextSecondary,
    outlineVariant = TwitchSurfaceElevated,
    scrim = PitchBlack,
    error = ErrorAccent,
    onError = ButtonWhite,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = TwitchPurpleFocus,
    onPrimary = ButtonWhite,
    primaryContainer = TwitchPurpleLight,
    onPrimaryContainer = PitchBlack,
    secondary = TwitchPurpleFocus,
    onSecondary = ButtonWhite,
    tertiary = TwitchPurple,
    onTertiary = ButtonWhite,
    background = TwitchWhite,
    onBackground = PitchBlack,
    surface = TwitchWhite,
    onSurface = PitchBlack,
    surfaceVariant = TwitchSurface,
    onSurfaceVariant = TwitchSurfaceElevated
)

@Composable
fun Calculadora_Nomina_ColombianaTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
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
