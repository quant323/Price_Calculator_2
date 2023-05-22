package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductListRepository
import com.zedevstuds.price_equalizer.price_calculation.ui.AUTOGENERATE_ID

class AddListUseCase(
    private val productListRepository: ProductListRepository
) {

    suspend fun execute(listName: String) {
        productListRepository.addList(
            ListModel(
                id = AUTOGENERATE_ID,
                name = listName
            )
        )
    }
}