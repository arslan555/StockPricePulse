package com.arslan.stockpricepulse.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arslan.stockpricepulse.R

// Custom Font Families
val GolosFontFamily = FontFamily(
    Font(R.font.golos, FontWeight.Normal)
)

val SoraFontFamily = FontFamily(
    Font(R.font.sora, FontWeight.Normal)
)

// Set of Material typography styles
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = GolosFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Stock Price Pulse specific typography styles - Fintech Dark Theme.
 * Uses Golos font for headings and Sora font for body text.
 * Numbers use thin/light font weight.
 */
object StockPriceTypography {
    /** Typography for stock symbol (e.g., "AAPL") - Golos Bold, ~16sp */
    val symbolText = TextStyle(
        fontFamily = GolosFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )

    /** Typography for company name (e.g., "Apple Inc.") - Sora Medium, ~13sp */
    val companyNameText = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )

    /** Typography for stock price (e.g., "$174.55") - Sora Thin/Light for numbers, ~17sp */
    val priceText = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Thin, // Thin weight for numbers
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    )

    /** Typography for profit/loss (e.g., "+1.25 (0.72%)") - Sora Light for numbers, ~13sp */
    val priceChangeText = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Light, // Light weight for numbers
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )

    /** Typography for header title (e.g., "Stock Price Pulse") - Golos SemiBold */
    val headerTitleText = TextStyle(
        fontFamily = GolosFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    )

    /** Typography for connection status text - Sora Medium */
    val statusText = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )

    /** Typography for button text - Sora SemiBold */
    val buttonText = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    /** Typography for error messages - Sora Normal */
    val errorText = TextStyle(
        fontFamily = SoraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
}