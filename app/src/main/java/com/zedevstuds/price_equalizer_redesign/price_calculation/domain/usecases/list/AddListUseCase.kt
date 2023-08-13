package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.ProductListRepository

class AddListUseCase(
    private val productListRepository: ProductListRepository
) {

    suspend fun execute(productList: ListModel) {
        productListRepository.addList(productList)
    }
}