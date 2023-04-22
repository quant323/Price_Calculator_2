package com.zedevstuds.price_equalizer.price_calculation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductDbModel::class], version = 1, exportSchema = false)
abstract class ProductsDatabase : RoomDatabase() {
    abstract val dao: ProductsDao
}