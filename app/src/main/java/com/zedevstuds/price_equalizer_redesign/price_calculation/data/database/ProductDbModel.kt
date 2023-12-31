package com.zedevstuds.price_equalizer_redesign.price_calculation.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.listOfUnits

private const val TABLE_NAME = "products_table"

@Entity(tableName = TABLE_NAME)
data class ProductDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "list_id")
    val listId: Int,

    @ColumnInfo(name = "entered_amount")
    val enteredAmount: String,

    @ColumnInfo(name = "entered_price")
    val enteredPrice: String,

    @ColumnInfo(name = "selected_measure_unit")
    val selectedMeasureUnitId: Int,

    @ColumnInfo(name = "price_for_one_unit")
    val priceForOneUnit: Double,

    val title: String,

    @ColumnInfo(name = "is_metric")
    val isMetric: Boolean = true
)

fun ProductDbModel.toDomain() =
    ProductModel(
        id = id,
        enteredAmount = enteredAmount,
        enteredPrice = enteredPrice,
        selectedMeasureUnit = listOfUnits.firstOrNull { it.id == selectedMeasureUnitId }
            ?: listOfUnits.first(),
        priceForOneUnit = priceForOneUnit,
        title = title
    )

fun ProductModel.toData(listId: Int) =
    ProductDbModel(
        id = id,
        enteredAmount = enteredAmount,
        enteredPrice = enteredPrice,
        selectedMeasureUnitId = selectedMeasureUnit.id,
        priceForOneUnit = priceForOneUnit,
        title = title,
        listId = listId
    )
