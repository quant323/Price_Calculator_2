package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val APPBAR_HEIGHT = 64.dp
private const val DRAWER_WIDTH = 0.8

@Composable
fun NavigationDrawer(
    items: List<ListModel>,
    selectedItem: ListModel,
    version: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    isDarkMode: Boolean,
    onListClicked: (ListModel) -> Unit,
    onEditList: (ListModel) -> Unit,
    onDarkModeClicked: (Boolean) -> Unit,
) {

    NavDrawerContent(
        itemLists = items,
        selectedItem = selectedItem,
        version = version,
        isDarkMode = isDarkMode,
        onListClicked = { item ->
            onListClicked(item)
            scope.launch { drawerState.close() }
        },
        onEditListClicked = onEditList,
        onBackClicked = {
            scope.launch { drawerState.close() }
        },
        onDarkModeClicked = onDarkModeClicked,
    )
}

@Composable
fun NavDrawerContent(
    itemLists: List<ListModel>,
    selectedItem: ListModel,
    version: String,
    isDarkMode: Boolean,
    onListClicked: (ListModel) -> Unit,
    onEditListClicked: (ListModel) -> Unit,
    onBackClicked: () -> Unit,
    onDarkModeClicked: (Boolean) -> Unit,
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
                MenuItemDrawer(
                    title = productList.name,
                    selected = productList == selectedItem,
                    isEditVisible = productList.id != MainScreenViewModel.DEFAULT_LIST_ID,
                    onItemClicked = {
                        onListClicked(productList)
                    },
                    onEditTitleClicked = {
                        onEditListClicked(productList)
                    }
                )
            }
        }
        DarkModeItemSelector(
            isChecked = isDarkMode,
            onCheckedChange = onDarkModeClicked
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.version_text, version)) },
            selected = false,
            onClick = { },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun MenuItemDrawer(
    title: String,
    selected: Boolean,
    isEditVisible: Boolean,
    onItemClicked: () -> Unit,
    onEditTitleClicked: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, modifier = Modifier.weight(1f))
                if (isEditVisible) {
                    IconButton(onClick = onEditTitleClicked) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit_24),
                            contentDescription = stringResource(R.string.edit_list_cont_desc)
                        )
                    }
                }
            }
        },
        selected = selected,
        onClick = onItemClicked,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun DarkModeItemSelector(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var isSwitchChecked by remember { mutableStateOf(isChecked) }
    NavigationDrawerItem(
        label = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Mode", modifier = Modifier.weight(1f))
                Switch(
                    checked = isSwitchChecked,
                    onCheckedChange = {
                        onCheckedChange(it)
                        isSwitchChecked = !isSwitchChecked
                    }
                )
            }
        },
        selected = false,
        onClick = {},
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

private fun getDrawerWidth(screenWidth: Int): Dp = (screenWidth * DRAWER_WIDTH).toInt().dp

@Preview
@Composable
fun DrawerPreview() {
    NavDrawerContent(
        itemLists = listItems,
        selectedItem = listItems.first(),
        version = "0.2",
        isDarkMode = false,
        onListClicked = {},
        onEditListClicked = {},
        onBackClicked = {},
        onDarkModeClicked = {},
    )
}

@Preview(showBackground = true)
@Composable
fun MenuItemDrawerPreview() {
    MenuItemDrawer(
        title = "Fast List",
        selected = false,
        isEditVisible = true,
        onItemClicked = {},
        onEditTitleClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DarkModeItemSelectorPreview() {
    DarkModeItemSelector(
        isChecked = true,
        onCheckedChange = {}
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