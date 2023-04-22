package com.zedevstuds.price_equalizer.price_calculation.data.di

import androidx.room.Room
import com.zedevstuds.price_equalizer.price_calculation.data.database.ProductsDatabase
import com.zedevstuds.price_equalizer.price_calculation.data.repositories.ProductRepositoryImp
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.ProductRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE_NAME = "products_database"

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), ProductsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single<ProductRepository> {
        ProductRepositoryImp(database = get())
    }
}