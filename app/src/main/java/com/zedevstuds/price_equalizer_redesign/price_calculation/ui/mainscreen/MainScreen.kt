package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.NavigationDrawer
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.CurrencyUi
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.EnterParamsArea
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.AboutDialog
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.ListTitleDialog
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.CalcAppBar
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.DeleteListDialog
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.SelectCurrencyDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainScreenViewModel = koinViewModel(),
    isDarkMode: Boolean = false,
    onThemeUpdated: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val productList = mainViewModel.productList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showAddListDialog by remember { mutableStateOf(false) }
    var showDeleteListDialog by remember { mutableStateOf(false) }
    var showRenameListDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    val currentList = mainViewModel.selectedProductList.collectAsState()
    val allLists = mainViewModel.allLists.collectAsState()
    val isSortApplied = mainViewModel.isSortApplied.collectAsState()

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
                onDarkModeClicked = onThemeUpdated,
                onAddListClicked = {
                    showAddListDialog = true
                }
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
                    isModifyListEnabled = currentList.value.id != MainScreenViewModel.DEFAULT_LIST_ID,
                    onCurrencyClicked = { showCurrencyDialog = true },
                    onSortClicked = mainViewModel::onSortClicked,
                    onDeleteListClicked = { showDeleteListDialog = true },
                    onRenameListClicked = { showRenameListDialog = true },
                    onNavigationIconClick = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    onAboutClicked = { showAboutDialog = true }
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
                    onUpdateProduct = mainViewModel::updateProduct
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
            if (showRenameListDialog) {
                ListTitleDialog(
                    initialListName = currentList.value.name,
                    listsOfProducts = allLists.value,
                    onConfirm = { title ->
                        mainViewModel.updateListTitle(currentList.value.copy(name = title))
                        showRenameListDialog = false
                    },
                    onDismiss = { showRenameListDialog = false }
                )
            }
            if (showAboutDialog) {
                AboutDialog(
                    version = mainViewModel.getVersionName(),
                    onConfirm = { showAboutDialog = false },
                    onDismiss = { showAboutDialog = false }
                )
            }
            LaunchedEffect(context) {
                mainViewModel.messageId.collectLatest {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
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
