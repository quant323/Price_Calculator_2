package com.zedevstuds.price_equalizer.price_calculation.domain.repositories

import com.zedevstuds.price_equalizer.price_calculation.data.database.ListDbModel
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {
    fun getAllLists(): Flow<List<ListModel>>

    suspend fun addList(listDbModel: ListModel)

    suspend fun deleteList(listDbModel: ListModel)
}