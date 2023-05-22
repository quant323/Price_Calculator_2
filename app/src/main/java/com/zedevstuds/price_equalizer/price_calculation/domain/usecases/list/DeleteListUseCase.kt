package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductListRepository

class DeleteListUseCase(
    private val productListRepository: ProductListRepository
) {

    suspend fun execute(listModel: ListModel) {
        productListRepository.deleteList(listModel)
    }
}