package com.zedevstuds.price_equalizer_redesign.core

import androidx.lifecycle.ViewModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.repositories.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    var isDarkMode = MutableStateFlow(
        preferenceRepository.getDarkMode(false)
    )

    fun onDarkModeChanged(isDarkMode: Boolean) {
        this.isDarkMode.value = isDarkMode
        preferenceRepository.saveDarkMode(isDarkMode)
    }

}