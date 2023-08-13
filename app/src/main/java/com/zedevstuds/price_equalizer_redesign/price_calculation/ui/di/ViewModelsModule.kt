package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.di

import com.zedevstuds.price_equalizer_redesign.core.ui.MainActivityViewModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.EnterParamsViewModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.MainScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory {
        EnterParamsViewModel(
            preferenceRepository = get(),
            getPriceForOneUnitUseCase = get(),
            context = androidContext(),
        )
    }
    viewModel {
        MainScreenViewModel(
            enterParamsViewModel = get(),
            deleteProductsInListUseCase = get(),
            addProductUseCase = get(),
            deleteProductUseCase = get(),
            getProductsForListByListIdUseCase = get(),
            updateProductTitleUseCase = get(),
            deleteListUseCase = get(),
            addListUseCase = get(),
            updateListUseCase = get(),
            getAllListsUseCase = get(),
            context = androidContext(),
        )
    }
    viewModel {
        MainActivityViewModel(
            preferenceRepository = get()
        )
    }
}