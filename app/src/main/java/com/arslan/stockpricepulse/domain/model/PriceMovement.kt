package com.arslan.stockpricepulse.domain.model

/**
 * Represents the price movement direction for a stock.
 */
sealed interface PriceMovement {
    /**
     * Price has increased compared to previous value.
     */
    data object Up : PriceMovement

    /**
     * Price has decreased compared to previous value.
     */
    data object Down : PriceMovement

    /**
     * Price remains unchanged.
     */
    data object Unchanged : PriceMovement
}

