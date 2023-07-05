package com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.zedevstuds.price_equalizer.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer.price_calculation.ui.enterparams.EnterParamsArea
import com.zedevstuds.price_equalizer.price_calculation.ui.drawer.NavigationDrawer
import com.zedevstuds.price_equalizer.price_calculation.ui.drawer.DrawerViewModel
import com.zedevstuds.price_equalizer.price_calculation.ui.enterparams.CurrencyUi
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.CalcAppBar
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.DeleteListDialog
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.SelectCurrencyDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainScreenViewModel = koinViewModel()
) {
    val productList = mainViewModel.productList.collectAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showDeleteListDialog by remember { mutableStateOf(false) }
    val currentList = mainViewModel.selectedProductList.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                viewModel = mainViewModel.drawerViewModel,
                scope = coroutineScope,
                drawerState = drawerState,
            )
        }
    ) {
        Scaffold(
            topBar = {
                CalcAppBar(
                    title = currentList.value.name,
                    currency = mainViewModel.getCurrency().sign,
                    isSortEnabled = productList.value.size > 1,
                    isDeleteEnabled = currentList.value != DrawerViewModel.defaultList,
                    onCurrencyClicked = { showCurrencyDialog = true },
                    onSortClicked = { mainViewModel.onSortClicked() },
                    onDeleteClicked = { showDeleteListDialog = true },
                    onNavigationIconClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
        ) { contentPadding ->
            Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
                MainScreenContent(
                    productList = productList,
                    currency = mainViewModel.getCurrency().sign,
                    enterParamsArea = {
                        EnterParamsArea(viewModel = mainViewModel.enterParamsViewModel)
                    },
                    scrollToFlow = mainViewModel.scrollTo,
                    onDeleteProduct = {
                        mainViewModel.onDeleteProduct(it)
                    }
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
            if (showDeleteListDialog) {
                DeleteListDialog(
                    listTitle = currentList.value.name,
                    onConfirm = {
                        mainViewModel.onDeleteProductList()
                        showDeleteListDialog = false
                    },
                    onDismiss = { showDeleteListDialog = false }
                )
            }
        }
    }
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
