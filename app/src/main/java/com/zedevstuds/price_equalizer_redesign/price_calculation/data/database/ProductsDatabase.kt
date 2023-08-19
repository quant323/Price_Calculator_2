package com.zedevstuds.price_equalizer_redesign.price_calculation.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductDbModel::class,
        ListDbModel::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class ProductsDatabase : RoomDatabase() {
    abstract val productsDao: ProductsDao
    abstract val listsDao: ListsDao
}