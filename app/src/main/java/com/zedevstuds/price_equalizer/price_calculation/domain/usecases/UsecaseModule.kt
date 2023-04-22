package com.zedevstuds.price_equalizer.price_calculation.domain.usecases

import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetPriceForOneUnitUseCase()
    }
    factory {
        SaveProductsUseCase(productRepository = get())
    }
    factory {
        GetProductsForListUseCase(productRepository = get())
    }
    factory {
        DeleteListUseCase(productRepository = get())
    }
}