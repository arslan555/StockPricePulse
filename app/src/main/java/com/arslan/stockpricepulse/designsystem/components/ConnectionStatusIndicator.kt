package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceColors
import com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography
import com.arslan.stockpricepulse.domain.model.ConnectionStatus

/**
 * Component displaying the WebSocket connection status.
 * Shows an indicator and text indicating the current connection state.
 *
 * @param status The current connection status
 * @param modifier Modifier to be applied to the indicator
 */
@Composable
fun ConnectionStatusIndicator(
    status: ConnectionStatus,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (status) {
            is ConnectionStatus.Connected -> {
                StatusDot(color = StockPriceColors.connected)
                Text(
                    text = "Connected",
                    style = StockPriceTypography.statusText,
                    color = StockPriceColors.connected
                )
            }

            is ConnectionStatus.Disconnected -> {
                StatusDot(color = StockPriceColors.disconnected)
                Text(
                    text = "Disconnected",
                    style = StockPriceTypography.statusText,
                    color = StockPriceColors.disconnected
                )
            }

            is ConnectionStatus.Connecting -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = StockPriceColors.connecting,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Connecting...",
                    style = StockPriceTypography.statusText,
                    color = StockPriceColors.connecting
                )
            }

            is ConnectionStatus.Error -> {
                StatusDot(color = StockPriceColors.error)
                Text(
                    text = "Error",
                    style = StockPriceTypography.statusText,
                    color = StockPriceColors.error
                )
            }
        }
    }
}

@Composable
private fun StatusDot(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(12.dp)
            .drawBehind {
                drawCircle(color = color)
            }
    )
}

