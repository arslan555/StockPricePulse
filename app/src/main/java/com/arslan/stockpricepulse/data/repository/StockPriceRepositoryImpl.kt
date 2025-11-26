package com.arslan.stockpricepulse.data.repository

import com.arslan.stockpricepulse.data.datasource.network.WebSocketDataSource
import com.arslan.stockpricepulse.data.mapper.StockDtoToDomainMapper
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.domain.model.Stock
import com.arslan.stockpricepulse.domain.repository.StockPriceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of StockPriceRepository.
 * Coordinates between WebSocketDataSource and domain layer using mappers.
 */
class StockPriceRepositoryImpl(
    private val webSocketDataSource: WebSocketDataSource,
    private val dtoToDomainMapper: StockDtoToDomainMapper
) : StockPriceRepository {

    override suspend fun connect(): Flow<ConnectionStatus> {
        return webSocketDataSource.connect()
    }

    override suspend fun disconnect() {
        webSocketDataSource.disconnect()
    }

    override fun observePriceUpdates(): Flow<Stock> {
        return webSocketDataSource.observePriceUpdates()
            .map { dto ->
                dtoToDomainMapper.toDomain(dto)
            }
    }

    override fun observeConnectionStatus(): Flow<ConnectionStatus> {
        return webSocketDataSource.observeConnectionStatus()
    }

    override fun isConnected(): Boolean {
        return webSocketDataSource.isConnected()
    }
}

