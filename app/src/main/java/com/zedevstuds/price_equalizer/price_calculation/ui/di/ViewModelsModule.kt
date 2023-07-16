package com.zedevstuds.price_equalizer.price_calculation.ui.di

import com.zedevstuds.price_equalizer.price_calculation.ui.drawer.DrawerViewModel
import com.zedevstuds.price_equalizer.price_calculation.ui.enterparams.EnterParamsViewModel
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.MainScreenViewModel
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
    factory {
        DrawerViewModel(
            getAllListsUseCase = get(),
            deleteListUseCase = get(),
            addListUseCase = get(),
            context = androidContext(),
        )
    }
    viewModel {
        MainScreenViewModel(
            enterParamsViewModel = get(),
            drawerViewModel = get(),
            deleteProductsInListUseCase = get(),
            addProductUseCase = get(),
            deleteProductUseCase = get(),
            getProductsForListByListIdUseCase = get(),
            updateProductTitleUseCase = get(),
        )
    }
}