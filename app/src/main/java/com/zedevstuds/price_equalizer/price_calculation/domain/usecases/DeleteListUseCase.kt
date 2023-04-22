package com.zedevstuds.price_equalizer.price_calculation.domain.usecases

import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class DeleteListUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(listName: String) {
        productRepository.deleteProductsByListName(listName)
    }
}