package com.zedevstuds.price_equalizer.price_calculation.domain.repositories

interface PreferenceRepository {
    fun getMeasureUnitId(default: Int): Int
    fun saveMeasureUnitId(unitId: Int)
    fun getCurrencyName(default: String): String
    fun saveCurrencyName(currency: String)
}