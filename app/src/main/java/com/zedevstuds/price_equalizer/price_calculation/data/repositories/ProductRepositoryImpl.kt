package com.zedevstuds.price_equalizer.price_calculation.data.repositories

import com.zedevstuds.price_equalizer.price_calculation.data.database.ProductsDatabase
import com.zedevstuds.price_equalizer.price_calculation.data.database.toData
import com.zedevstuds.price_equalizer.price_calculation.data.database.toDomain
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    database: ProductsDatabase
) : ProductRepository {

    private val dao = database.productsDao

    override suspend fun insertProduct(product: ProductModel, listName: String) {
        dao.insertProduct(product.toData(listName))
    }

    override suspend fun deleteProduct(product: ProductModel, listName: String) {
        dao.deleteProduct(product.toData(listName))
    }

    override fun getAllProducts(): Flow<List<ProductModel>> {
        return dao.getAllProducts().map { products ->
            products.map { it.toDomain() }
        }
    }

    override fun getProductsByListName(listName: String): Flow<List<ProductModel>> {
        return dao.getProductsByListName(listName).map { products ->
            products.map { it.toDomain() }
        }
    }

    override suspend fun insertProducts(products: List<ProductModel>, listName: String) {
        dao.insertProducts(
            products.map { it.toData(listName) }
        )
    }

    override suspend fun deleteProductsByListName(listName: String) {
        dao.deleteProductsByListName(listName)
    }

    override suspend fun updateProductListName(oldName: String, newName: String) {
        dao.updateProductListName(oldName, newName)
    }
}