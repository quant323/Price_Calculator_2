package com.zedevstuds.price_equalizer_redesign.core

import android.app.Application
import com.zedevstuds.price_equalizer_redesign.price_calculation.data.di.databaseModule
import com.zedevstuds.price_equalizer_redesign.price_calculation.data.di.preferencesModule
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.di.listUseCaseModule
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.di.productUseCaseModule
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.di.viewModelsModule
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
                productUseCaseModule,
                listUseCaseModule,
                databaseModule
            )
        }
    }
}