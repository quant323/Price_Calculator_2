package com.zedevstuds.price_equalizer.price_calculation.ui.enterparams

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import com.zedevstuds.price_equalizer.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.models.getMainUnit
import com.zedevstuds.price_equalizer.price_calculation.domain.models.listOfUnits
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.PreferenceRepository
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.GetPriceForOneUnitUseCase
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.AUTOGENERATE_ID
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale

class EnterParamsViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val getPriceForOneUnitUseCase: GetPriceForOneUnitUseCase,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
) {

    private val _enterParamsViewState = mutableStateOf(getInitialState())
    val enterParamsViewState: State<EnterParamsViewState> = _enterParamsViewState

    val events = MutableSharedFlow<EnterParamsEvent>()

    private var numberOfCreatedProducts = 0

    fun onMeasureUnitSelected(unit: MeasureUnit) {
        _enterParamsViewState.value = enterParamsViewState.value.copy(
            selectedUnit = unit,
            mainUnit = unit.getMainUnit()
        )
        preferenceRepository.saveMeasureUnitId(enterParamsViewState.value.selectedUnit.id)
    }

    fun onAmountChanged(newAmount: String) {
        if (!isValueValid(newAmount, MAX_AMOUNT_LENGTH)) return
        _enterParamsViewState.value = enterParamsViewState.value.copy(
            enteredAmount = newAmount
        )
        calculatePriceForCustomAmount()
    }

    fun onPriceChanged(newPrice: String) {
        if (!isValueValid(newPrice, MAX_PRICE_LENGTH)) return
        _enterParamsViewState.value = enterParamsViewState.value.copy(
            enteredPrice = newPrice
        )
        calculatePriceForCustomAmount()
    }

    fun onTitleChanged(title: String) {
        _enterParamsViewState.value = enterParamsViewState.value.copy(
            title = title
        )
    }

    fun onOkClicked() {
        if (enterParamsViewState.value.enteredAmount.isEmpty() ||
            enterParamsViewState.value.enteredPrice.isEmpty()) return
        addProductToList()
        resetInitialState()
    }

    fun onCleanClicked() {
        numberOfCreatedProducts = 0
        clearList()
    }

    fun onCurrencyChanged(currency: CurrencyUi) {
        _enterParamsViewState.value = enterParamsViewState.value.copy(
            currency = currency
        )
        preferenceRepository.saveCurrencyName(enterParamsViewState.value.currency.name)
    }

    private fun addProductToList() {
        val product = ProductModel(
            id = AUTOGENERATE_ID,
            enteredAmount = enterParamsViewState.value.enteredAmount,
            enteredPrice = enterParamsViewState.value.enteredPrice,
            selectedMeasureUnit =  enterParamsViewState.value.selectedUnit,
            priceForOneUnit = enterParamsViewState.value.priceForCustomAmount.toDouble(),
            title = enterParamsViewState.value.title
        )
        scope.launch {
            events.emit(EnterParamsEvent.AddProductEvent(product))
        }
        numberOfCreatedProducts++
    }

    private fun clearList() {
        scope.launch {
            events.emit(EnterParamsEvent.CleanListEvent)
        }
        resetInitialState()
    }

    private fun calculatePriceForCustomAmount() {
        try {
            val priceForCustomAmount = getPriceForOneUnitUseCase.execute(
                amount = enterParamsViewState.value.enteredAmount,
                price = enterParamsViewState.value.enteredPrice,
                measureUnit = enterParamsViewState.value.selectedUnit
            )
            _enterParamsViewState.value = enterParamsViewState.value.copy(
                priceForCustomAmount = priceForCustomAmount.toString()
            )
        } catch (e: Exception) {
            Log.d(TAG, "calculatePriceForCustomAmount exception: ${e.message}")
        }
    }

    private fun isValueValid(value: String, maxLength: Int): Boolean {
        val noDecValue = value.replace(".", "")
        return when {
            noDecValue.isDigitsOnly().not() -> false
            noDecValue.length > maxLength -> false
            else -> true
        }
    }

    private fun getInitialState(): EnterParamsViewState {
        val measureUnit = getMeasureUnit()
        return EnterParamsViewState(
            enteredAmount = INITIAL_VALUE,
            enteredPrice = INITIAL_VALUE,
            customAmount = INITIAL_CUSTOM_AMOUNT,
            priceForCustomAmount = INITIAL_CUSTOM_PRICE,
            selectedUnit = measureUnit,
            mainUnit = measureUnit.getMainUnit(),
            listOfUnits = listOfUnits,
            title = INITIAL_TITLE,
            currency = getCurrency()
        )
    }

    private fun resetInitialState() {
        _enterParamsViewState.value = enterParamsViewState.value.copy(
            enteredAmount = INITIAL_VALUE,
            enteredPrice = INITIAL_VALUE,
            customAmount = INITIAL_CUSTOM_AMOUNT,
            priceForCustomAmount = INITIAL_CUSTOM_PRICE,
            title = INITIAL_TITLE,
//            title = "$INITIAL_TITLE ${numberOfCreatedProducts + 1}",
        )
    }

    private fun getMeasureUnit(): MeasureUnit {
        val defaultUnit = MeasureUnit.KG
        val id = preferenceRepository.getMeasureUnitId(defaultUnit.id)
        return listOfUnits.firstOrNull{ it.id == id } ?: defaultUnit
    }

    private fun getCurrency(): CurrencyUi {
        val defaultCurrency = getDefaultCurrency()
        val currencyName = preferenceRepository.getCurrencyName(default = defaultCurrency.name)
        return CurrencyUi.currencyList.firstOrNull { it.name == currencyName } ?: defaultCurrency
    }

    private fun getDefaultCurrency(): CurrencyUi {
        return when (Locale.getDefault().language) {
            LANG_RUS -> CurrencyUi.RUB
            LANG_ENG -> CurrencyUi.DOLLAR
            LANG_ES, LANG_FR, LANG_GER, LANG_IT -> CurrencyUi.EURO
            LANG_UKR -> CurrencyUi.UKR
            LANG_IND -> CurrencyUi.INDIAN
            LANG_BRA -> CurrencyUi.BRAZIL
            LANG_KAZ -> CurrencyUi.KAZAKH
            LANG_KOR -> CurrencyUi.KOREAN
            else -> CurrencyUi.DOLLAR
        }
    }

    data class EnterParamsViewState(
        val enteredAmount: String,
        val enteredPrice: String,
        val customAmount: String,
        val priceForCustomAmount: String,
        val selectedUnit: MeasureUnit,
        val mainUnit: MeasureUnit,
        val listOfUnits: List<MeasureUnit>,
        val title: String,
        val currency: CurrencyUi,
    )

    sealed class EnterParamsEvent {
        class AddProductEvent(val product: ProductModel) : EnterParamsEvent()
        object CleanListEvent : EnterParamsEvent()
    }

    companion object {
        private const val INITIAL_VALUE = ""
        private const val INITIAL_CUSTOM_AMOUNT = "1"
        private const val INITIAL_CUSTOM_PRICE = "0"
        private const val INITIAL_TITLE = "Product"
        private const val MAX_AMOUNT_LENGTH = 8
        private const val MAX_PRICE_LENGTH = 8
        private const val LANG_RUS = "ru"
        private const val LANG_ENG = "en"
        private const val LANG_IT = "it"
        private const val LANG_FR = "fr"
        private const val LANG_ES = "es"
        private const val LANG_BRA = "pt"
        private const val LANG_GER = "de"
        private const val LANG_IND = "hi"
        private const val LANG_KOR = "ko"
        private const val LANG_UKR = "uk"
        private const val LANG_KAZ = "kk"
    }
}