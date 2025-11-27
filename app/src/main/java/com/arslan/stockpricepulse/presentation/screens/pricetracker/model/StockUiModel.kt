package com.arslan.stockpricepulse.presentation.screens.pricetracker.model

import com.arslan.stockpricepulse.domain.model.PriceMovement

/**
 * UI model representing a stock for display in the Compose UI.
 *
 * @param symbol The stock symbol code (e.g., "AAPL")
 * @param companyName The full company name (e.g., "Apple Inc.")
 * @param price The formatted price string (e.g., "$174.55")
 * @param priceValue The raw price value (for sorting and calculations)
 * @param priceChange The absolute price change amount (e.g., 1.25)
 * @param priceChangePercent The price change percentage (e.g., 0.72)
 * @param movement The price movement direction (Up, Down, Unchanged)
 */
data class StockUiModel(
    val symbol: String,
    val companyName: String,
    val price: String,
    val priceValue: Double,
    val priceChange: Double,
    val priceChangePercent: Double,
    val movement: PriceMovement
)

