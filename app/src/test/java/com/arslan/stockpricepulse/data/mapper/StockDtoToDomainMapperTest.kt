package com.arslan.stockpricepulse.data.mapper

import com.arslan.stockpricepulse.data.model.StockPriceDto
import com.arslan.stockpricepulse.domain.model.PriceMovement
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StockDtoToDomainMapperTest {

    private lateinit var mapper: StockDtoToDomainMapper

    @Before
    fun setup() {
        mapper = StockDtoToDomainMapper()
    }

    @Test
    fun `map DTO to Domain model with Up movement`() {
        // Given
        val dto = StockPriceDto(
            symbol = "AAPL",
            price = 150.0,
            previousPrice = 100.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals("AAPL", result.symbol)
        assertEquals(150.0, result.currentPrice, 0.01)
        assertEquals(100.0, result.previousPrice, 0.01)
        assertEquals(PriceMovement.Up, result.movement)
    }

    @Test
    fun `map DTO to Domain model with Down movement`() {
        // Given
        val dto = StockPriceDto(
            symbol = "GOOG",
            price = 100.0,
            previousPrice = 150.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals("GOOG", result.symbol)
        assertEquals(100.0, result.currentPrice, 0.01)
        assertEquals(150.0, result.previousPrice, 0.01)
        assertEquals(PriceMovement.Down, result.movement)
    }

    @Test
    fun `map DTO to Domain model with Unchanged movement`() {
        // Given
        val dto = StockPriceDto(
            symbol = "MSFT",
            price = 150.0,
            previousPrice = 150.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals("MSFT", result.symbol)
        assertEquals(150.0, result.currentPrice, 0.01)
        assertEquals(150.0, result.previousPrice, 0.01)
        assertEquals(PriceMovement.Unchanged, result.movement)
    }

    @Test
    fun `map list of DTOs to Domain models`() {
        // Given
        val dtos = listOf(
            StockPriceDto("AAPL", 150.0, 100.0),
            StockPriceDto("GOOG", 200.0, 180.0),
            StockPriceDto("MSFT", 100.0, 120.0)
        )

        // When
        val result = mapper.toDomainList(dtos)

        // Then
        assertEquals(3, result.size)
        assertEquals("AAPL", result[0].symbol)
        assertEquals("GOOG", result[1].symbol)
        assertEquals("MSFT", result[2].symbol)
        assertEquals(PriceMovement.Up, result[0].movement)
        assertEquals(PriceMovement.Up, result[1].movement)
        assertEquals(PriceMovement.Down, result[2].movement)
    }

    @Test
    fun `map empty list of DTOs`() {
        // Given
        val dtos = emptyList<StockPriceDto>()

        // When
        val result = mapper.toDomainList(dtos)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `map DTO with zero prices`() {
        // Given
        val dto = StockPriceDto(
            symbol = "STOCK",
            price = 0.0,
            previousPrice = 0.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals(0.0, result.currentPrice, 0.01)
        assertEquals(0.0, result.previousPrice, 0.01)
        assertEquals(PriceMovement.Unchanged, result.movement)
    }

    @Test
    fun `map DTO with negative prices`() {
        // Given
        val dto = StockPriceDto(
            symbol = "STOCK",
            price = -10.0,
            previousPrice = -20.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals(-10.0, result.currentPrice, 0.01)
        assertEquals(-20.0, result.previousPrice, 0.01)
        assertEquals(PriceMovement.Up, result.movement)
    }

    @Test
    fun `map DTO calculates price change correctly`() {
        // Given
        val dto = StockPriceDto(
            symbol = "AAPL",
            price = 150.0,
            previousPrice = 100.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals(50.0, result.priceChange, 0.01)
    }

    @Test
    fun `map DTO calculates price change percent correctly`() {
        // Given
        val dto = StockPriceDto(
            symbol = "AAPL",
            price = 150.0,
            previousPrice = 100.0
        )

        // When
        val result = mapper.toDomain(dto)

        // Then
        assertEquals(50.0, result.priceChangePercent, 0.01) // (150-100)/100 * 100 = 50%
    }
}

