package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography
import com.arslan.stockpricepulse.domain.model.ConnectionStatus

/**
 * Top app bar component for PriceTracker screen.
 * Displays connection status on the left and Start/Stop button on the right.
 *
 * @param connectionStatus Current WebSocket connection status
 * @param isStartButtonEnabled Whether the start button should be enabled
 * @param isStopButtonEnabled Whether the stop button should be enabled
 * @param onStartClick Callback when start button is clicked
 * @param onStopClick Callback when stop button is clicked
 * @param modifier Modifier to be applied to the top bar
 */
@Composable
fun PriceTrackerTopBar(
    connectionStatus: ConnectionStatus,
    isStartButtonEnabled: Boolean,
    isStopButtonEnabled: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Connection status indicator on the left
        ConnectionStatusIndicator(status = connectionStatus)

        // Start/Stop button on the right
        when {
            isStopButtonEnabled -> {
                Button(
                    onClick = onStopClick,
                    enabled = isStopButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Stop",
                        style = StockPriceTypography.buttonText
                    )
                }
            }

            isStartButtonEnabled -> {
                Button(
                    onClick = onStartClick,
                    enabled = isStartButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Start",
                        style = StockPriceTypography.buttonText
                    )
                }
            }
        }
    }
}

