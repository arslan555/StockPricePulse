package com.arslan.stockpricepulse.data.mapper

import com.arslan.stockpricepulse.data.datasource.mock.MockStockPriceDataSource
import com.arslan.stockpricepulse.domain.model.Stock
import com.arslan.stockpricepulse.presentation.screens.pricetracker.model.StockUiModel

/**
 * Mapper that converts domain models to UI models.
 */
class DomainToUiMapper {

    /**
     * Converts a Stock domain model to a StockUiModel for UI display.
     *
     * @param stock The domain model
     * @return StockUiModel with formatted price string, company name, and price changes
     */
    fun toUi(stock: Stock): StockUiModel {
        return StockUiModel(
            symbol = stock.symbol,
            companyName = MockStockPriceDataSource.getCompanyName(stock.symbol),
            price = formatPrice(stock.currentPrice),
            priceValue = stock.currentPrice,
            priceChange = stock.priceChange,
            priceChangePercent = stock.priceChangePercent,
            movement = stock.movement
        )
    }

    /**
     * Converts a list of Stock domain models to a list of StockUiModel.
     *
     * @param stocks List of domain models
     * @return List of UI models
     */
    fun toUiList(stocks: List<Stock>): List<StockUiModel> {
        return stocks.map { toUi(it) }
    }

    /**
     * Formats a price value to a string with dollar sign and 2 decimal places.
     *
     * @param price The price value
     * @return Formatted price string (e.g., "$174.55")
     */
    private fun formatPrice(price: Double): String {
        return String.format("$%.2f", price)
    }
}

