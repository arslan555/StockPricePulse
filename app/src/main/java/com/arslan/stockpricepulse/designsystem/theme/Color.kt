package com.arslan.stockpricepulse.designsystem.theme

import androidx.compose.ui.graphics.Color

// Material Design 3 colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Stock Price Pulse specific colors - Fintech Dark Theme
object StockPriceColors {
    // Primary Background Colors
    /** Very dark navy background for main card */
    val primaryBackground = Color(0xFF0F1217)

    // Price Movement Colors
    /** Green color for price increase/up movement */
    val green = Color(0xFF4CAF50)
    
    /** Red color for price decrease/down movement */
    val red = Color(0xFFEF5350)
    /** Gray color for unchanged/neutral price movement */
    val gray = Color(0xFF9E9E9E)
    
    // Text Colors
    /** Primary text color - white */
    val textPrimary = Color(0xFFFFFFFF)
    
    /** Secondary/muted text color */
    val textSecondary = Color(0xFFB0B0B0)

    /** Connection status - Connected (green) */
    val connected = Color(0xFF4CAF50)
    
    /** Connection status - Disconnected (red) */
    val disconnected = Color(0xFFF44336)
    
    /** Connection status - Connecting (orange/amber) */
    val connecting = Color(0xFFFF9800)
    
    // Status Colors
    /** Error color */
    val error = Color(0xFFD32F2F)

    // Divider Colors
    /** Divider color between rows */
    val divider = Color(0xFF3A3A3A)
    
    // Button Colors
    /** Toggle button background (blue-ish) */
    val toggleButtonBackground = Color(0xFF1976D2)
}