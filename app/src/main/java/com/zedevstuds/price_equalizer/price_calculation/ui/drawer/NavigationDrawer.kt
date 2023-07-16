package com.zedevstuds.price_equalizer.price_calculation.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items.AddListDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val APPBAR_HEIGHT = 64.dp
private const val DRAWER_WIDTH = 0.8

@Composable
fun NavigationDrawer(
    viewModel: DrawerViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    val items = viewModel.listsOfProducts.collectAsState(emptyList())
    val selectedItem = viewModel.selectedItem.collectAsState()
    var showAddListDialog by remember { mutableStateOf(false) }

    NavDrawerContent(
        itemLists = items.value,
        selectedItem = selectedItem.value,
        onListClicked = { item ->
            viewModel.onListClicked(item)
            scope.launch { drawerState.close() }
        },
        onBackClicked = {
            scope.launch { drawerState.close() }
        },
        onAddListClicked = { showAddListDialog = true }
    )

    if (showAddListDialog) {
        AddListDialog(
            initialTile = stringResource(R.string.dialog_default_list_title),
            listsOfProducts = items.value,
            onConfirm = { title ->
                viewModel.addList(title)
                showAddListDialog = false
                scope.launch { drawerState.close() }
            },
            onDismiss = { showAddListDialog = false }
        )
    }
}

@Composable
fun NavDrawerContent(
    itemLists: List<ListModel>,
    selectedItem: ListModel,
    onListClicked: (ListModel) -> Unit,
    onBackClicked: () -> Unit,
    onAddListClicked: () -> Unit
) {
    val drawerWidth = getDrawerWidth(LocalConfiguration.current.screenWidthDp)

    ModalDrawerSheet(modifier = Modifier.width(drawerWidth)) {
        Box(
            modifier = Modifier
                .height(APPBAR_HEIGHT)
                .padding(start = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.close_drawer_cont_desc),
                modifier = Modifier.clickable {
                    onBackClicked()
                }
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(itemLists) {productList ->
                NavigationDrawerItem(
                    label = { Text(productList.name) },
                    selected = productList == selectedItem,
                    onClick = {
                        onListClicked(productList)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
        IconButton(
            onClick = { onAddListClicked() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_circle_outline_24),
                contentDescription = stringResource(R.string.add_list_cont_desc)
            )
        }
        Box(modifier = Modifier.height(8.dp))
    }
}

private fun getDrawerWidth(screenWidth: Int): Dp = (screenWidth * DRAWER_WIDTH).toInt().dp

@Preview
@Composable
fun DrawerPreview() {
    NavDrawerContent(
        itemLists = listItems,
        selectedItem = listItems.first(),
        onListClicked = {},
        onBackClicked = {},
        onAddListClicked = {}
    )
}

private val listItems = listOf(
    ListModel(0, "Current List"),
    ListModel(1, "Pasta"),
    ListModel(2, "Cheese"),
    ListModel(3, "Apples"),
    ListModel(4, "Current List"),
    ListModel(5, "Pasta"),
    ListModel(6, "Cheese"),
    ListModel(7, "Apples"),
    ListModel(8, "Current List"),
    ListModel(9, "Pasta"),
    ListModel(10, "Cheese"),
    ListModel(11, "Apples"),
    ListModel(12, "Current List"),
    ListModel(13, "Pasta"),
    ListModel(14, "Cheese"),
    ListModel(15, "Apples")
)

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ModalNavigationDrawerSample(content: @Composable () -> Unit) {
//    val drawerState = rememberDrawerState(DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    // icons to mimic drawer destinations
//    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
//    val selectedItem = remember { mutableStateOf(items[0]) }
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet {
//                Spacer(Modifier.height(12.dp))
//                items.forEach { item ->
//                    NavigationDrawerItem(
//                        icon = { Icon(item, contentDescription = null) },
//                        label = { Text(item.name) },
//                        selected = item == selectedItem.value,
//                        onClick = {
//                            scope.launch { drawerState.close() }
//                            selectedItem.value = item
//                        },
//                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                    )
//                }
//            }
//        },
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(text = if (drawerState.isClosed) ">>> Swipe >>>" else "<<< Swipe <<<")
//                Spacer(Modifier.height(20.dp))
//                Button(onClick = { scope.launch { drawerState.open() } }) {
//                    Text("Click to open")
//                }
//            }
//        }
//    )
//}