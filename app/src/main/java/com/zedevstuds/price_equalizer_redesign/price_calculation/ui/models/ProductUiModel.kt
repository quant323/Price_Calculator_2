package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.models

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel

data class ProductUiModel(
    val id: Int,
    val index: Int,
    val enteredAmount: String,
    val enteredPrice: String,
    val selectedMeasureUnit: MeasureUnit,
    val priceForOneUnit: Double,
    val title: String,
)

fun ProductUiModel.toDomain() = ProductModel(
    id = id,
    enteredAmount = enteredAmount,
    enteredPrice = enteredPrice,
    selectedMeasureUnit = selectedMeasureUnit,
    priceForOneUnit = priceForOneUnit,
    title = title
)


fun ProductModel.toUiModel(index: Int) = ProductUiModel(
    id = id,
    index = index,
    enteredAmount = enteredAmount,
    enteredPrice = enteredPrice,
    selectedMeasureUnit = selectedMeasureUnit,
    priceForOneUnit = priceForOneUnit,
    title = title
)
