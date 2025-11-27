package com.arslan.stockpricepulse.presentation.screens.pricetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arslan.stockpricepulse.R
import com.arslan.stockpricepulse.designsystem.components.PriceTrackerTopBar
import com.arslan.stockpricepulse.designsystem.components.StockRow
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceColors
import com.arslan.stockpricepulse.domain.model.ConnectionStatus
import com.arslan.stockpricepulse.presentation.screens.pricetracker.model.StockUiModel

/**
 * Main screen for displaying real-time stock prices.
 * Implements MVI pattern with ViewModel for state management.
 *
 * @param viewModel The ViewModel managing the screen state and business logic
 * @param modifier Modifier to be applied to the screen
 */
@Composable
fun PriceTrackerScreen(
    viewModel: PriceTrackerViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is PriceTrackerSideEffect.ShowError -> {
                    snackBarHostState.showSnackbar(sideEffect.message)
                }

                is PriceTrackerSideEffect.ShowSuccess -> {
                    snackBarHostState.showSnackbar(sideEffect.message)
                }

                is PriceTrackerSideEffect.ShowMessage -> {
                    snackBarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = StockPriceColors.primaryBackground,
        topBar = {
            PriceTrackerTopBar(
                connectionStatus = uiState.connectionStatus,
                isStartButtonEnabled = uiState.isStartButtonEnabled,
                isStopButtonEnabled = uiState.isStopButtonEnabled,
                onStartClick = {
                    viewModel.handleIntent(PriceTrackerIntent.StartPriceFeed)
                },
                onStopClick = {
                    viewModel.handleIntent(PriceTrackerIntent.StopPriceFeed)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(StockPriceColors.primaryBackground)
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.stocks.isEmpty() -> {
                    // Show loading indicator when initializing
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.stocks.isEmpty() -> {
                    // Show empty state message
                    EmptyStateMessage(
                        modifier = Modifier.align(Alignment.Center),
                        connectionStatus = uiState.connectionStatus
                    )
                }

                else -> {
                    // Show list of stocks
                    StockList(
                        stocks = uiState.stocks,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Show loading overlay if loading while stocks are displayed
            if (uiState.isLoading && uiState.stocks.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing.large),
                    contentAlignment = Alignment.TopEnd
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(Spacing.small)
                    )
                }
            }
        }
    }
}

/**
 * Displays the list of stocks using LazyColumn.
 *
 * @param stocks List of stock UI models to display
 * @param modifier Modifier to be applied to the list
 */
@Composable
private fun StockList(
    stocks: List<StockUiModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            items = stocks,
            key = { _, stock -> stock.symbol }
        ) { index, stock ->
            StockRow(
                stock = stock,
                index = index
            )
        }
    }
}

/**
 * Displays an empty state message when no stocks are available.
 *
 * @param connectionStatus Current connection status
 * @param modifier Modifier to be applied to the message
 */
@Composable
private fun EmptyStateMessage(
    connectionStatus: ConnectionStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(Spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message = when (connectionStatus) {
            is ConnectionStatus.Disconnected -> stringResource(R.string.press_start_to_begin_receiving_price_updates)
            is ConnectionStatus.Connecting -> stringResource(R.string.connecting_to_price_feed)
            is ConnectionStatus.Error -> stringResource(R.string.connection_error_please_try_again)
            is ConnectionStatus.Connected -> stringResource(R.string.waiting_for_price_updates)
        }

        androidx.compose.material3.Text(
            text = message,
            style = com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography.statusText,
            modifier = Modifier.padding(Spacing.medium)
        )
    }
}

