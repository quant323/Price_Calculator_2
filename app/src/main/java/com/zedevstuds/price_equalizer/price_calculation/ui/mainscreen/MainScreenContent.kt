package com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.EditProductTitleDialog
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.ProductListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun MainScreenContent(
    productList: List<ProductModel>,
    currency: String,
    scrollToFlow: Flow<MainScreenViewModel.ScrollPosition>,
    enterParamsArea: @Composable () -> Unit,
    onDeleteProduct: (ProductModel) -> Unit,
    onUpdateProductTitle: (ProductModel) -> Unit,
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .padding(
                bottom = 8.dp,
                start = 8.dp,
                end = 8.dp
            )
    ) {
        var productToEdit by remember { mutableStateOf<ProductModel?>(null) }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollState
        ) {
            itemsIndexed(productList) {index, product ->
                ProductListItem(
                    product = product,
                    bestPriceProduct = getBestPriceProduct(productList),
                    currency = currency,
                    index = index + 1,
                    modifier = Modifier.fillMaxWidth(),
                    onEditClicked = {
                        productToEdit = product
                    },
                    onDeleteClicked = {
                        onDeleteProduct(product)
                    }
                )
            }
        }
        enterParamsArea()
        productToEdit?.let { product ->
            EditProductTitleDialog(
                currentTitle = product.title,
                onConfirm = {
                    onUpdateProductTitle(product.copy(title = it))
                    productToEdit = null
                },
                onDismiss = { productToEdit = null }
            )
        }
    }

    LaunchedEffect(Unit) {
        scrollToFlow.collect {
            if (productList.isEmpty()) return@collect
            val itemIndex = when (it) {
                MainScreenViewModel.ScrollPosition.FIRST -> 0
                MainScreenViewModel.ScrollPosition.LAST -> productList.lastIndex
            }
            scrollState.scrollToItem(itemIndex)
        }
    }
}

private fun getBestPriceProduct(productList: List<ProductModel>): ProductModel? {
    return if (productList.size > 1) {
        productList.minByOrNull { it.priceForOneUnit }
    } else null
}

@Preview(showBackground = true)
@Composable
fun MainScreenContentPreview() {
    MainScreenContent(
        productList = productModels,
        currency = "$",
        scrollToFlow = flow {  },
        enterParamsArea = {},
        onDeleteProduct = {},
        onUpdateProductTitle = {}
    )
}

private val productModel =
    ProductModel(
        id = 1,
        enteredAmount = "5",
        enteredPrice = "7",
        selectedMeasureUnit = MeasureUnit.KG,
        priceForOneUnit = 11.2,
        title = "Product"
    )

private val productModels = listOf(
    productModel,
    productModel,
    productModel,
    productModel,
    productModel,
    productModel,
    productModel
)