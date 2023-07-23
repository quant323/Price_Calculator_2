package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zedevstuds.price_equalizer_redesign.BuildConfig
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.AddListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.DeleteListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.GetAllListsUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.list.UpdateListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.AddProductUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.DeleteProductUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.DeleteProductsInListUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.GetProductsForListByListIdUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.UpdateProductTitleUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.CurrencyUi
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.EnterParamsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val TAG = "myTag"
const val AUTOGENERATE_ID = 0

class MainScreenViewModel(
    val enterParamsViewModel: EnterParamsViewModel,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductsInListUseCase: DeleteProductsInListUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductsForListByListIdUseCase: GetProductsForListByListIdUseCase,
    private val updateProductTitleUseCase: UpdateProductTitleUseCase,
    private val addListUseCase: AddListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
    private val updateListUseCase: UpdateListUseCase,
    private val getAllListsUseCase: GetAllListsUseCase,
    context: Context,
) : ViewModel() {

    private val defaultList = ListModel(
        id = DEFAULT_LIST_ID,
        name = context.getString(R.string.default_list_title)
    )
    val selectedProductList = MutableStateFlow(defaultList)
    val allLists = MutableStateFlow(emptyList<ListModel>())
    val isSortApplied = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val productList = selectedProductList.combine(isSortApplied) { listModel, isSortByPrice ->
        getProductsForListByListIdUseCase.execute(listModel.id, isSortByPrice)
    }.flatMapLatest {
        it
    }

    val scrollTo = MutableSharedFlow<ScrollPosition>()


    init {
        getProductLists()
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
            isSortApplied.value = !isSortApplied.value
            scrollToItem(ScrollPosition.FIRST)
        }
    }

    fun addProductList(listName: String) {
        viewModelScope.launch {
            addListUseCase.execute(listName)
        }
    }

    fun updateListTitle(listModel: ListModel) {
        viewModelScope.launch {
            updateListUseCase.execute(listModel)
        }
    }

    fun deleteProductList() {
        if (selectedProductList.value.id == DEFAULT_LIST_ID) return
        viewModelScope.launch {
            deleteProductsInListUseCase.execute(selectedProductList.value.id)
            deleteListUseCase.execute(selectedProductList.value)
        }
    }

    fun updateProductTitle(updatedProduct: ProductModel) {
        viewModelScope.launch {
            updateProductTitleUseCase.execute(updatedProduct, selectedProductList.value.id)
        }
    }

    fun getVersionName() = BuildConfig.VERSION_NAME

    fun onListClicked(listModel: ListModel) {
        selectedProductList.value = listModel
    }

    private fun getProductLists() {
        viewModelScope.launch {
            getAllListsUseCase.execute().map {
                (listOf(defaultList) + it).also { totalList ->
                    selectedProductList.value =
                        getSelectedItem(newList = totalList, oldList = allLists.value)
                    allLists.value = totalList
                }
            }
                .collect()
        }
    }

    private fun getSelectedItem(newList: List<ListModel>, oldList: List<ListModel>): ListModel {
        return when {
            oldList.isEmpty() -> newList.first()
            newList.size > oldList.size -> newList.last()
            newList.size < oldList.size -> newList.first()
            else -> newList.first { it.id == selectedProductList.value.id }
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

    enum class ScrollPosition {
        FIRST, LAST
    }

    companion object {
        const val MAX_TITLE_LENGTH = 12
        const val DEFAULT_LIST_ID = -1
        private const val DELAY_BEFORE_SCROLL = 200L
    }
}