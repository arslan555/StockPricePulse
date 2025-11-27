package com.arslan.stockpricepulse

import android.app.Application
import com.arslan.stockpricepulse.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application class for StockPricePulse.
 * Initializes Koin dependency injection.
 */
class StockPricePulseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidContext(this@StockPricePulseApplication)
            modules(appModule)
        }
    }
}

