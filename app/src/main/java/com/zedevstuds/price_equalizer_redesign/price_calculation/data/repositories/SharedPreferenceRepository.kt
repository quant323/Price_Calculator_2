package com.zedevstuds.price_equalizer_redesign.price_calculation.data.repositories

import android.content.SharedPreferences
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.PreferenceRepository

class SharedPreferenceRepository(
    private val preferences: SharedPreferences
) : PreferenceRepository {

    override fun getCurrencyName(default: String): String {
        return preferences.getString(CURRENCY, default) ?: default
    }

    override fun saveCurrencyName(currency: String) {
        preferences.edit().putString(CURRENCY, currency).apply()
    }

    override fun getDarkMode(default: Boolean): Boolean {
        return preferences.getBoolean(DARK_MODE, default)
    }

    override fun saveDarkMode(isDarkMode: Boolean) {
        preferences.edit().putBoolean(DARK_MODE, isDarkMode).apply()
    }

    override fun getIsSorted(default: Boolean): Boolean {
        return preferences.getBoolean(IS_SORTED, default)
    }

    override fun saveIsSorted(isSorted: Boolean) {
        preferences.edit().putBoolean(IS_SORTED, isSorted).apply()
    }

    companion object {
        private const val CURRENCY = "currency"
        private const val DARK_MODE = "dark_mode"
        private const val IS_SORTED = "is_sorted"
    }
}