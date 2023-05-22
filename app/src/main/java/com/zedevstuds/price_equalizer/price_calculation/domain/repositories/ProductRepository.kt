package com.zedevstuds.price_equalizer.price_calculation.domain.repositories

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: ProductModel, listName: String)
    suspend fun deleteProduct(product: ProductModel, listName: String)
    fun getAllProducts(): Flow<List<ProductModel>>
    suspend fun insertProducts(products: List<ProductModel>, listName: String)
    fun getProductsByListName(listName: String): Flow<List<ProductModel>>
    suspend fun deleteProductsByListName(listName: String)
    suspend fun updateProductListName(oldName: String, newName: String)

//    suspend fun updateListName(newListName: String)
}