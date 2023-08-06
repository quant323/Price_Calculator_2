package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.drawer.NavigationDrawer
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.CurrencyUi
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.EnterParamsArea
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items.ListTitleDialog
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items.CalcAppBar
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items.DeleteListDialog
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items.SelectCurrencyDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainScreenViewModel = koinViewModel(),
    isDarkMode: Boolean = false,
    onThemeUpdated: (Boolean) -> Unit = {}
) {
    val productList = mainViewModel.productList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showAddListDialog by remember { mutableStateOf(false) }
    var showDeleteListDialog by remember { mutableStateOf(false) }
    val currentList = mainViewModel.selectedProductList.collectAsState()
    val allLists = mainViewModel.allLists.collectAsState()
    val isSortApplied = mainViewModel.isSortApplied.collectAsState()
    var listToChangeName by remember { mutableStateOf<ListModel?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                items = allLists.value,
                selectedItem = currentList.value,
                version = mainViewModel.getVersionName(),
                isDarkMode = isDarkMode,
                scope = coroutineScope,
                drawerState = drawerState,
                onListClicked = {
                    mainViewModel.onListClicked(it)
                },
                onEditList = {
                    listToChangeName = it
                },
                onDarkModeClicked = onThemeUpdated
            )
        }
    ) {
        Scaffold(
            topBar = {
                CalcAppBar(
                    title = currentList.value.name,
                    currency = mainViewModel.getCurrency().sign,
                    isSortEnabled = productList.value.size > 1,
                    isSortApplied = isSortApplied.value,
                    isDeleteEnabled = currentList.value.id != MainScreenViewModel.DEFAULT_LIST_ID,
                    onCurrencyClicked = { showCurrencyDialog = true },
                    onSortClicked = mainViewModel::onSortClicked,
                    onCreateListClicked = { showAddListDialog = true },
                    onDeleteListClicked = { showDeleteListDialog = true },
                    onNavigationIconClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                )
            ) {
                MainScreenContent(
                    productList = productList,
                    currency = mainViewModel.getCurrency().sign,
                    scrollToFlow = mainViewModel.scrollTo,
                    modifier = Modifier.weight(1f),
                    onDeleteProduct = mainViewModel::onDeleteProduct,
                    onUpdateProductTitle = { product ->
                        mainViewModel.updateProductTitle(product)
                    }
                )
                EnterParamsArea(viewModel = mainViewModel.enterParamsViewModel)
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
            if (showAddListDialog) {
                ListTitleDialog(
                    initialListName = stringResource(R.string.dialog_default_list_title),
                    listsOfProducts = allLists.value,
                    onConfirm = { title ->
                        mainViewModel.addProductList(title)
                        showAddListDialog = false
                        coroutineScope.launch { drawerState.close() }
                    },
                    onDismiss = { showAddListDialog = false }
                )
            }
            if (showDeleteListDialog) {
                DeleteListDialog(
                    listTitle = currentList.value.name,
                    onConfirm = {
                        mainViewModel.deleteProductList()
                        showDeleteListDialog = false
                    },
                    onDismiss = { showDeleteListDialog = false }
                )
            }
            listToChangeName?.let { listModel ->
                ListTitleDialog(
                    initialListName = listModel.name,
                    listsOfProducts = allLists.value,
                    onConfirm = { title ->
                        mainViewModel.updateListTitle(listModel.copy(name = title))
                        listToChangeName = null
                    },
                    onDismiss = { listToChangeName = null }
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
