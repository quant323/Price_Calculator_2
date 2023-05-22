package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class UpdateProductListNameUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(oldName: String, newName: String) {
        productRepository.updateProductListName(oldName, newName)
    }
}