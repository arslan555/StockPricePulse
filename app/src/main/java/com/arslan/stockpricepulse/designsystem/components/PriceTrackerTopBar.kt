package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arslan.stockpricepulse.R
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography
import com.arslan.stockpricepulse.designsystem.theme.stockPriceColors
import com.arslan.stockpricepulse.domain.model.ConnectionStatus

/**
 * Top app bar component for PriceTracker screen - Fintech Dark Theme.
 * Displays connection status dot + title on the left and toggle buttons on the right.
 * Has dark navy background with rounded top corners.
 *
 * @param connectionStatus Current WebSocket connection status
 * @param isStartButtonEnabled Whether the start button should be enabled
 * @param isStopButtonEnabled Whether the stop button should be enabled
 * @param isDarkTheme Current theme state (true for dark, false for light)
 * @param onStartClick Callback when start button is clicked
 * @param onStopClick Callback when stop button is clicked
 * @param onThemeToggle Callback when theme toggle button is clicked
 * @param modifier Modifier to be applied to the top bar
 */
@Composable
fun PriceTrackerTopBar(
    connectionStatus: ConnectionStatus,
    isStartButtonEnabled: Boolean,
    isStopButtonEnabled: Boolean,
    isDarkTheme: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = stockPriceColors()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding() // Add padding for status bar
            .background(colors.primaryBackground)
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
                    text = stringResource(R.string.stock_price_pulse),
                    style = StockPriceTypography.headerTitleText,
                    color = colors.textPrimary
                )
            }

            // Right side: Theme toggle + Start/Stop toggle buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Theme toggle button
                // Start/Stop toggle button
                ToggleButton(
                    isActive = isStopButtonEnabled,
                    onStartClick = onStartClick,
                    onStopClick = onStopClick,
                    enabled = isStartButtonEnabled || isStopButtonEnabled,
                    colors = colors
                )
                ThemeToggleButton(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle,
                    colors = colors
                )
            }
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
    val colors = stockPriceColors()
    val dotColor = when (status) {
        is ConnectionStatus.Connected -> colors.connected
        is ConnectionStatus.Disconnected -> colors.disconnected
        is ConnectionStatus.Connecting -> colors.connecting
        is ConnectionStatus.Error -> colors.error
    }

    Box(
        modifier = modifier
            .size(10.dp)
            .background(dotColor, shape = androidx.compose.foundation.shape.CircleShape)
    )
}

/**
 * Theme toggle button for switching between light and dark themes.
 */
@Composable
private fun ThemeToggleButton(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    colors: com.arslan.stockpricepulse.designsystem.theme.StockPriceColorPalette,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onThemeToggle,
        modifier = modifier
            .padding(start = 16.dp)
            .size(40.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (isDarkTheme) R.drawable.ic_day else R.drawable.ic_night
            ),
            contentDescription = if (isDarkTheme) "Switch to Light Theme" else "Switch to Dark Theme",
            tint = colors.textPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Toggle button for Start/Stop.
 */
@Composable
private fun ToggleButton(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    enabled: Boolean,
    colors: com.arslan.stockpricepulse.designsystem.theme.StockPriceColorPalette,
) {

    IconButton(
        onClick = if (isActive) onStopClick else onStartClick,
        enabled = enabled,
        modifier = modifier
            .padding(start = 16.dp)
            .size(40.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (isActive) R.drawable.ic_play else R.drawable.ic_pause
            ),
            contentDescription = if (isActive) "Switch to Light Theme" else "Switch to Dark Theme",
            tint = colors.textPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

