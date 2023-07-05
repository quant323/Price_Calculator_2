package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.di

import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.AddProductUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.ClearListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.DeleteProductsInListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.DeleteProductUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.GetPriceForOneUnitUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.GetProductsForListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.SaveProductsUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.UpdateProductListNameUseCase
import org.koin.dsl.module

val productUseCaseModule = module {
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
        DeleteProductsInListUseCase(productRepository = get())
    }
    factory {
        AddProductUseCase(productRepository = get())
    }
    factory {
        DeleteProductUseCase(productRepository = get())
    }
    factory {
        ClearListUseCase(productRepository = get())
    }
    factory {
        UpdateProductListNameUseCase(productRepository = get())
    }
}