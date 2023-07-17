package com.zedevstuds.price_equalizer.price_calculation.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listDbModel: ListDbModel)

    @Delete
    suspend fun deleteList(listDbModel: ListDbModel)

    @Update
    suspend fun updateList(listDbModel: ListDbModel)

    @Query("SELECT * FROM lists_table")
    fun getAllLists(): Flow<List<ListDbModel>>
}