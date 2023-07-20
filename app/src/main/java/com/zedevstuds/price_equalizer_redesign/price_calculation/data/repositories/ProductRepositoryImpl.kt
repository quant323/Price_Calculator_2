package com.zedevstuds.price_equalizer_redesign.price_calculation.data.repositories

import com.zedevstuds.price_equalizer_redesign.price_calculation.data.database.ProductsDatabase
import com.zedevstuds.price_equalizer_redesign.price_calculation.data.database.toData
import com.zedevstuds.price_equalizer_redesign.price_calculation.data.database.toDomain
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    database: ProductsDatabase
) : ProductRepository {

    private val dao = database.productsDao

    override suspend fun insertProduct(product: ProductModel, listId: Int) {
        dao.insertProduct(product.toData(listId))
    }

    override suspend fun deleteProduct(product: ProductModel, listId: Int) {
        dao.deleteProduct(product.toData(listId))
    }

    override fun getProductsByListId(listId: Int): Flow<List<ProductModel>> {
        return dao.getProductsByListId(listId).map { products ->
            products.map { it.toDomain() }
        }
    }

    override suspend fun deleteProductsByListId(listId: Int) {
        dao.deleteProductsByListId(listId)
    }

    override suspend fun updateProduct(updatedProduct: ProductModel, listId: Int) {
        dao.updateProduct(updatedProduct.toData(listId))
    }
}
