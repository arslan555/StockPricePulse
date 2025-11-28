package com.arslan.stockpricepulse.data.repository

import com.arslan.stockpricepulse.data.datasource.network.WebSocketDataSource
import com.arslan.stockpricepulse.data.mapper.StockDtoToDomainMapper
import com.arslan.stockpricepulse.data.model.StockPriceDto
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.domain.model.Stock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StockPriceRepositoryImplTest {

    private lateinit var webSocketDataSource: WebSocketDataSource
    private lateinit var dtoToDomainMapper: StockDtoToDomainMapper
    private lateinit var repository: StockPriceRepositoryImpl

    @Before
    fun setup() {
        webSocketDataSource = mockk()
        dtoToDomainMapper = mockk()
        repository = StockPriceRepositoryImpl(webSocketDataSource, dtoToDomainMapper)
    }

    @Test
    fun `connect delegates to WebSocketDataSource`() = runTest {
        // Given
        val statuses = listOf(
            ConnectionStatus.Connecting,
            ConnectionStatus.Connected
        )
        coEvery { webSocketDataSource.connect() } returns flowOf(*statuses.toTypedArray())

        // When
        val result = repository.connect().toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ConnectionStatus.Connecting)
        assertTrue(result[1] is ConnectionStatus.Connected)
        coVerify(exactly = 1) { webSocketDataSource.connect() }
    }

    @Test
    fun `disconnect delegates to WebSocketDataSource`() = runTest {
        // Given
        coEvery { webSocketDataSource.disconnect() } returns Unit

        // When
        repository.disconnect()

        // Then
        coVerify(exactly = 1) { webSocketDataSource.disconnect() }
    }

    @Test
    fun `observePriceUpdates maps DTOs to Domain models`() = runTest {
        // Given
        val dtos = listOf(
            StockPriceDto("AAPL", 150.0, 100.0),
            StockPriceDto("GOOG", 200.0, 180.0)
        )
        val domainStocks = listOf(
            Stock("AAPL", 150.0, 100.0, PriceMovement.Up),
            Stock("GOOG", 200.0, 180.0, PriceMovement.Up)
        )

        every { webSocketDataSource.observePriceUpdates() } returns flowOf(*dtos.toTypedArray())
        every { dtoToDomainMapper.toDomain(dtos[0]) } returns domainStocks[0]
        every { dtoToDomainMapper.toDomain(dtos[1]) } returns domainStocks[1]

        // When
        val result = repository.observePriceUpdates().toList()

        // Then
        assertEquals(2, result.size)
        assertEquals("AAPL", result[0].symbol)
        assertEquals("GOOG", result[1].symbol)
        assertEquals(150.0, result[0].currentPrice, 0.01)
        assertEquals(200.0, result[1].currentPrice, 0.01)
        coVerify(exactly = 2) { dtoToDomainMapper.toDomain(any()) }
    }

    @Test
    fun `observePriceUpdates handles empty Flow`() = runTest {
        // Given
        every { webSocketDataSource.observePriceUpdates() } returns flowOf()

        // When
        val result = repository.observePriceUpdates().toList()

        // Then
        assertEquals(0, result.size)
        coVerify(exactly = 0) { dtoToDomainMapper.toDomain(any()) }
    }

    @Test
    fun `observePriceUpdates maps single DTO correctly`() = runTest {
        // Given
        val dto = StockPriceDto("AAPL", 150.0, 100.0)
        val domainStock = Stock("AAPL", 150.0, 100.0, PriceMovement.Up)

        every { webSocketDataSource.observePriceUpdates() } returns flowOf(dto)
        every { dtoToDomainMapper.toDomain(dto) } returns domainStock

        // When
        val result = repository.observePriceUpdates().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals("AAPL", result[0].symbol)
        assertEquals(150.0, result[0].currentPrice, 0.01)
        coVerify(exactly = 1) { dtoToDomainMapper.toDomain(dto) }
    }

    @Test
    fun `observeConnectionStatus delegates to WebSocketDataSource`() = runTest {
        // Given
        val statuses = listOf(
            ConnectionStatus.Disconnected,
            ConnectionStatus.Connecting,
            ConnectionStatus.Connected
        )
        every { webSocketDataSource.observeConnectionStatus() } returns flowOf(*statuses.toTypedArray())

        // When
        val result = repository.observeConnectionStatus().toList()

        // Then
        assertEquals(3, result.size)
        assertTrue(result[0] is ConnectionStatus.Disconnected)
        assertTrue(result[1] is ConnectionStatus.Connecting)
        assertTrue(result[2] is ConnectionStatus.Connected)
    }

    @Test
    fun `observeConnectionStatus handles error status`() = runTest {
        // Given
        val errorStatus = ConnectionStatus.Error("Connection failed")
        every { webSocketDataSource.observeConnectionStatus() } returns flowOf(errorStatus)

        // When
        val result = repository.observeConnectionStatus().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ConnectionStatus.Error)
        assertEquals("Connection failed", (result[0] as ConnectionStatus.Error).message)
    }

    @Test
    fun `isConnected returns true when WebSocketDataSource is connected`() {
        // Given
        every { webSocketDataSource.isConnected() } returns true

        // When
        val result = repository.isConnected()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isConnected returns false when WebSocketDataSource is disconnected`() {
        // Given
        every { webSocketDataSource.isConnected() } returns false

        // When
        val result = repository.isConnected()

        // Then
        assertFalse(result)
    }

    @Test
    fun `observePriceUpdates maps DTOs with different price movements`() = runTest {
        // Given
        val dtos = listOf(
            StockPriceDto("AAPL", 150.0, 100.0), // Up
            StockPriceDto("GOOG", 100.0, 150.0), // Down
            StockPriceDto("MSFT", 150.0, 150.0) // Unchanged
        )
        val domainStocks = listOf(
            Stock("AAPL", 150.0, 100.0, PriceMovement.Up),
            Stock("GOOG", 100.0, 150.0, PriceMovement.Down),
            Stock("MSFT", 150.0, 150.0, PriceMovement.Unchanged)
        )

        every { webSocketDataSource.observePriceUpdates() } returns flowOf(*dtos.toTypedArray())
        every { dtoToDomainMapper.toDomain(dtos[0]) } returns domainStocks[0]
        every { dtoToDomainMapper.toDomain(dtos[1]) } returns domainStocks[1]
        every { dtoToDomainMapper.toDomain(dtos[2]) } returns domainStocks[2]

        // When
        val result = repository.observePriceUpdates().toList()

        // Then
        assertEquals(3, result.size)
        assertEquals(PriceMovement.Up, result[0].movement)
        assertEquals(PriceMovement.Down, result[1].movement)
        assertEquals(PriceMovement.Unchanged, result[2].movement)
    }
}

