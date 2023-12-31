package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models

data class ProductModel(
    val id: Int,
    val enteredAmount: String,
    val enteredPrice: String,
    val selectedMeasureUnit: MeasureUnit,
    val priceForOneUnit: Double,
    val title: String,
)
