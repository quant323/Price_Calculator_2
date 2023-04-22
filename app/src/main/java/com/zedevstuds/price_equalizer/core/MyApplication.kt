package com.zedevstuds.price_equalizer.core

import android.app.Application
import com.zedevstuds.price_equalizer.price_calculation.data.di.databaseModule
import com.zedevstuds.price_equalizer.price_calculation.data.di.preferencesModule
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.useCaseModule
import com.zedevstuds.price_equalizer.price_calculation.ui.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                preferencesModule,
                viewModelsModule,
                useCaseModule,
                databaseModule
            )
        }
    }
}