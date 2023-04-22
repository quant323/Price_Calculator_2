package com.zedevstuds.price_equalizer.price_calculation.ui.di

import com.zedevstuds.price_equalizer.price_calculation.ui.EnterParamsViewModel
import com.zedevstuds.price_equalizer.price_calculation.ui.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory {
        EnterParamsViewModel(
            preferenceRepository = get(),
            getPriceForOneUnitUseCase = get()
        )
    }
    viewModel {
        MainScreenViewModel(
            enterParamsViewModel = get(),
            getPriceForOneUnitUseCase = get(),
            preferenceRepository = get(),
            saveProductsUseCase = get(),
            getProductsForListUseCase = get(),
            deleteListUseCase = get()
        )
    }
}