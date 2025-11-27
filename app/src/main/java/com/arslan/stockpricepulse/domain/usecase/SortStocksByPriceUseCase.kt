package com.arslan.stockpricepulse.domain.usecase

import com.arslan.stockpricepulse.domain.model.Stock

/**
 * Use case for sorting stocks by price in descending order (highest price first).
 * This encapsulates the business logic for sorting stock data.
 */
class SortStocksByPriceUseCase {

    /**
     * Sorts a list of stocks by their current price in descending order.
     * Stocks with higher prices appear first in the list.
     *
     * @param stocks List of stocks to sort
     * @return Sorted list of stocks (highest price first)
     */
    operator fun invoke(stocks: List<Stock>): List<Stock> {
        return stocks.sortedByDescending { it.currentPrice }
    }
}

