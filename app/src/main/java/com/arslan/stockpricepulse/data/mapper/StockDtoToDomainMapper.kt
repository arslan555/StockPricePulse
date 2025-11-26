package com.arslan.stockpricepulse.data.mapper

import com.arslan.stockpricepulse.data.model.StockPriceDto
import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.domain.model.Stock

/**
 * Mapper that converts data layer DTOs to domain models.
 */
class StockDtoToDomainMapper {

    /**
     * Converts a StockPriceDto to a Stock domain model.
     * Calculates the price movement based on current and previous prices.
     *
     * @param dto The data transfer object containing price information
     * @return Stock domain model
     */
    fun toDomain(dto: StockPriceDto): Stock {
        val movement = calculatePriceMovement(
            currentPrice = dto.price,
            previousPrice = dto.previousPrice
        )

        return Stock(
            symbol = dto.symbol,
            currentPrice = dto.price,
            previousPrice = dto.previousPrice,
            movement = movement
        )
    }

    /**
     * Converts a list of StockPriceDto to a list of Stock domain models.
     *
     * @param dtos List of data transfer objects
     * @return List of Stock domain models
     */
    fun toDomainList(dtos: List<StockPriceDto>): List<Stock> {
        return dtos.map { toDomain(it) }
    }

    /**
     * Calculates the price movement direction based on current and previous prices.
     *
     * @param currentPrice The current price
     * @param previousPrice The previous price
     * @return PriceMovement (Up, Down, or Unchanged)
     */
    private fun calculatePriceMovement(
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

