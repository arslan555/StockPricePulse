package com.arslan.stockpricepulse.presentation.screens.pricetracker

/**
 * Represents user intents/actions in the MVI pattern.
 * These are events triggered by user interactions or system events.
 */
sealed interface PriceTrackerIntent {
    /**
     * User wants to start the price feed (connect WebSocket and start receiving updates).
     */
    data object StartPriceFeed : PriceTrackerIntent

    /**
     * User wants to stop the price feed (disconnect WebSocket and stop receiving updates).
     */
    data object StopPriceFeed : PriceTrackerIntent

    /**
     * A price update was received from the WebSocket.
     *
     * @param symbol The stock symbol that was updated
     * @param price The new price value
     * @param previousPrice The previous price value
     */
    data class PriceUpdateReceived(
        val symbol: String,
        val price: Double,
        val previousPrice: Double
    ) : PriceTrackerIntent

    /**
     * Connection status changed (connected, disconnected, error, etc.).
     *
     * @param isConnected Whether the WebSocket is currently connected
     */
    data class ConnectionStatusChanged(
        val isConnected: Boolean
    ) : PriceTrackerIntent

    /**
     * An error occurred that should be displayed to the user.
     *
     * @param message The error message
     */
    data class ErrorOccurred(
        val message: String
    ) : PriceTrackerIntent
}

