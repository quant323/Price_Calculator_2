package com.zedevstuds.price_equalizer.price_calculation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.list.AddListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.AddProductUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.ClearListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.DeleteListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.DeleteProductUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.GetProductsForListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.SaveProductsUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.UpdateProductListNameUseCase
import com.zedevstuds.price_equalizer.price_calculation.ui.models.CurrencyUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

const val TAG = "myTag"
const val AUTOGENERATE_ID = 0

class MainScreenViewModel(
    val enterParamsViewModel: EnterParamsViewModel,
    val drawerViewModel: DrawerViewModel,
    private val addProductUseCase: AddProductUseCase,
    private val saveProductsUseCase: SaveProductsUseCase,
    private val getProductsForListUseCase: GetProductsForListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val clearListUseCase: ClearListUseCase,
    private val addListUseCase: AddListUseCase,
    private val updateProductListNameUseCase: UpdateProductListNameUseCase,
) : ViewModel() {

    private var currentListName = MutableStateFlow(DEFAULT_LIST_NAME)
    private var sortByPrice = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val productList = currentListName.combine(sortByPrice) { a, b ->
        getProductsForListUseCase.execute(a, b)
    }.flatMapLatest {
        it
    }

    init {
        subscribeToEnterParamsEvents()
        subscribeToDrawerEvents()
    }

    fun onDeleteProduct(product: ProductModel) {
        viewModelScope.launch {
            deleteProductUseCase.execute(product, currentListName.value)
        }
    }

    fun onCurrencyChanged(currency: CurrencyUi) {
        enterParamsViewModel.onCurrencyChanged(currency)
    }

    fun getCurrency() = enterParamsViewModel.enterParamsViewState.value.currency

    fun onSortClicked() {
        sortByPrice.value = !sortByPrice.value
    }

    fun saveProductList(listName: String) {

        viewModelScope.launch {
            addListUseCase.execute(listName)
            updateProductListNameUseCase.execute(
                oldName = currentListName.value,
                newName = listName
            )
        }
    }

    private fun subscribeToEnterParamsEvents() {
        viewModelScope.launch {
            enterParamsViewModel.events.collect { event ->
                when (event) {
                    is EnterParamsViewModel.EnterParamsEvent.AddProductEvent -> {
                        addProductUseCase.execute(event.product, currentListName.value)
                    }
                    is EnterParamsViewModel.EnterParamsEvent.CleanListEvent -> {
                        clearListUseCase.execute(currentListName.value)
                    }
                }
            }
        }
    }

    private fun subscribeToDrawerEvents() {
        viewModelScope.launch {
            drawerViewModel.events.collect { event ->
                when (event) {
                    is DrawerViewModel.DrawerEvent.OnListClickEvent -> {
                        currentListName.value = event.listModel.name
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_TITLE_LENGTH = 12
        const val DEFAULT_LIST_NAME = "FAST LIST"
    }
}
