package com.zedevstuds.price_equalizer.price_calculation.domain.models

data class ProductModel(
    val id: Int,
    val enteredAmount: String,
    val enteredPrice: String,
    val selectedMeasureUnit: MeasureUnit,
    val priceForOneUnit: Double,
    var title: String,
)
