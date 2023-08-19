package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsForListByListIdUseCase(
    private val productRepository: ProductRepository
) {

    fun execute(listId: Int): Flow<List<ProductModel>> =
        productRepository.getProductsByListId(listId)

}