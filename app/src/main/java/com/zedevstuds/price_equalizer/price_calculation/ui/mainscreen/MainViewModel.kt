package com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.AddProductUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.DeleteProductUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.DeleteProductsInListUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.GetProductsForListByListIdUseCase
import com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product.UpdateProductTitleUseCase
import com.zedevstuds.price_equalizer.price_calculation.ui.drawer.DrawerViewModel
import com.zedevstuds.price_equalizer.price_calculation.ui.enterparams.CurrencyUi
import com.zedevstuds.price_equalizer.price_calculation.ui.enterparams.EnterParamsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
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
    private val deleteProductsInListUseCase: DeleteProductsInListUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductsForListByListIdUseCase: GetProductsForListByListIdUseCase,
    private val updateProductTitleUseCase: UpdateProductTitleUseCase,
) : ViewModel() {

    val selectedProductList = drawerViewModel.selectedItem
    private val sortByPrice = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val productList = selectedProductList.combine(sortByPrice) { listModel, isSortByPrice ->
        getProductsForListByListIdUseCase.execute(listModel.id, isSortByPrice)
    }.flatMapLatest {
        it
    }

    val scrollTo = MutableSharedFlow<ScrollPosition>()

    init {
        subscribeToEnterParamsEvents()
    }

    fun onDeleteProduct(product: ProductModel) {
        viewModelScope.launch {
            deleteProductUseCase.execute(product, selectedProductList.value.id)
        }
    }

    fun onCurrencyChanged(currency: CurrencyUi) {
        enterParamsViewModel.onCurrencyChanged(currency)
    }

    fun getCurrency() = enterParamsViewModel.enterParamsViewState.value.currency

    fun onSortClicked() {
        viewModelScope.launch {
            sortByPrice.value = !sortByPrice.value
            scrollToItem(ScrollPosition.FIRST)
        }
    }

    fun onDeleteProductList() {
        if (selectedProductList.value.id == DrawerViewModel.DEFAULT_LIST_ID) return
        viewModelScope.launch {
            deleteProductsInListUseCase.execute(selectedProductList.value.id)
            drawerViewModel.deleteCurrentProductList()
        }
    }

    fun updateProductTitle(updatedProduct: ProductModel) {
        viewModelScope.launch {
            updateProductTitleUseCase.execute(updatedProduct, selectedProductList.value.id)
        }
    }

    private fun subscribeToEnterParamsEvents() {
        viewModelScope.launch {
            enterParamsViewModel.events.collect { event ->
                when (event) {
                    is EnterParamsViewModel.EnterParamsEvent.AddProductEvent -> {
                        addProductUseCase.execute(event.product, selectedProductList.value.id)
                        delay(DELAY_BEFORE_SCROLL)
                        scrollToItem(ScrollPosition.LAST)
                    }
                    is EnterParamsViewModel.EnterParamsEvent.CleanListEvent -> {
                        deleteProductsInListUseCase.execute(selectedProductList.value.id)
                    }
                }
            }
        }
    }

    private suspend fun scrollToItem(scrollPosition: ScrollPosition) {
        scrollTo.emit(scrollPosition)
    }

    companion object {
        const val MAX_TITLE_LENGTH = 12
        private const val DELAY_BEFORE_SCROLL = 200L
    }

    enum class ScrollPosition {
        FIRST, LAST
    }
}