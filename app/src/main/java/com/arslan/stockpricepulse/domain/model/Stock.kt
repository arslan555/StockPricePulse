package com.arslan.stockpricepulse.domain.model

/**
 * Domain model representing a stock symbol with its price information.
 *
 * @param symbol The stock symbol code (e.g., "AAPL", "GOOG")
 * @param currentPrice The current price of the stock
 * @param previousPrice The previous price before the last update
 * @param movement The price movement direction (Up, Down, or Unchanged)
 */
data class Stock(
    val symbol: String,
    val currentPrice: Double,
    val previousPrice: Double,
    val movement: PriceMovement
) {
    /**
     * Calculates the price change amount.
     */
    val priceChange: Double
        get() = currentPrice - previousPrice

    /**
     * Calculates the price change percentage.
     */
    val priceChangePercent: Double
        get() = if (previousPrice != 0.0) {
            ((currentPrice - previousPrice) / previousPrice) * 100
        } else {
            0.0
        }
}

