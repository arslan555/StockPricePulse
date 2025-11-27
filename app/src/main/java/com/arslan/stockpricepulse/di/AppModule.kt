package com.arslan.stockpricepulse.di

import com.arslan.stockpricepulse.data.datasource.mock.MockStockPriceDataSource
import com.arslan.stockpricepulse.data.datasource.network.WebSocketDataSource
import com.arslan.stockpricepulse.data.datasource.network.WebSocketDataSourceImpl
import com.arslan.stockpricepulse.data.mapper.DomainToUiMapper
import com.arslan.stockpricepulse.data.mapper.StockDtoToDomainMapper
import com.arslan.stockpricepulse.data.repository.StockPriceRepositoryImpl
import com.arslan.stockpricepulse.domain.repository.StockPriceRepository
import com.arslan.stockpricepulse.domain.usecase.CalculatePriceMovementUseCase
import com.arslan.stockpricepulse.domain.usecase.ManageWebSocketConnectionUseCase
import com.arslan.stockpricepulse.domain.usecase.ObservePriceUpdatesUseCase
import com.arslan.stockpricepulse.domain.usecase.SortStocksByPriceUseCase
import com.arslan.stockpricepulse.presentation.screens.pricetracker.PriceTrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin dependency injection module for the application.
 * Provides all dependencies needed throughout the app.
 */
val appModule = module {

    // Data Sources
    single<MockStockPriceDataSource> { MockStockPriceDataSource() }

    single<WebSocketDataSource> {
        WebSocketDataSourceImpl(
            mockDataSource = get()
        )
    }

    // Mappers
    single<StockDtoToDomainMapper> { StockDtoToDomainMapper() }
    single<DomainToUiMapper> { DomainToUiMapper() }

    // Repository
    single<StockPriceRepository> {
        StockPriceRepositoryImpl(
            webSocketDataSource = get(),
            dtoToDomainMapper = get()
        )
    }

    // Use Cases
    single<SortStocksByPriceUseCase> { SortStocksByPriceUseCase() }
    single<CalculatePriceMovementUseCase> { CalculatePriceMovementUseCase() }
    single<ObservePriceUpdatesUseCase> {
        ObservePriceUpdatesUseCase(
            repository = get()
        )
    }
    single<ManageWebSocketConnectionUseCase> {
        ManageWebSocketConnectionUseCase(
            repository = get()
        )
    }

    // ViewModels
    viewModel<PriceTrackerViewModel> {
        PriceTrackerViewModel(
            observePriceUpdatesUseCase = get(),
            manageConnectionUseCase = get(),
            sortStocksUseCase = get(),
            domainToUiMapper = get()
        )
    }
}

