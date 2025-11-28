package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.domain.model.Stock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SortStocksByPriceUseCaseTest {

    private lateinit var useCase: SortStocksByPriceUseCase

    @Before
    fun setup() {
        useCase = SortStocksByPriceUseCase()
    }

    @Test
    fun `sort stocks by price descending - highest price first`() {
        // Given
        val stocks = listOf(
            createStock("AAPL", 150.0),
            createStock("GOOG", 200.0),
            createStock("MSFT", 100.0)
        )

        // When
        val result = useCase(stocks)

        // Then
        assertEquals(3, result.size)
        assertEquals("GOOG", result[0].symbol)
        assertEquals("AAPL", result[1].symbol)
        assertEquals("MSFT", result[2].symbol)
    }

    @Test
    fun `sort stocks handles empty list`() {
        // Given
        val stocks = emptyList<Stock>()

        // When
        val result = useCase(stocks)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `sort stocks handles single stock`() {
        // Given
        val stocks = listOf(createStock("AAPL", 150.0))

        // When
        val result = useCase(stocks)

        // Then
        assertEquals(1, result.size)
        assertEquals("AAPL", result[0].symbol)
    }

    @Test
    fun `sort stocks with same price maintains order`() {
        // Given
        val stocks = listOf(
            createStock("AAPL", 150.0),
            createStock("GOOG", 150.0),
            createStock("MSFT", 150.0)
        )

        // When
        val result = useCase(stocks)

        // Then
        assertEquals(3, result.size)
        // All have same price, order should be maintained (stable sort)
        assertEquals(150.0, result[0].currentPrice, 0.01)
        assertEquals(150.0, result[1].currentPrice, 0.01)
        assertEquals(150.0, result[2].currentPrice, 0.01)
    }

    @Test
    fun `sort stocks with negative prices`() {
        // Given
        val stocks = listOf(
            createStock("STOCK1", -10.0),
            createStock("STOCK2", -5.0),
            createStock("STOCK3", -20.0)
        )

        // When
        val result = useCase(stocks)

        // Then
        assertEquals(3, result.size)
        assertEquals("STOCK2", result[0].symbol) // -5.0 is highest
        assertEquals("STOCK1", result[1].symbol) // -10.0
        assertEquals("STOCK3", result[2].symbol) // -20.0 is lowest
    }

    @Test
    fun `sort stocks with zero prices`() {
        // Given
        val stocks = listOf(
            createStock("STOCK1", 0.0),
            createStock("STOCK2", 10.0),
            createStock("STOCK3", 0.0)
        )

        // When
        val result = useCase(stocks)

        // Then
        assertEquals(3, result.size)
        assertEquals("STOCK2", result[0].symbol) // 10.0 is highest
        assertEquals(0.0, result[1].currentPrice, 0.01)
        assertEquals(0.0, result[2].currentPrice, 0.01)
    }

    private fun createStock(
        symbol: String,
        currentPrice: Double,
        previousPrice: Double = currentPrice - 1.0
    ): Stock {
        val movement = when {
            currentPrice > previousPrice -> PriceMovement.Up
            currentPrice < previousPrice -> PriceMovement.Down
            else -> PriceMovement.Unchanged
        }
        return Stock(
            symbol = symbol,
            currentPrice = currentPrice,
            previousPrice = previousPrice,
            movement = movement
        )
    }
}

