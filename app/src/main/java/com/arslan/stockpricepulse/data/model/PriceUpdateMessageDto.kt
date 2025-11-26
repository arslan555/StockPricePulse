package com.arslan.stockpricepulse.data.model

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for price update messages sent/received via WebSocket.
 * This represents the JSON structure that will be serialized and sent to the echo server.
 *
 * @param symbol The stock symbol code
 * @param price The updated price value
 * @param previousPrice The previous price value
 * @param timestamp The timestamp when the update was generated
 */
@Serializable
data class PriceUpdateMessageDto(
    val symbol: String,
    val price: Double,
    val previousPrice: Double,
    val timestamp: Long
) {
    companion object {
        /**
         * Creates a PriceUpdateMessageDto from a StockPriceDto.
         */
        fun fromStockPriceDto(dto: StockPriceDto): PriceUpdateMessageDto {
            return PriceUpdateMessageDto(
                symbol = dto.symbol,
                price = dto.price,
                previousPrice = dto.previousPrice,
                timestamp = dto.timestamp
            )
        }
    }
}

