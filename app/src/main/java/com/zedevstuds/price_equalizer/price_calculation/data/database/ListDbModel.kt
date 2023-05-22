package com.zedevstuds.price_equalizer.price_calculation.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel

private const val TABLE_NAME = "lists_table"

@Entity(tableName = TABLE_NAME)
data class ListDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String
)

fun ListDbModel.toDomain() =
    ListModel(
        id = id,
        name = name
    )

fun ListModel.toData() =
    ListDbModel(
        id = id,
        name = name
    )
