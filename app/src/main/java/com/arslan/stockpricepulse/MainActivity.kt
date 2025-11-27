package com.arslan.stockpricepulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.arslan.stockpricepulse.designsystem.theme.StockPricePulseTheme
import com.arslan.stockpricepulse.presentation.screens.pricetracker.PriceTrackerScreen
import com.arslan.stockpricepulse.presentation.screens.pricetracker.PriceTrackerViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockPricePulseTheme {
                // Get ViewModel from Koin
                val viewModel: PriceTrackerViewModel = koinViewModel()
                
                PriceTrackerScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}