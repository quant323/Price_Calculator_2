package com.zedevstuds.price_equalizer.price_calculation.domain.repositories

import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel

interface ProductRepository {

    suspend fun insertProducts(products: List<ProductModel>, listName: String)

    suspend fun getProductsByListName(listName: String): List<ProductModel>

    suspend fun deleteProductsByListName(listName: String)

//    suspend fun updateListName(newListName: String)
}