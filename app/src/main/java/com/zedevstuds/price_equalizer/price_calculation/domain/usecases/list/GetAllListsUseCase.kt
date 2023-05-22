package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductListRepository
import kotlinx.coroutines.flow.Flow

class GetAllListsUseCase(
    private val productListRepository: ProductListRepository
) {

    fun execute(): Flow<List<ListModel>> {
        return productListRepository.getAllLists()
    }
}