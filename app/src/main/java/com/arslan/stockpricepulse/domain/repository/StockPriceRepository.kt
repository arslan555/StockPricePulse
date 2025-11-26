package com.arslan.stockpricepulse.domain.repository

import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.model.Stock
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for stock price data operations.
 * Provides abstraction over data sources (WebSocket, cache, etc.).
 */
interface StockPriceRepository {
    /**
     * Connects to the WebSocket server and starts receiving price updates.
     * @return Flow of connection status updates
     */
    suspend fun connect(): Flow<ConnectionStatus>

    /**
     * Disconnects from the WebSocket server and stops receiving updates.
     */
    suspend fun disconnect()

    /**
     * Observes real-time stock price updates.
     * @return Flow of Stock domain models, updated whenever a price change is received
     */
    fun observePriceUpdates(): Flow<Stock>

    /**
     * Observes the WebSocket connection status.
     * @return Flow of connection status updates
     */
    fun observeConnectionStatus(): Flow<ConnectionStatus>

    /**
     * Checks if currently connected to the WebSocket server.
     * @return true if connected, false otherwise
     */
    fun isConnected(): Boolean
}

