package com.zedevstuds.price_equalizer_redesign.price_calculation.data.di

import android.content.Context
import com.zedevstuds.price_equalizer_redesign.price_calculation.data.repositories.SharedPreferenceRepository
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.PreferenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val APP_PREFERENCES = "APP_PREFERENCES"

val preferencesModule = module {

    single {
        androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<PreferenceRepository> {
        SharedPreferenceRepository(preferences = get())
    }
}