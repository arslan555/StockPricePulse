package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.PriceMovement
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatePriceMovementUseCaseTest {

    private lateinit var useCase: CalculatePriceMovementUseCase

    @Before
    fun setup() {
        useCase = CalculatePriceMovementUseCase()
    }

    @Test
    fun `calculate Up movement when current price is greater than previous`() {
        // Given
        val currentPrice = 150.0
        val previousPrice = 100.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Up, result)
    }

    @Test
    fun `calculate Down movement when current price is less than previous`() {
        // Given
        val currentPrice = 100.0
        val previousPrice = 150.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Down, result)
    }

    @Test
    fun `calculate Unchanged movement when current price equals previous`() {
        // Given
        val currentPrice = 150.0
        val previousPrice = 150.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Unchanged, result)
    }

    @Test
    fun `calculate movement with zero prices`() {
        // Given
        val currentPrice = 0.0
        val previousPrice = 0.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Unchanged, result)
    }

    @Test
    fun `calculate movement with negative prices`() {
        // Given
        val currentPrice = -10.0
        val previousPrice = -20.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Up, result) // -10 > -20
    }

    @Test
    fun `calculate movement with very small price differences`() {
        // Given
        val currentPrice = 100.0001
        val previousPrice = 100.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Up, result)
    }

    @Test
    fun `calculate movement with large price differences`() {
        // Given
        val currentPrice = 1000.0
        val previousPrice = 100.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Up, result)
    }

    @Test
    fun `calculate movement when price goes from positive to negative`() {
        // Given
        val currentPrice = -10.0
        val previousPrice = 10.0

        // When
        val result = useCase(currentPrice, previousPrice)

        // Then
        assertEquals(PriceMovement.Down, result)
    }
}

