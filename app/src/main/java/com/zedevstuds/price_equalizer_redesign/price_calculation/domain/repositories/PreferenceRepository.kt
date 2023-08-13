package com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories

interface PreferenceRepository {
    fun getCurrencyName(default: String): String
    fun saveCurrencyName(currency: String)
    fun getDarkMode(default: Boolean): Boolean
    fun saveDarkMode(isDarkMode: Boolean)
    fun getIsSorted(default: Boolean): Boolean
    fun saveIsSorted(isSorted: Boolean)
}