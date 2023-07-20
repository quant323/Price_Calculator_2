package com.zedevstuds.price_equalizer_redesign.price_calculation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductDbModel::class, ListDbModel::class], version = 1, exportSchema = false)
abstract class ProductsDatabase : RoomDatabase() {
    abstract val productsDao: ProductsDao
    abstract val listsDao: ListsDao
}