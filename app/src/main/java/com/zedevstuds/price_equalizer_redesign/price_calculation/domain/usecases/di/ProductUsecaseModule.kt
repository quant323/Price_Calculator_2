package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.di

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.AddProductUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.DeleteProductsInListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.DeleteProductUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.GetPriceForOneUnitUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.GetProductsForListByListIdUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.UpdateProductUseCase
import org.koin.dsl.module

val productUseCaseModule = module {
    factory {
        GetPriceForOneUnitUseCase()
    }
    factory {
        DeleteProductsInListUseCase(productRepository = get())
    }
    factory {
        AddProductUseCase(productRepository = get())
    }
    factory {
        DeleteProductUseCase(productRepository = get())
    }
    factory {
        GetProductsForListByListIdUseCase(productRepository = get())
    }
    factory {
        UpdateProductUseCase(productRepository = get())
    }
}