package com.zedevstuds.price_equalizer.price_calculation.data.repositories

import com.zedevstuds.price_equalizer.price_calculation.data.database.ProductsDatabase
import com.zedevstuds.price_equalizer.price_calculation.data.database.toData
import com.zedevstuds.price_equalizer.price_calculation.data.database.toDomain
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository

class ProductRepositoryImp(
    database: ProductsDatabase
) : ProductRepository {
    private val dao = database.dao

    override suspend fun insertProducts(products: List<ProductModel>, listName: String) {
        dao.insertProducts(
            products.map { it.toData(listName) }
        )
    }

    override suspend fun getProductsByListName(listName: String): List<ProductModel> {
        return dao.getProductsByListName(listName)
            .map { it.toDomain() }
    }

    override suspend fun deleteProductsByListName(listName: String) {
        dao.deleteProductsByListName(listName)
    }

//    override suspend fun updateListName(newListName: String) {
//        dao.updateListName(newListName)
//    }
}