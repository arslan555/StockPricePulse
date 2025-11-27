package com.arslan.stockpricepulse.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arslan.stockpricepulse.designsystem.theme.Spacing
import com.arslan.stockpricepulse.designsystem.theme.StockPriceTypography
import com.arslan.stockpricepulse.presentation.screens.pricetracker.model.StockUiModel

/**
 * Reusable component for displaying a stock row in the list.
 *
 * @param stock The stock UI model to display
 * @param modifier Modifier to be applied to the row
 */
@Composable
fun StockRow(
    stock: StockUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.medium, vertical = Spacing.small),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stock symbol
            Text(
                text = stock.symbol,
                style = StockPriceTypography.symbolText
            )

            // Price and movement indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriceChangeIndicator(movement = stock.movement)
                Text(
                    text = stock.price,
                    style = StockPriceTypography.priceText
                )
            }
        }
    }
}

