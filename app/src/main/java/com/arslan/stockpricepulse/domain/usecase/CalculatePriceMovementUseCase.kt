package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.PriceMovement

/**
 * Use case for calculating price movement direction.
 * Encapsulates the business logic for determining if a price has gone up, down, or unchanged.
 */
class CalculatePriceMovementUseCase {

    /**
     * Calculates the price movement direction based on current and previous prices.
     *
     * @param currentPrice The current price value
     * @param previousPrice The previous price value
     * @return PriceMovement indicating Up, Down, or Unchanged
     */
    operator fun invoke(
        currentPrice: Double,
        previousPrice: Double
    ): PriceMovement {
        return when {
            currentPrice > previousPrice -> PriceMovement.Up
            currentPrice < previousPrice -> PriceMovement.Down
            else -> PriceMovement.Unchanged
        }
    }
}

