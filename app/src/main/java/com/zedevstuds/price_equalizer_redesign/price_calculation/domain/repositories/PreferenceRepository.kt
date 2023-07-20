package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories

interface PreferenceRepository {
    fun getMeasureUnitId(default: Int): Int
    fun saveMeasureUnitId(unitId: Int)
    fun getCurrencyName(default: String): String
    fun saveCurrencyName(currency: String)
    fun getDarkMode(default: Boolean): Boolean
    fun saveDarkMode(isDarkMode: Boolean)
}