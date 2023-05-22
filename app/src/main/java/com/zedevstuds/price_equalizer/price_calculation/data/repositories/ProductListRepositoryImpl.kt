package com.zedevstuds.price_equalizer.price_calculation.data.repositories

import com.zedevstuds.price_equalizer.price_calculation.data.database.ListDbModel
import com.zedevstuds.price_equalizer.price_calculation.data.database.ProductsDatabase
import com.zedevstuds.price_equalizer.price_calculation.data.database.toData
import com.zedevstuds.price_equalizer.price_calculation.data.database.toDomain
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductListRepositoryImpl(
    database: ProductsDatabase
) : ProductListRepository {

    private val dao = database.listsDao

    override fun getAllLists(): Flow<List<ListModel>> {
        return dao.getAllLists().map { lists ->
            lists.map { it.toDomain() }
        }
    }

    override suspend fun addList(listDbModel: ListModel) {
        dao.insertList(listDbModel.toData())
    }

    override suspend fun deleteList(listDbModel: ListModel) {
        dao.deleteList(listDbModel.toData())
    }
}