package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.arslan.stockpricepulse.designsystem.theme.StockPriceColors
import com.arslan.stockpricepulse.domain.model.PriceMovement

/**
 * Indicator component showing price movement direction.
 * Displays an arrow icon (up/down) with appropriate color.
 *
 * @param movement The price movement direction (Up, Down, or Unchanged)
 * @param modifier Modifier to be applied to the indicator
 */
@Composable
fun PriceChangeIndicator(
    movement: PriceMovement,
    modifier: Modifier = Modifier
) {
    val (icon, color) = when (movement) {
        is PriceMovement.Up -> "▲" to StockPriceColors.green
        is PriceMovement.Down -> "▼" to StockPriceColors.red
        is PriceMovement.Unchanged -> "—" to StockPriceColors.gray
    }

    Text(
        text = icon,
        color = color,
        modifier = modifier,
        style = TextStyle(fontSize = 16.sp)
    )
}

