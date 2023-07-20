package com.zedevstuds.price_equalizer_redesign.price_calculation.data.database

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

    @Query("SELECT * FROM products_table WHERE list_id = :listId")
    fun getProductsByListId(listId: Int): Flow<List<ProductDbModel>>

    @Query("DELETE FROM products_table WHERE list_id = :listId")
    suspend fun deleteProductsByListId(listId: Int)
}