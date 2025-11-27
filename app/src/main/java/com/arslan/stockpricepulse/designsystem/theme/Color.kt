package com.arslan.stockpricepulse.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Theme-aware color palette for Stock Price Pulse app.
 * Provides light and dark theme variants for all UI elements.
 */
data class StockPriceColorPalette(
    // Background Colors
    val primaryBackground: Color,
    val screenBackground: Color,

    // Price Movement Colors (same for both themes)
    val green: Color,
    val red: Color,
    val gray: Color,

    // Text Colors
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,

    // Connection Status Colors (same for both themes)
    val connected: Color,
    val disconnected: Color,
    val connecting: Color,
    val error: Color,

    // UI Element Colors
    val divider: Color,
    val toggleButtonBackground: Color,
)

/**
 * Dark theme color palette - Fintech Dark Theme
 */
internal val DarkStockPriceColors = StockPriceColorPalette(
    // Backgrounds
    primaryBackground = Color(0xFF0F1217), // Very dark navy
    screenBackground = Color(0xFF0F1217), // Very dark navy

    // Price Movement (same for both themes)
    green = Color(0xFF4CAF50),
    red = Color(0xFFEF5350),
    gray = Color(0xFF9E9E9E),

    // Text Colors
    textPrimary = Color(0xFFFFFFFF), // White
    textSecondary = Color(0xFFB0B0B0), // Light gray
    textTertiary = Color(0xFF768089), // Muted gray

    // Connection Status (same for both themes)
    connected = Color(0xFF4CAF50),
    disconnected = Color(0xFFF44336),
    connecting = Color(0xFFFF9800),
    error = Color(0xFFD32F2F),

    // UI Elements
    divider = Color(0xFF3A3A3A),
    toggleButtonBackground = Color(0xFF1976D2),
)

/**
 * Light theme color palette - Clean Light Theme
 */
internal val LightStockPriceColors = StockPriceColorPalette(
    // Backgrounds
    primaryBackground = Color(0xFFFFFFFF), // White
    screenBackground = Color(0xFFFFFFFF), // White

    // Price Movement (same for both themes)
    green = Color(0xFF4CAF50),
    red = Color(0xFFEF5350),
    gray = Color(0xFF9E9E9E),

    // Text Colors
    textPrimary = Color(0xFF1C1C1E), // Dark gray/black
    textSecondary = Color(0xFF6D6D70), // Medium gray
    textTertiary = Color(0xFF8E8E93), // Light gray

    // Connection Status (same for both themes)
    connected = Color(0xFF4CAF50),
    disconnected = Color(0xFFF44336),
    connecting = Color(0xFFFF9800),
    error = Color(0xFFD32F2F),

    // UI Elements
    divider = Color(0xFFE5E5E5), // Light gray divider
    toggleButtonBackground = Color(0xFF1976D2),
)

/**
 * Local composition for StockPriceColors to access theme-aware colors.
 */
val LocalStockPriceColors = compositionLocalOf { DarkStockPriceColors }

/**
 * Composable function to get the current theme-aware StockPriceColors.
 * Use this within MaterialTheme to access theme-aware colors.
 */
@Composable
fun stockPriceColors(): StockPriceColorPalette {
    return LocalStockPriceColors.current
}
