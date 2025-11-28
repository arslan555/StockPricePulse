package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.domain.model.Stock
import com.arslan.stockpricepulse.domain.repository.StockPriceRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObservePriceUpdatesUseCaseTest {

    private lateinit var repository: StockPriceRepository
    private lateinit var useCase: ObservePriceUpdatesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObservePriceUpdatesUseCase(repository)
    }

    @Test
    fun `invoke returns Flow from repository`() = runTest {
        // Given
        val expectedStocks = listOf(
            Stock("AAPL", 150.0, 100.0, PriceMovement.Up),
            Stock("GOOG", 200.0, 180.0, PriceMovement.Up)
        )
        every { repository.observePriceUpdates() } returns flowOf(*expectedStocks.toTypedArray())

        // When
        val result = useCase().toList()

        // Then
        assertEquals(2, result.size)
        assertEquals("AAPL", result[0].symbol)
        assertEquals("GOOG", result[1].symbol)
        coVerify(exactly = 1) { repository.observePriceUpdates() }
    }

    @Test
    fun `invoke handles empty Flow`() = runTest {
        // Given
        every { repository.observePriceUpdates() } returns flowOf()

        // When
        val result = useCase().toList()

        // Then
        assertEquals(0, result.size)
        coVerify(exactly = 1) { repository.observePriceUpdates() }
    }

    @Test
    fun `invoke handles single stock update`() = runTest {
        // Given
        val stock = Stock("AAPL", 150.0, 100.0, PriceMovement.Up)
        every { repository.observePriceUpdates() } returns flowOf(stock)

        // When
        val result = useCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals("AAPL", result[0].symbol)
        assertEquals(150.0, result[0].currentPrice, 0.01)
    }
}

