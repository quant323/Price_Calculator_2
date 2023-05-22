package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class AddProductUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(product: ProductModel, listNane: String) {
        productRepository.insertProduct(product, listNane)
    }
}