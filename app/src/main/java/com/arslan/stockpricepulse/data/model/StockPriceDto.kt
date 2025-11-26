package com.arslan.stockpricepulse.data.model

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for stock price information.
 * Used for serialization/deserialization of WebSocket messages.
 *
 * @param symbol The stock symbol code (e.g., "AAPL", "GOOG")
 * @param price The current price of the stock
 * @param previousPrice The previous price before this update
 * @param timestamp The timestamp when this price update was generated (milliseconds since epoch)
 */
@Serializable
data class StockPriceDto(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)

