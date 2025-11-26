package com.arslan.stockpricepulse.data.datasource.network

import com.arslan.stockpricepulse.data.model.StockPriceDto
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import kotlinx.coroutines.flow.Flow

/**
 * Interface for WebSocket data source operations.
 * Handles connection, sending/receiving messages, and connection status.
 */
interface WebSocketDataSource {
    /**
     * Connects to the WebSocket server.
     * @return Flow of connection status updates
     */
    suspend fun connect(): Flow<ConnectionStatus>

    /**
     * Disconnects from the WebSocket server.
     */
    suspend fun disconnect()

    /**
     * Sends a price update message via WebSocket.
     * @param priceUpdate The stock price update to send
     */
    suspend fun sendPriceUpdate(priceUpdate: StockPriceDto)

    /**
     * Observes incoming price update messages from the WebSocket.
     * @return Flow of received StockPriceDto updates
     */
    fun observePriceUpdates(): Flow<StockPriceDto>

    /**
     * Observes the connection status.
     * @return Flow of connection status updates
     */
    fun observeConnectionStatus(): Flow<ConnectionStatus>

    /**
     * Checks if the WebSocket is currently connected.
     */
    fun isConnected(): Boolean
}

