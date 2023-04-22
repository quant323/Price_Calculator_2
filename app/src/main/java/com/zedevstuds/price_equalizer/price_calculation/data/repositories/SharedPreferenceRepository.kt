package com.zedevstuds.price_equalizer.price_calculation.data.repositories

import android.content.SharedPreferences
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.PreferenceRepository

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

    companion object {
        private const val MEASURE_UNIT = "measure_unit"
        private const val CURRENCY = "currency"
    }
}