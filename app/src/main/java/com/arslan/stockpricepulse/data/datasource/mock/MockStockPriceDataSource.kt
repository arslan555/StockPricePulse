package com.arslan.stockpricepulse.data.datasource.mock

import com.arslan.stockpricepulse.data.model.StockPriceDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random

/**
 * Mock data source that generates stock price updates every 2 seconds.
 * This simulates real-time stock price data for 25 predefined symbols.
 */
class MockStockPriceDataSource {

    companion object {
        /**
         * Update interval in milliseconds (2 seconds).
         */
        private const val UPDATE_INTERVAL_MS = 2000L

        /**
         * Minimum price change percentage (-5%).
         */
        private const val MIN_DELTA_PERCENT = -0.05

        /**
         * Maximum price change percentage (+5%).
         */
        private const val MAX_DELTA_PERCENT = 0.05

        /**
         * Predefined list of 25 stock symbols with their base prices.
         */
        private val STOCK_SYMBOLS = listOf(
            "AAPL" to 175.50,
            "GOOG" to 142.30,
            "MSFT" to 378.85,
            "AMZN" to 151.94,
            "TSLA" to 248.50,
            "NVDA" to 485.20,
            "META" to 485.58,
            "NFLX" to 485.90,
            "AMD" to 144.25,
            "INTC" to 44.12,
            "ORCL" to 125.67,
            "CRM" to 278.45,
            "ADBE" to 567.89,
            "PYPL" to 62.34,
            "UBER" to 68.45,
            "LYFT" to 12.56,
            "SPOT" to 285.67,
            "TWTR" to 53.70,
            "SNAP" to 11.23,
            "PINS" to 33.45,
            "SQ" to 78.90,
            "SHOP" to 45.67,
            "ZM" to 65.43,
            "DOCU" to 54.32,
            "RBLX" to 42.10
        )
    }

    /**
     * Current prices for each symbol.
     * Key: symbol, Value: current price
     */
    private val currentPrices = mutableMapOf<String, Double>().apply {
        STOCK_SYMBOLS.forEach { (symbol, basePrice) ->
            this[symbol] = basePrice
        }
    }

    /**
     * Base prices for each symbol (used to calculate reasonable price ranges).
     * Key: symbol, Value: base price
     */
    private val basePrices = STOCK_SYMBOLS.toMap()

    /**
     * Generates a flow of stock price updates every 2 seconds.
     * Each emission contains updated prices for all 25 symbols.
     *
     * @return Flow of lists of StockPriceDto, where each list contains all 25 symbols
     */
    fun generatePriceUpdates(): Flow<List<StockPriceDto>> = flow {
        while (true) {
            val updates = generateUpdatesForAllSymbols()
            emit(updates)
            delay(UPDATE_INTERVAL_MS)
        }
    }.onStart {
        // Emit initial prices immediately
        emit(generateInitialPrices())
    }

    /**
     * Generates initial prices for all symbols (using base prices).
     */
    private fun generateInitialPrices(): List<StockPriceDto> {
        return basePrices.map { (symbol, basePrice) ->
            StockPriceDto(
                symbol = symbol,
                price = basePrice,
                previousPrice = basePrice,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    /**
     * Generates updated prices for all symbols with random deltas.
     */
    private fun generateUpdatesForAllSymbols(): List<StockPriceDto> {
        val timestamp = System.currentTimeMillis()
        
        return currentPrices.map { (symbol, currentPrice) ->
            val basePrice = basePrices[symbol] ?: currentPrice
            val previousPrice = currentPrice
            
            // Generate new price with random delta (±1-5% of base price)
            val deltaPercent = Random.nextDouble(MIN_DELTA_PERCENT, MAX_DELTA_PERCENT)
            val priceChange = basePrice * deltaPercent
            val newPrice = (currentPrice + priceChange).coerceAtLeast(basePrice * 0.5)
                .coerceAtMost(basePrice * 1.5)
            
            // Update current price
            currentPrices[symbol] = newPrice
            
            StockPriceDto(
                symbol = symbol,
                price = newPrice,
                previousPrice = previousPrice,
                timestamp = timestamp
            )
        }
    }

    /**
     * Gets the current price for a specific symbol.
     */
    fun getCurrentPrice(symbol: String): Double? {
        return currentPrices[symbol]
    }

    /**
     * Gets all current prices.
     */
    fun getAllCurrentPrices(): Map<String, Double> {
        return currentPrices.toMap()
    }
}

