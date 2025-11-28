package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.repository.StockPriceRepository
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

class ManageWebSocketConnectionUseCaseTest {

    private lateinit var repository: StockPriceRepository
    private lateinit var useCase: ManageWebSocketConnectionUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ManageWebSocketConnectionUseCase(repository)
    }

    @Test
    fun `connect returns ConnectionStatus Flow from repository`() = runTest {
        // Given
        val statuses = listOf(
            ConnectionStatus.Connecting,
            ConnectionStatus.Connected
        )
        coEvery { repository.connect() } returns flowOf(*statuses.toTypedArray())

        // When
        val result = useCase.connect().toList()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0] is ConnectionStatus.Connecting)
        assertTrue(result[1] is ConnectionStatus.Connected)
        coVerify(exactly = 1) { repository.connect() }
    }

    @Test
    fun `disconnect calls repository disconnect`() = runTest {
        // Given
        coEvery { repository.disconnect() } returns Unit

        // When
        useCase.disconnect()

        // Then
        coVerify(exactly = 1) { repository.disconnect() }
    }

    @Test
    fun `observeConnectionStatus returns Flow from repository`() = runTest {
        // Given
        val statuses = listOf(
            ConnectionStatus.Disconnected,
            ConnectionStatus.Connecting,
            ConnectionStatus.Connected
        )
        every { repository.observeConnectionStatus() } returns flowOf(*statuses.toTypedArray())

        // When
        val result = useCase.observeConnectionStatus().toList()

        // Then
        assertEquals(3, result.size)
        assertTrue(result[0] is ConnectionStatus.Disconnected)
        assertTrue(result[1] is ConnectionStatus.Connecting)
        assertTrue(result[2] is ConnectionStatus.Connected)
        coVerify(exactly = 1) { repository.observeConnectionStatus() }
    }

    @Test
    fun `isConnected returns true when repository is connected`() {
        // Given
        every { repository.isConnected() } returns true

        // When
        val result = useCase.isConnected()

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { repository.isConnected() }
    }

    @Test
    fun `isConnected returns false when repository is disconnected`() {
        // Given
        every { repository.isConnected() } returns false

        // When
        val result = useCase.isConnected()

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { repository.isConnected() }
    }

    @Test
    fun `connect handles error status`() = runTest {
        // Given
        val errorStatus = ConnectionStatus.Error("Connection failed")
        coEvery { repository.connect() } returns flowOf(errorStatus)

        // When
        val result = useCase.connect().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is ConnectionStatus.Error)
        assertEquals("Connection failed", (result[0] as ConnectionStatus.Error).message)
    }
}

