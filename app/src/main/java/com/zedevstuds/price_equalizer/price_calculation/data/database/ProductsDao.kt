package com.zedevstuds.price_equalizer.price_calculation.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productDbModel: ProductDbModel)

    @Delete
    suspend fun deleteProduct(productDbModel: ProductDbModel)

    @Update
    suspend fun updateProduct(productDbModel: ProductDbModel)

    @Query("SELECT * FROM products_table")
    fun getAllProducts(): Flow<List<ProductDbModel>>

    @Query("SELECT * FROM products_table WHERE list_name = :listName")
    fun getProductsByListName(listName: String): Flow<List<ProductDbModel>>

    @Query("UPDATE products_table SET list_name = :newName WHERE list_name = :oldName")
    suspend fun updateProductListName(oldName: String, newName: String)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductDbModel>)

    @Query("DELETE FROM products_table WHERE list_name = :listName")
    suspend fun deleteProductsByListName(listName: String)
}