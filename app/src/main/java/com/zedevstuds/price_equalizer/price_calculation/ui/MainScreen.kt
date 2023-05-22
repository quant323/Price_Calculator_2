package com.zedevstuds.price_equalizer.price_calculation.ui

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.CalcAppBar
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.EditTitleDialog
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.EnterParamsArea
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.ListTitleDialog
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.NavigationDrawer
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.ProductListItem
import com.zedevstuds.price_equalizer.price_calculation.ui.compose.SelectCurrencyDialog
import com.zedevstuds.price_equalizer.price_calculation.ui.models.CurrencyUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainScreenViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showListTitleDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(mainViewModel.drawerViewModel, coroutineScope, drawerState)
        }
    ) {
        Scaffold(
            topBar = {
                CalcAppBar(
                    currency = mainViewModel.getCurrency().sign,
                    isSortEnabled = true,   // TODO fix
//                    isSortEnabled = mainViewModel.productList.value.size > 1,
                    isSaveEnabled = true,
                    onCurrencyClicked = { showCurrencyDialog = true },
                    onSortClicked = { mainViewModel.onSortClicked() },
                    onSaveClicked = { showListTitleDialog = true },
                    onNavigationIconClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
        ) { contentPadding ->
            Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
                MainScreenContent(
                    mainScreenViewModel = mainViewModel,
                    scope = coroutineScope
                )
            }
            if (showCurrencyDialog) {
                SelectCurrencyDialog(
                    currencyList = CurrencyUi.currencyList,
                    currentCurrency = mainViewModel.getCurrency(),
                    onConfirm = {
                        mainViewModel.onCurrencyChanged(it)
                        showCurrencyDialog = false
                    },
                    onDismiss = { showCurrencyDialog = false }
                )
            }
            if (showListTitleDialog) {
                ListTitleDialog(
                    initialTile = MainScreenViewModel.DEFAULT_LIST_NAME,
                    onConfirm = { title ->
                        mainViewModel.saveProductList(title)
                        showListTitleDialog = false
                    },
                    onDismiss = { showListTitleDialog = false }
                )
            }
        }
    }
}

@Composable
fun MainScreenContent(
    mainScreenViewModel: MainScreenViewModel,
    scope: CoroutineScope,
) {
    val productList = mainScreenViewModel.productList.collectAsState(emptyList())
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
            items(productList.value, key = { it.id }) {product ->
                val currency = mainScreenViewModel.getCurrency().sign
                ProductListItem(
                    product = product,
                    bestPriceProduct = getBestPriceProduct(productList.value),
                    currency = currency,
                    modifier = Modifier.fillMaxWidth(),
                    onEditClicked = {
                        productToEdit = product
                    },
                    onDeleteClicked = {
                        mainScreenViewModel.onDeleteProduct(product)
                    }
                )
            }
//            items(30) {
//                ProductListItem(title = "Product 1",  enteredParams = "5", calculatedParams = "10")
//            }
        }

        EnterParamsArea(
            viewModel = mainScreenViewModel.enterParamsViewModel,
            onProductAdded = {
                if (productList.value.isNotEmpty()) {
                    scope.launch {
                        scrollState.scrollToItem(productList.value.size - 1)
                    }
                }
            }
        )
        productToEdit?.let { product ->
            EditTitleDialog(
                currentTitle = product.title,
                onConfirm = {
                    product.title = it
                    productToEdit = null
                },
                onDismiss = { productToEdit = null }
            )
        }
    }
}

private fun getBestPriceProduct(productList: List<ProductModel>): ProductModel? {
    return if (productList.size > 1) {
        productList.minByOrNull { it.priceForOneUnit }
    } else null
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun MainScreenPreview() {
    PriceCalculatorTheme {
        MainScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreviewNight() {
    PriceCalculatorTheme {
        MainScreen()
    }
}
