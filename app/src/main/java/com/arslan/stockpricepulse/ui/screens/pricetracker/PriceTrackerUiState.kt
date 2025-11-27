package com.arslan.stockpricepulse.ui.screens.pricetracker

import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.ui.screens.pricetracker.model.StockUiModel

/**
 * Represents the UI state in the MVI pattern.
 * This is an immutable data class that holds all the information needed to render the UI.
 *
 * @param stocks List of stock UI models, sorted by price (highest first)
 * @param connectionStatus Current WebSocket connection status
 * @param isLoading Whether the app is currently loading or processing data
 * @param error Error message to display, if any (null when no error)
 */
data class PriceTrackerUiState(
    val stocks: List<StockUiModel> = emptyList(),
    val connectionStatus: ConnectionStatus = ConnectionStatus.Disconnected,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    /**
     * Whether the price feed is currently active (connected and not loading).
     */
    val isFeedActive: Boolean
        get() = connectionStatus is ConnectionStatus.Connected && !isLoading

    /**
     * Whether the start button should be enabled.
     */
    val isStartButtonEnabled: Boolean
        get() = connectionStatus is ConnectionStatus.Disconnected && !isLoading

    /**
     * Whether the stop button should be enabled.
     */
    val isStopButtonEnabled: Boolean
        get() = connectionStatus is ConnectionStatus.Connected && !isLoading
}

