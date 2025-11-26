package com.arslan.stockpricepulse.ui.screens.pricetracker.model

import com.arslan.stockpricepulse.domain.model.PriceMovement

/**
 * UI model representing a stock for display in the Compose UI.
 *
 * @param symbol The stock symbol code (e.g., "AAPL")
 * @param price The formatted price string (e.g., "123.45")
 * @param priceValue The raw price value (for sorting and calculations)
 * @param movement The price movement direction (Up, Down, Unchanged)
 */
data class StockUiModel(
    val symbol: String,
    val price: String,
    val priceValue: Double,
    val movement: PriceMovement
)

