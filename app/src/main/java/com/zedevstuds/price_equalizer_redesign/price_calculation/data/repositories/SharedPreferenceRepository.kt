package com.zedevstuds.price_equalizer_redesign.price_calculation.data.repositories

import android.content.SharedPreferences
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.PreferenceRepository

class SharedPreferenceRepository(
    private val preferences: SharedPreferences
) : PreferenceRepository {

    override fun getMeasureUnitId(default: Int): Int {
        return preferences.getInt(MEASURE_UNIT, default)
    }

    override fun saveMeasureUnitId(unitId: Int) {
        preferences.edit().putInt(MEASURE_UNIT, unitId).apply()
    }

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

    companion object {
        private const val MEASURE_UNIT = "measure_unit"
        private const val CURRENCY = "currency"
        private const val DARK_MODE = "dark_mode"
    }
}