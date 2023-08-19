package com.zedevstuds.price_equalizer_redesign.price_calculation.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.MeasureUnit

private const val TABLE_NAME = "lists_table"

@Entity(tableName = TABLE_NAME)
data class ListDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,

    @ColumnInfo(name = "measure_unit")
    val measureUnit: MeasureUnit?
)

fun ListDbModel.toDomain() =
    ListModel(
        id = id,
        name = name,
        measureUnit = measureUnit ?: MeasureUnit.KG
    )

fun ListModel.toData() =
    ListDbModel(
        id = id,
        name = name,
        measureUnit = measureUnit
    )
