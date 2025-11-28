package com.arslan.stockpricepulse.presentation.screens.pricetracker

import app.cash.turbine.test
import com.arslan.stockpricepulse.data.mapper.DomainToUiMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlin.time.Duration.Companion.seconds
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.domain.model.Stock
import com.arslan.stockpricepulse.domain.usecase.ManageWebSocketConnectionUseCase
import com.arslan.stockpricepulse.domain.usecase.ObservePriceUpdatesUseCase
import com.arslan.stockpricepulse.domain.usecase.SortStocksByPriceUseCase
import com.arslan.stockpricepulse.presentation.screens.pricetracker.model.StockUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PriceTrackerViewModelTest {

    private lateinit var observePriceUpdatesUseCase: ObservePriceUpdatesUseCase
    private lateinit var manageConnectionUseCase: ManageWebSocketConnectionUseCase
    private lateinit var sortStocksUseCase: SortStocksByPriceUseCase
    private lateinit var domainToUiMapper: DomainToUiMapper
    private lateinit var viewModel: PriceTrackerViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        observePriceUpdatesUseCase = mockk()
        manageConnectionUseCase = mockk()
        sortStocksUseCase = mockk()
        domainToUiMapper = mockk()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // ViewModel cleanup happens automatically when test completes
        // No need to call onCleared() as it's protected
        Dispatchers.resetMain()
    }

    private fun createViewModel(): PriceTrackerViewModel {
        return PriceTrackerViewModel(
            observePriceUpdatesUseCase = observePriceUpdatesUseCase,
            manageConnectionUseCase = manageConnectionUseCase,
            sortStocksUseCase = sortStocksUseCase,
            domainToUiMapper = domainToUiMapper
        )
    }

    @Test
    fun `initial state is correct`() {
        // Given & When
        viewModel = createViewModel()

        // Then
        val initialState = viewModel.uiState.value
        assertTrue(initialState.stocks.isEmpty())
        assertTrue(initialState.connectionStatus is ConnectionStatus.Disconnected)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
        assertFalse(initialState.isFeedActive)
        assertTrue(initialState.isStartButtonEnabled)
        assertFalse(initialState.isStopButtonEnabled)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent StartPriceFeed sets loading state and connects`() = runTest {
        // Given
        val statuses = flowOf(
            ConnectionStatus.Connecting,
            ConnectionStatus.Connected
        )
        coEvery { manageConnectionUseCase.connect() } returns statuses
        every { manageConnectionUseCase.observeConnectionStatus() } returns flowOf(ConnectionStatus.Disconnected)
        every { observePriceUpdatesUseCase() } returns flowOf()
        every { sortStocksUseCase(any()) } returns emptyList()

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.handleIntent(PriceTrackerIntent.StartPriceFeed)
        advanceUntilIdle()

        // Then - verify final state
        val finalState = viewModel.uiState.value
        assertEquals(ConnectionStatus.Connected, finalState.connectionStatus)
        assertFalse(finalState.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent StopPriceFeed handles exception`() = runTest {
        // Given
        val exception = Exception("Disconnect failed")
        coEvery { manageConnectionUseCase.disconnect() } throws exception
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.handleIntent(PriceTrackerIntent.StopPriceFeed)
        advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals("Disconnect failed", finalState.error)
        assertFalse(finalState.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `price updates are collected sorted and mapped to UI models`() = runTest {
        // Given
        val stocks = listOf(
            Stock("AAPL", 150.0, 100.0, PriceMovement.Up),
            Stock("GOOG", 200.0, 180.0, PriceMovement.Up)
        )
        
        // Use SharedFlow to control when values are emitted
        val priceUpdatesFlow = MutableSharedFlow<Stock>(replay = 0, extraBufferCapacity = 10)
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        
        coEvery { manageConnectionUseCase.connect() } returns flowOf(ConnectionStatus.Connected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow
        every { observePriceUpdatesUseCase() } returns priceUpdatesFlow
        
        // Mock sorting
        every { sortStocksUseCase(any()) } answers {
            val stocksList = firstArg<List<Stock>>()
            stocksList.sortedByDescending { it.currentPrice }
        }
        
        // Mock mapper
        every { domainToUiMapper.toUi(any()) } answers {
            val stock = firstArg<Stock>()
            createUiModel(stock.symbol, stock.currentPrice)
        }

        // When - Start feed and collect price updates
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.handleIntent(PriceTrackerIntent.StartPriceFeed)
        advanceUntilIdle()
        
        // Emit price updates after collection has started
        priceUpdatesFlow.emit(stocks[0])
        advanceUntilIdle()
        priceUpdatesFlow.emit(stocks[1])
        advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(2, finalState.stocks.size)
        assertEquals("GOOG", finalState.stocks[0].symbol) // Sorted by price descending
        assertEquals("AAPL", finalState.stocks[1].symbol)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent ConnectionStatusChanged updates connection status`() = runTest {
        // Given
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.handleIntent(PriceTrackerIntent.ConnectionStatusChanged(true))
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.connectionStatus is ConnectionStatus.Connected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleIntent ConnectionStatusChanged sets disconnected when false`() = runTest {
        // Given
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.handleIntent(PriceTrackerIntent.ConnectionStatusChanged(false))
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.connectionStatus is ConnectionStatus.Disconnected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `connection status observation updates state automatically`() = runTest {
        // Given
        // flowOf emits all values and completes - collection will finish
        val connectionStatusFlow = flowOf(
            ConnectionStatus.Disconnected,
            ConnectionStatus.Connecting,
            ConnectionStatus.Connected
        )
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow

        // When - ViewModel is created, it automatically starts observing in init
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then - verify final state (flow completes after emitting all values)
        val finalState = viewModel.uiState.value
        assertEquals(ConnectionStatus.Connected, finalState.connectionStatus)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `multiple price updates accumulate in stocks map`() = runTest {
        // Given
        val stock1 = Stock("AAPL", 150.0, 100.0, PriceMovement.Up)
        val stock2 = Stock("GOOG", 200.0, 180.0, PriceMovement.Up)
        val stock1Updated = Stock("AAPL", 160.0, 150.0, PriceMovement.Up)

        // Use SharedFlow to control when values are emitted
        val priceUpdatesFlow = MutableSharedFlow<Stock>(replay = 0, extraBufferCapacity = 10)
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        
        coEvery { manageConnectionUseCase.connect() } returns flowOf(ConnectionStatus.Connected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow
        every { observePriceUpdatesUseCase() } returns priceUpdatesFlow
        
        // Mock sorting - handle different combinations
        every { sortStocksUseCase(any()) } answers {
            val stocks = firstArg<List<Stock>>()
            stocks.sortedByDescending { it.currentPrice }
        }
        
        // Mock mapper
        every { domainToUiMapper.toUi(any()) } answers {
            val stock = firstArg<Stock>()
            createUiModel(stock.symbol, stock.currentPrice)
        }

        // When
        viewModel = createViewModel()
        advanceUntilIdle() // Let init complete
        
        viewModel.handleIntent(PriceTrackerIntent.StartPriceFeed)
        advanceUntilIdle() // Let connection complete
        
        // Emit price updates after collection has started
        priceUpdatesFlow.emit(stock1)
        advanceUntilIdle()
        
        priceUpdatesFlow.emit(stock2)
        advanceUntilIdle()
        
        priceUpdatesFlow.emit(stock1Updated)
        advanceUntilIdle()

        // Then - stocks should accumulate and update
        val finalState = viewModel.uiState.value
        assertEquals(2, finalState.stocks.size)
        finalState.stocks.find { it.symbol == "AAPL" }?.priceValue?.let { assertEquals(160.0, it, 0.01) }
        finalState.stocks.find { it.symbol == "GOOG" }?.priceValue?.let {
            assertEquals(200.0, it, 0.01)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isStartButtonEnabled is true when disconnected and not loading`() = runTest {
        // Given
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.isStartButtonEnabled)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isStopButtonEnabled is true when connected and not loading`() = runTest {
        // Given
        coEvery { manageConnectionUseCase.connect() } returns flowOf(ConnectionStatus.Connected)
        val connectionStatusFlow = flowOf(ConnectionStatus.Disconnected)
        every { manageConnectionUseCase.observeConnectionStatus() } returns connectionStatusFlow
        every { observePriceUpdatesUseCase() } returns flowOf()
        every { sortStocksUseCase(any()) } returns emptyList()

        // When
        viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.handleIntent(PriceTrackerIntent.StartPriceFeed)
        advanceUntilIdle()
        
        // Emit connected status
        advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertTrue(finalState.isStopButtonEnabled)
        assertFalse(finalState.isStartButtonEnabled)
    }

    private fun createUiModel(symbol: String, price: Double): StockUiModel {
        return StockUiModel(
            symbol = symbol,
            companyName = "Test Company",
            price = "$${String.format("%.2f", price)}",
            priceValue = price,
            priceChange = 0.0,
            priceChangePercent = 0.0,
            movement = PriceMovement.Unchanged
        )
    }
}

