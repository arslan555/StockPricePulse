package com.arslan.stockpricepulse.designsystem.theme

import androidx.compose.ui.graphics.Color

// Material Design 3 colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Stock Price Pulse specific colors
object StockPriceColors {
    /** Green color for price increase/up movement */
    val green = Color(0xFF4CAF50)
    
    /** Light green for subtle price increase indicators */
    val lightGreen = Color(0xFF81C784)
    
    /** Red color for price decrease/down movement */
    val red = Color(0xFFF44336)
    
    /** Light red for subtle price decrease indicators */
    val lightRed = Color(0xFFE57373)
    
    /** Gray color for unchanged/neutral price movement */
    val gray = Color(0xFF9E9E9E)
    
    /** Connection status - Connected (green) */
    val connected = Color(0xFF4CAF50)
    
    /** Connection status - Disconnected (red) */
    val disconnected = Color(0xFFF44336)
    
    /** Connection status - Connecting (orange/amber) */
    val connecting = Color(0xFFFF9800)
    
    /** Error color */
    val error = Color(0xFFD32F2F)
    
    /** Success color */
    val success = Color(0xFF388E3C)
    
    /** Background color for price flash animation (green) */
    val flashGreen = Color(0xFFE8F5E9)
    
    /** Background color for price flash animation (red) */
    val flashRed = Color(0xFFFFEBEE)
}