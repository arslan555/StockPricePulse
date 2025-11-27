package com.arslan.stockpricepulse.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark theme Material colors - using StockPriceColors palette
private val DarkColorScheme = darkColorScheme(
    primary = DarkStockPriceColors.toggleButtonBackground, // Blue for primary actions
    secondary = DarkStockPriceColors.green, // Green for secondary actions
    tertiary = DarkStockPriceColors.connecting, // Orange for tertiary actions
    background = DarkStockPriceColors.screenBackground, // Dark background
    surface = DarkStockPriceColors.primaryBackground, // Dark surface
    onBackground = DarkStockPriceColors.textPrimary, // White text on background
    onSurface = DarkStockPriceColors.textPrimary // White text on surface
)

// Light theme Material colors - using StockPriceColors palette
private val LightColorScheme = lightColorScheme(
    primary = LightStockPriceColors.toggleButtonBackground, // Blue for primary actions
    secondary = LightStockPriceColors.green, // Green for secondary actions
    tertiary = LightStockPriceColors.connecting, // Orange for tertiary actions
    background = LightStockPriceColors.screenBackground, // Light background
    surface = LightStockPriceColors.primaryBackground, // White surface
    onBackground = LightStockPriceColors.textPrimary, // Dark text on background
    onSurface = LightStockPriceColors.textPrimary // Dark text on surface
)

@Composable
fun StockPricePulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use custom theme colors
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

    // Select the appropriate StockPriceColors palette based on theme
    val stockPriceColors = if (darkTheme) {
        DarkStockPriceColors
    } else {
        LightStockPriceColors
    }

    // Set status bar color to match top bar (primaryBackground)
    val view = LocalView.current
    val statusBarColor = stockPriceColors.primaryBackground
    val isLightStatusBar = !darkTheme // Light icons for dark theme, dark icons for light theme

    SideEffect {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            it.statusBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = isLightStatusBar
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(LocalStockPriceColors provides stockPriceColors) {
            content()
        }
    }
}