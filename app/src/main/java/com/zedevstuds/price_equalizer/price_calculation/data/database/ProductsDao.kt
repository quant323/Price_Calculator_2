package com.zedevstuds.price_equalizer.price_calculation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductDbModel>)

    @Query("SELECT * FROM products_table WHERE list_name = :listName")
    suspend fun getProductsByListName(listName: String): List<ProductDbModel>

    @Query("DELETE FROM products_table WHERE list_name = :listName")
    suspend fun deleteProductsByListName(listName: String)

//    @Update
//    suspend fun updateListName(newListName: String)
}