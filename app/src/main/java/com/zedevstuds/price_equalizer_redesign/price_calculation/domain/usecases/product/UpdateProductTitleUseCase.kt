package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.ProductRepository

class UpdateProductTitleUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(product: ProductModel, listId: Int) {
        productRepository.updateProduct(product, listId)
    }
}