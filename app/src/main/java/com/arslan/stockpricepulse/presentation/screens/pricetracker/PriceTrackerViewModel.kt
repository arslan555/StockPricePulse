package com.arslan.stockpricepulse.presentation.screens.pricetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.stockpricepulse.data.mapper.DomainToUiMapper
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.model.Stock
import com.arslan.stockpricepulse.domain.usecase.ManageWebSocketConnectionUseCase
import com.arslan.stockpricepulse.domain.usecase.ObservePriceUpdatesUseCase
import com.arslan.stockpricepulse.domain.usecase.SortStocksByPriceUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel implementing the MVI pattern for PriceTracker screen.
 * Handles user intents, manages UI state, and emits side effects.
 */
class PriceTrackerViewModel(
    private val observePriceUpdatesUseCase: ObservePriceUpdatesUseCase,
    private val manageConnectionUseCase: ManageWebSocketConnectionUseCase,
    private val sortStocksUseCase: SortStocksByPriceUseCase,
    private val domainToUiMapper: DomainToUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(PriceTrackerUiState())
    val uiState: StateFlow<PriceTrackerUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<PriceTrackerSideEffect>(Channel.UNLIMITED)
    val sideEffects = _sideEffects.receiveAsFlow()

    // Map to store current stocks by symbol for efficient updates
    private val stocksMap = mutableMapOf<String, Stock>()

    // Job for observing price updates
    private var priceUpdatesJob: Job? = null

    // Job for observing connection status
    private var connectionStatusJob: Job? = null

    init {
        observeConnectionStatus()
    }

    /**
     * Handles user intents/actions.
     */
    fun handleIntent(intent: PriceTrackerIntent) {
        when (intent) {
            is PriceTrackerIntent.StartPriceFeed -> startPriceFeed()
            is PriceTrackerIntent.StopPriceFeed -> stopPriceFeed()
            is PriceTrackerIntent.PriceUpdateReceived -> handlePriceUpdate(
                symbol = intent.symbol,
                price = intent.price,
                previousPrice = intent.previousPrice
            )
            is PriceTrackerIntent.ConnectionStatusChanged -> handleConnectionStatusChange(intent.isConnected)
            is PriceTrackerIntent.ErrorOccurred -> handleError(intent.message)
        }
    }

    /**
     * Starts the price feed by connecting to WebSocket and observing updates.
     */
    private fun startPriceFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                manageConnectionUseCase.connect().collect { status ->
                    _uiState.update { it.copy(connectionStatus = status) }

                    when (status) {
                        is ConnectionStatus.Connected -> {
                            _uiState.update { it.copy(isLoading = false) }
                            startObservingPriceUpdates()
                            _sideEffects.send(PriceTrackerSideEffect.ShowSuccess("Connected to price feed"))
                        }

                        is ConnectionStatus.Connecting -> {
                            // Keep loading state
                        }

                        is ConnectionStatus.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = status.message
                                )
                            }
                            _sideEffects.send(PriceTrackerSideEffect.ShowError(status.message))
                        }

                        is ConnectionStatus.Disconnected -> {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to start price feed"
                    )
                }
                _sideEffects.send(PriceTrackerSideEffect.ShowError(e.message ?: "Failed to start price feed"))
            }
        }
    }

    /**
     * Stops the price feed by disconnecting from WebSocket.
     */
    private fun stopPriceFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                priceUpdatesJob?.cancel()
                priceUpdatesJob = null
                manageConnectionUseCase.disconnect()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        connectionStatus = ConnectionStatus.Disconnected
                    )
                }
                _sideEffects.send(PriceTrackerSideEffect.ShowMessage("Price feed stopped"))
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to stop price feed"
                    )
                }
                _sideEffects.send(PriceTrackerSideEffect.ShowError(e.message ?: "Failed to stop price feed"))
            }
        }
    }

    /**
     * Starts observing price updates from the WebSocket.
     */
    private fun startObservingPriceUpdates() {
        priceUpdatesJob?.cancel()
        priceUpdatesJob = viewModelScope.launch {
            observePriceUpdatesUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(error = e.message ?: "Error receiving price updates")
                    }
                    _sideEffects.send(PriceTrackerSideEffect.ShowError(e.message ?: "Error receiving price updates"))
                }
                .collect { stock ->
                    // Update stocks map
                    stocksMap[stock.symbol] = stock

                    // Convert to UI models, sort, and update state
                    val domainStocks = stocksMap.values.toList()
                    val sortedStocks = sortStocksUseCase(domainStocks)
                    val uiStocks = sortedStocks.map { domainToUiMapper.toUi(it) }

                    _uiState.update { it.copy(stocks = uiStocks) }
                }
        }
    }

    /**
     * Handles a price update received from WebSocket.
     */
    private fun handlePriceUpdate(symbol: String, price: Double, previousPrice: Double) {
        // This is handled automatically by startObservingPriceUpdates()
        // But kept for potential future use or manual updates
    }

    /**
     * Handles connection status changes.
     */
    private fun handleConnectionStatusChange(isConnected: Boolean) {
        _uiState.update {
            it.copy(
                connectionStatus = if (isConnected) {
                    ConnectionStatus.Connected
                } else {
                    ConnectionStatus.Disconnected
                }
            )
        }
    }

    /**
     * Handles errors.
     */
    private fun handleError(message: String) {
        _uiState.update { it.copy(error = message) }
        viewModelScope.launch {
            _sideEffects.send(PriceTrackerSideEffect.ShowError(message))
        }
    }

    /**
     * Observes connection status changes.
     */
    private fun observeConnectionStatus() {
        connectionStatusJob?.cancel()
        connectionStatusJob = viewModelScope.launch {
            manageConnectionUseCase.observeConnectionStatus()
                .collect { status ->
                    _uiState.update { it.copy(connectionStatus = status) }

                    // If disconnected, cancel price updates
                    if (status is ConnectionStatus.Disconnected) {
                        priceUpdatesJob?.cancel()
                        priceUpdatesJob = null
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        priceUpdatesJob?.cancel()
        connectionStatusJob?.cancel()
    }
}

