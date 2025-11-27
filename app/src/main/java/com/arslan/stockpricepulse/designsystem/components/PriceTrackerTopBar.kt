package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceColors
import com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography
import com.arslan.stockpricepulse.domain.model.ConnectionStatus

/**
 * Top app bar component for PriceTracker screen - Fintech Dark Theme.
 * Displays connection status dot + title on the left and toggle button on the right.
 * Has dark navy background with rounded top corners.
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(StockPriceColors.primaryBackground)
            .padding(horizontal = Spacing.medium, vertical = Spacing.medium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Connection dot + Title
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connection status dot
                ConnectionStatusDot(status = connectionStatus)
                
                // Title
                Text(
                    text = "Stock Price Pulse",
                    style = StockPriceTypography.headerTitleText,
                    color = StockPriceColors.textPrimary
                )
            }

            // Right side: Toggle button (pill shape)
            ToggleButton(
                isActive = isStopButtonEnabled,
                onStartClick = onStartClick,
                onStopClick = onStopClick,
                enabled = isStartButtonEnabled || isStopButtonEnabled
            )
        }
    }
}

/**
 * Connection status dot indicator.
 */
@Composable
private fun ConnectionStatusDot(
    status: ConnectionStatus,
    modifier: Modifier = Modifier
) {
    val dotColor = when (status) {
        is ConnectionStatus.Connected -> StockPriceColors.connected
        is ConnectionStatus.Disconnected -> StockPriceColors.disconnected
        is ConnectionStatus.Connecting -> StockPriceColors.connecting
        is ConnectionStatus.Error -> StockPriceColors.error
    }

    Box(
        modifier = modifier
            .size(10.dp)
            .background(dotColor, shape = androidx.compose.foundation.shape.CircleShape)
    )
}

/**
 * Toggle button (pill shape) for Start/Stop.
 */
@Composable
private fun ToggleButton(
    isActive: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = if (isActive) onStopClick else onStartClick,
        enabled = enabled,
        modifier = modifier
            .size(40.dp)
            .background(
                StockPriceColors.toggleButtonBackground,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Using a simple icon - you can replace with play/pause icons
        Text(
            text = if (isActive) "⏸" else "▶",
            color = StockPriceColors.textPrimary,
            style = StockPriceTypography.buttonText
        )
    }
}

