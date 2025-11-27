package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.Stock
import com.arslan.stockpricepulse.domain.repository.StockPriceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing real-time stock price updates from the repository.
 * Provides a clean interface for the presentation layer to observe price changes.
 */
class ObservePriceUpdatesUseCase(
    private val repository: StockPriceRepository
) {
    /**
     * Observes stock price updates from the WebSocket connection.
     * Returns a Flow that emits Stock domain models whenever a price update is received.
     *
     * @return Flow of Stock domain models
     */
    operator fun invoke(): Flow<Stock> {
        return repository.observePriceUpdates()
    }
}

