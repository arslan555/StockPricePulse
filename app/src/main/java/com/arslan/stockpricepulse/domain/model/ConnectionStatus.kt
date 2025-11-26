package com.arslan.stockpricepulse.domain.model

/**
 * Represents the WebSocket connection status.
 */
sealed interface ConnectionStatus {
    /**
     * WebSocket is connected and ready to send/receive messages.
     */
    data object Connected : ConnectionStatus

    /**
     * WebSocket is disconnected.
     */
    data object Disconnected : ConnectionStatus

    /**
     * WebSocket is in the process of connecting.
     */
    data object Connecting : ConnectionStatus

    /**
     * WebSocket connection failed or encountered an error.
     */
    data class Error(val message: String) : ConnectionStatus
}

