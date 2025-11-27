package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography
import com.arslan.stockpricepulse.designsystem.theme.stockPriceColors
import com.arslan.stockpricepulse.domain.model.PriceMovement
import com.arslan.stockpricepulse.presentation.screens.pricetracker.model.StockUiModel
import kotlinx.coroutines.delay

/**
 * Reusable component for displaying a stock row in the list - Fintech Dark Theme.
 * Flat design with alternating backgrounds, shows symbol, company name, price, and change.
 *
 * @param stock The stock UI model to display
 * @param index The index of the row (for alternating backgrounds)
 * @param modifier Modifier to be applied to the row
 */
@Composable
fun StockRow(
    modifier: Modifier = Modifier,
    stock: StockUiModel,
    index: Int = 0,
) {
    val colors = stockPriceColors()
    // Flash animation state for price text only
    var shouldFlashPrice by remember { mutableStateOf(false) }
    var flashPriceColor by remember { mutableStateOf<Color?>(null) }

    // Trigger flash when price changes
    LaunchedEffect(stock.priceValue) {
        // Only flash on Up or Down movement, not Unchanged
        flashPriceColor = when (stock.movement) {
            is PriceMovement.Up -> colors.green
            is PriceMovement.Down -> colors.red
            else -> null
        }
        shouldFlashPrice = flashPriceColor != null
        delay(1000) // Flash duration 1 second
        shouldFlashPrice = false
        flashPriceColor = null
    }

    // Animate price text color for flash effect
    val animatedPriceColor by animateColorAsState(
        targetValue = if (shouldFlashPrice && flashPriceColor != null) {
            flashPriceColor!!
        } else {
            colors.textPrimary
        },
        animationSpec = tween(durationMillis = 200),
        label = "priceFlashAnimation"
    )

    // Animate price change text color for flash effect
    val animatedPriceChangeColor by animateColorAsState(
        targetValue = if (shouldFlashPrice && flashPriceColor != null) {
            flashPriceColor!!
        } else {
            when (stock.movement) {
                is PriceMovement.Up -> colors.green
                is PriceMovement.Down -> colors.red
                else -> colors.gray
            }
        },
        animationSpec = tween(durationMillis = 200),
        label = "priceChangeFlashAnimation"
    )

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Row content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.primaryBackground)
                .padding(horizontal = Spacing.medium, vertical = Spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Arrow icon + Symbol + Company name
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Movement arrow icon
                PriceChangeIndicator(movement = stock.movement)
                
                // Symbol and company name column
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Stock symbol
                    Text(
                        text = stock.symbol,
                        style = StockPriceTypography.symbolText,
                        color = colors.textPrimary
                    )
                    
                    // Company name
                    Text(
                        text = stock.companyName,
                        style = StockPriceTypography.companyNameText,
                        color = colors.textSecondary
                    )
                }
            }

            // Right side: Price + Price change (with flash animation)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Current price (flashes green/red)
                Text(
                    text = stock.price,
                    style = StockPriceTypography.priceText,
                    color = animatedPriceColor
                )
                
                // Price change (+X.XX (X.XX%)) (flashes green/red)
                Text(
                    text = formatPriceChange(stock.priceChange, stock.priceChangePercent),
                    style = StockPriceTypography.priceChangeText,
                    color = animatedPriceChangeColor
                )
            }
        }
        
        // Divider at bottom
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.divider)
                .align(Alignment.BottomCenter)
        )
    }
}

/**
 * Formats price change as "+X.XX (X.XX%)" or "-X.XX (X.XX%)".
 */
private fun formatPriceChange(change: Double, changePercent: Double): String {
    val sign = if (change >= 0) "+" else ""
    return "$sign${String.format("%.2f", change)} (${String.format("%.2f", changePercent)}%)"
}

