package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: ProductModel, listId: Int)
    suspend fun deleteProduct(product: ProductModel, listId: Int)
    fun getProductsByListId(listId: Int): Flow<List<ProductModel>>
    suspend fun deleteProductsByListId(listId: Int)
    suspend fun updateProduct(updatedProduct: ProductModel, listId: Int)
}