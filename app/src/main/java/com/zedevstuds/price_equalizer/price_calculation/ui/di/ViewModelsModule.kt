package com.zedevstuds.price_equalizer.price_calculation.ui.di

import com.zedevstuds.price_equalizer.price_calculation.ui.DrawerViewModel
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
    factory {
        DrawerViewModel(
            getAllListsUseCase = get()
        )
    }
    viewModel {
        MainScreenViewModel(
            enterParamsViewModel = get(),
            drawerViewModel = get(),
            saveProductsUseCase = get(),
            getProductsForListUseCase = get(),
            deleteListUseCase = get(),
            addProductUseCase = get(),
            deleteProductUseCase = get(),
            clearListUseCase = get(),
            addListUseCase = get(),
            updateProductListNameUseCase = get(),
        )
    }
}