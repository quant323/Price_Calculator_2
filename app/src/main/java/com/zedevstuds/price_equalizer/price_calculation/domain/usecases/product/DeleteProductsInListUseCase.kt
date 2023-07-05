package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class DeleteProductsInListUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(listName: String) {
        productRepository.deleteProductsByListName(listName)
    }
}