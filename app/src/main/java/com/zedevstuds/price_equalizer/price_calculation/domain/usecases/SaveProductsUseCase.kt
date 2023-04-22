package com.zedevstuds.price_equalizer.price_calculation.domain.usecases

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class SaveProductsUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(products: List<ProductModel>, listName: String) {
        productRepository.insertProducts(products, listName)
    }
}