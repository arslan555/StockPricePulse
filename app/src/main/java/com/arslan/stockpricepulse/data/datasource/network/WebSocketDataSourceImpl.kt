package com.arslan.stockpricepulse.data.datasource.network

import com.arslan.stockpricepulse.data.datasource.mock.MockStockPriceDataSource
import com.arslan.stockpricepulse.data.model.PriceUpdateMessageDto
import com.arslan.stockpricepulse.data.model.StockPriceDto
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Implementation of WebSocketDataSource using Ktor WebSocket client.
 * Integrates with MockStockPriceDataSource to send mock price updates.
 */
class WebSocketDataSourceImpl(
    private val mockDataSource: MockStockPriceDataSource
) : WebSocketDataSource {

    companion object {
        /**
         * WebSocket server URL (Postman Echo WebSocket).
         */
        private const val WEBSOCKET_URL = "wss://ws.postman-echo.com/raw"

        /**
         * Interval for sending price updates (2 seconds).
         */
        private const val UPDATE_INTERVAL_MS = 2000L
    }

    private val client = HttpClient {
        install(WebSockets)
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private var webSocketSession: WebSocketSession? = null
    private val connectionStatusFlow = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Disconnected)
    private val priceUpdatesChannel = Channel<StockPriceDto>(Channel.UNLIMITED)
    private var sendingJob: Job? = null
    private var receivingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun connect(): Flow<ConnectionStatus> = flow {
        if (isConnected()) {
            emit(ConnectionStatus.Connected)
            return@flow
        }

        try {
            connectionStatusFlow.value = ConnectionStatus.Connecting
            emit(ConnectionStatus.Connecting)

            webSocketSession = client.webSocketSession {
                url(WEBSOCKET_URL)
            }

            connectionStatusFlow.value = ConnectionStatus.Connected
            emit(ConnectionStatus.Connected)

            // Start receiving messages
            startReceiving()

            // Start sending mock price updates
            startSending()

        } catch (e: Exception) {
            val errorStatus = ConnectionStatus.Error(e.message ?: "Connection failed")
            connectionStatusFlow.value = errorStatus
            emit(errorStatus)
        }
    }

    override suspend fun disconnect() {
        sendingJob?.cancel()
        receivingJob?.cancel()
        
        try {
            webSocketSession?.close()
        } catch (e: Exception) {
            // Ignore close errors
        }
        
        webSocketSession = null
        connectionStatusFlow.value = ConnectionStatus.Disconnected
    }

    override suspend fun sendPriceUpdate(priceUpdate: StockPriceDto) {
        if (!isConnected()) {
            return
        }

        try {
            val message = PriceUpdateMessageDto.fromStockPriceDto(priceUpdate)
            val jsonString = json.encodeToString(message)
            webSocketSession?.send(Frame.Text(jsonString))
        } catch (e: Exception) {
            // Handle send error - could emit error status
            connectionStatusFlow.value = ConnectionStatus.Error("Failed to send: ${e.message}")
        }
    }

    override fun observePriceUpdates(): Flow<StockPriceDto> {
        return priceUpdatesChannel.receiveAsFlow()
    }

    override fun observeConnectionStatus(): Flow<ConnectionStatus> {
        return connectionStatusFlow.asStateFlow()
    }

    override fun isConnected(): Boolean {
        return webSocketSession != null && connectionStatusFlow.value is ConnectionStatus.Connected
    }

    /**
     * Starts receiving messages from the WebSocket.
     * Parses echoed messages and emits them as StockPriceDto.
     */
    private fun startReceiving() {
        receivingJob = scope.launch {
            val session = webSocketSession ?: return@launch

            try {
                for (frame in session.incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        try {
                            // Parse the echoed message
                            val message = json.decodeFromString<PriceUpdateMessageDto>(text)
                            val stockPriceDto = StockPriceDto(
                                symbol = message.symbol,
                                price = message.price,
                                previousPrice = message.previousPrice,
                                timestamp = message.timestamp
                            )
                            priceUpdatesChannel.send(stockPriceDto)
                        } catch (e: Exception) {
                            // Failed to parse message - skip it
                        }
                    }
                }
            } catch (e: Exception) {
                // Connection closed or error occurred
                connectionStatusFlow.value = ConnectionStatus.Error("Receive error: ${e.message}")
                disconnect()
            }
        }
    }

    /**
     * Starts sending mock price updates every 2 seconds.
     * Uses MockStockPriceDataSource to generate updates.
     */
    private fun startSending() {
        sendingJob = scope.launch {
            mockDataSource.generatePriceUpdates().collect { priceUpdates ->
                if (isConnected()) {
                    // Send all price updates
                    priceUpdates.forEach { priceUpdate ->
                        sendPriceUpdate(priceUpdate)
                    }
                }
            }
        }
    }
}

