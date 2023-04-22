package com.zedevstuds.price_equalizer.price_calculation.domain.usecases

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class GetProductsForListUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun execute(listName: String): List<ProductModel> {
        return productRepository.getProductsByListName(listName)
    }
}