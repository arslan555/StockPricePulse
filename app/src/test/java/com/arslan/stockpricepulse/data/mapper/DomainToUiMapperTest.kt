package com.arslan.stockpricepulse.data.mapper

import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.domain.model.Stock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DomainToUiMapperTest {

    private lateinit var mapper: DomainToUiMapper

    @Before
    fun setup() {
        mapper = DomainToUiMapper()
    }

    @Test
    fun `map Domain to UI model with Up movement`() {
        // Given
        val stock = Stock(
            symbol = "AAPL",
            currentPrice = 150.0,
            previousPrice = 100.0,
            movement = PriceMovement.Up
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("AAPL", result.symbol)
        assertEquals("Apple Inc.", result.companyName)
        assertEquals("$150.00", result.price)
        assertEquals(150.0, result.priceValue, 0.01)
        assertEquals(PriceMovement.Up, result.movement)
        assertEquals(50.0, result.priceChange, 0.01)
        assertEquals(50.0, result.priceChangePercent, 0.01)
    }

    @Test
    fun `map Domain to UI model with Down movement`() {
        // Given
        val stock = Stock(
            symbol = "GOOG",
            currentPrice = 100.0,
            previousPrice = 150.0,
            movement = PriceMovement.Down
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("GOOG", result.symbol)
        assertEquals("Alphabet Inc.", result.companyName)
        assertEquals("$100.00", result.price)
        assertEquals(100.0, result.priceValue, 0.01)
        assertEquals(PriceMovement.Down, result.movement)
        assertEquals(-50.0, result.priceChange, 0.01)
        assertEquals(-33.33, result.priceChangePercent, 0.01)
    }

    @Test
    fun `map Domain to UI model with Unchanged movement`() {
        // Given
        val stock = Stock(
            symbol = "MSFT",
            currentPrice = 150.0,
            previousPrice = 150.0,
            movement = PriceMovement.Unchanged
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("MSFT", result.symbol)
        assertEquals("Microsoft Corp.", result.companyName)
        assertEquals("$150.00", result.price)
        assertEquals(150.0, result.priceValue, 0.01)
        assertEquals(PriceMovement.Unchanged, result.movement)
        assertEquals(0.0, result.priceChange, 0.01)
        assertEquals(0.0, result.priceChangePercent, 0.01)
    }

    @Test
    fun `map Domain to UI model formats price correctly`() {
        // Given
        val stock = Stock(
            symbol = "AAPL",
            currentPrice = 174.55,
            previousPrice = 174.0,
            movement = PriceMovement.Up
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("$174.55", result.price)
    }

    @Test
    fun `map Domain to UI model formats price with two decimals`() {
        // Given
        val stock = Stock(
            symbol = "AAPL",
            currentPrice = 174.5,
            previousPrice = 174.0,
            movement = PriceMovement.Up
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("$174.50", result.price)
    }

    @Test
    fun `map Domain to UI model handles zero price`() {
        // Given
        val stock = Stock(
            symbol = "STOCK",
            currentPrice = 0.0,
            previousPrice = 0.0,
            movement = PriceMovement.Unchanged
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("$0.00", result.price)
        assertEquals(0.0, result.priceValue, 0.01)
    }

    @Test
    fun `map Domain to UI model handles negative price change`() {
        // Given
        val stock = Stock(
            symbol = "GOOG",
            currentPrice = 100.0,
            previousPrice = 150.0,
            movement = PriceMovement.Down
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals(-50.0, result.priceChange, 0.01)
    }

    @Test
    fun `map list of Domain to UI models`() {
        // Given
        val stocks = listOf(
            Stock("AAPL", 150.0, 100.0, PriceMovement.Up),
            Stock("GOOG", 200.0, 180.0, PriceMovement.Up),
            Stock("MSFT", 100.0, 120.0, PriceMovement.Down)
        )

        // When
        val result = mapper.toUiList(stocks)

        // Then
        assertEquals(3, result.size)
        assertEquals("AAPL", result[0].symbol)
        assertEquals("GOOG", result[1].symbol)
        assertEquals("MSFT", result[2].symbol)
    }

    @Test
    fun `map empty list of Domain models`() {
        // Given
        val stocks = emptyList<Stock>()

        // When
        val result = mapper.toUiList(stocks)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `map Domain to UI model gets company name for known symbol`() {
        // Given
        val stock = Stock(
            symbol = "TSLA",
            currentPrice = 250.0,
            previousPrice = 240.0,
            movement = PriceMovement.Up
        )

        // When
        val result = mapper.toUi(stock)

        // Then
        assertEquals("Tesla, Inc.", result.companyName)
    }
}

