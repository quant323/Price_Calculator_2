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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.EditProductTitleDialog
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.ProductListItem
import kotlinx.coroutines.flow.Flow

@Composable
fun MainScreenContent(
    productList: State<List<ProductModel>>,
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
            itemsIndexed(productList.value) {index, product ->
                ProductListItem(
                    product = product,
                    bestPriceProduct = getBestPriceProduct(productList.value),
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
//            items(30) {
//                ProductListItem(title = "Product 1",  enteredParams = "5", calculatedParams = "10")
//            }
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
            if (productList.value.isEmpty()) return@collect
            val itemIndex = when (it) {
                MainScreenViewModel.ScrollPosition.FIRST -> 0
                MainScreenViewModel.ScrollPosition.LAST -> productList.value.lastIndex
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