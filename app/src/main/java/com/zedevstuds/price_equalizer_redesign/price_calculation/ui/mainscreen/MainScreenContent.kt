package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items.ProductTitleDialog
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items.ProductListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun MainScreenContent(
    productList: State<List<ProductModel>>,
    currency: String,
    scrollToFlow: Flow<MainScreenViewModel.ScrollPosition>,
    modifier: Modifier = Modifier,
    onDeleteProduct: (ProductModel) -> Unit,
    onUpdateProductTitle: (ProductModel) -> Unit,
) {
    val scrollState = rememberLazyListState()

    Column(modifier = modifier) {
        var productToEdit by remember { mutableStateOf<ProductModel?>(null) }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier,
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
            }
            if (productList.value.isEmpty()) {
                Text(
                    text = stringResource(R.string.hint_main),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
        productToEdit?.let { product ->
            ProductTitleDialog(
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

@Preview(showBackground = true)
@Composable
fun MainScreenContentPreview() {
    MainScreenContent(
        productList = remember { mutableStateOf(productModels) },
        currency = "$",
        scrollToFlow = flow {  },
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