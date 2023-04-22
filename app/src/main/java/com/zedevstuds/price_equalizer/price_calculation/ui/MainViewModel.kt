package com.zedevstuds.price_equalizer.price_calculation.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.repositories.PreferenceRepository
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.DeleteListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.GetPriceForOneUnitUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.GetProductsForListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.SaveProductsUseCase
import com.zedevstuds.price_equalizer.price_calculation.ui.models.CurrencyUi
import kotlinx.coroutines.launch

const val TAG = "myTag"

class MainScreenViewModel(
    val enterParamsViewModel: EnterParamsViewModel,
    private val preferenceRepository: PreferenceRepository,
    private val getPriceForOneUnitUseCase: GetPriceForOneUnitUseCase,
    private val saveProductsUseCase: SaveProductsUseCase,
    private val getProductsForListUseCase: GetProductsForListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
) : ViewModel() {

    val productList: SnapshotStateList<ProductModel> = mutableStateListOf()

    private var sortByPrice = false

    fun onDeleteProduct(product: ProductModel) {
        productList.remove(product)
    }

    fun onCurrencyChanged(currency: CurrencyUi) {
        enterParamsViewModel.onCurrencyChanged(currency)
    }

    fun getCurrency() = enterParamsViewModel.enterParamsViewState.value.currency

    fun onSortClicked() {
        sortByPrice = !sortByPrice
        if (sortByPrice) {
            productList.sortBy { it.priceForOneUnit }
        } else {
            productList.sortBy { it.id }
        }
    }

    fun saveProductList(listName: String = TEST_LIST_NAME) {
        viewModelScope.launch {
            saveProductsUseCase.execute(products = productList, listName = listName)
        }
    }

    fun loadProductList(listName: String = TEST_LIST_NAME) {
        viewModelScope.launch {
            productList.apply {
                clear()
                addAll(getProductsForListUseCase.execute(listName))
            }
        }
    }

    companion object {
        const val MAX_TITLE_LENGTH = 12
        const val DEFAULT_LIST_NAME = "List 1"
        private const val TEST_LIST_NAME = "test_list"
    }
}
