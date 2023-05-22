package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductsForListUseCase(
    private val productRepository: ProductRepository
) {

    fun execute(listName: String, isSortByPrice: Boolean): Flow<List<ProductModel>> {
        return productRepository.getProductsByListName(listName)
            .map {  productList ->
                if (isSortByPrice) {
                    productList.sortedBy { it.priceForOneUnit }
                } else {
                    productList.sortedBy { it.id }
                }
            }
    }
}