package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.repository.StockPriceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for managing WebSocket connection lifecycle.
 * Encapsulates connection and disconnection logic.
 */
class ManageWebSocketConnectionUseCase(
    private val repository: StockPriceRepository
) {
    /**
     * Connects to the WebSocket server and starts receiving price updates.
     *
     * @return Flow of connection status updates
     */
    suspend fun connect(): Flow<ConnectionStatus> {
        return repository.connect()
    }

    /**
     * Disconnects from the WebSocket server and stops receiving updates.
     */
    suspend fun disconnect() {
        repository.disconnect()
    }

    /**
     * Observes the WebSocket connection status.
     *
     * @return Flow of connection status updates
     */
    fun observeConnectionStatus(): Flow<ConnectionStatus> {
        return repository.observeConnectionStatus()
    }

    /**
     * Checks if the WebSocket is currently connected.
     *
     * @return true if connected, false otherwise
     */
    fun isConnected(): Boolean {
        return repository.isConnected()
    }
}

