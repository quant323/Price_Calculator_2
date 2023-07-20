package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zedevstuds.price_equalizer_redesign.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcAppBar(
    title: String,
    currency: String,
    isSortEnabled: Boolean,
    isSortApplied: Boolean,
    isDeleteEnabled: Boolean,
    onCurrencyClicked: () -> Unit,
    onSortClicked: () -> Unit,
    onCreateListClicked: () -> Unit,
    onDeleteListClicked: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.show_menu_cont_desc)
                )
            }
        },
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { onSortClicked() }, enabled = isSortEnabled) {
                Icon(
                    painter = painterResource(
                        id = if (isSortApplied) R.drawable.ic_filter_list_off_24
                        else R.drawable.ic_filter_list_24
                    ),
                    contentDescription = stringResource(R.string.sort_products_cont_desc)
                )
            }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.create_list_menu_title)) },
                    onClick = {
                        onCreateListClicked()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.delete_list_menu_title)) },
                    enabled = isDeleteEnabled,
                    onClick = {
                        onDeleteListClicked()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.currency_menu_title, currency)) },
                    onClick = {
                        onCurrencyClicked()
                        showMenu = false
                    }
                )
            }
        }
        //sets appBar color. Just in case it is needed
//        colors = TopAppBarDefaults.smallTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//        )
    )
}

@Composable
@Preview(showBackground = true)
fun CalcAppBarPreview() {
    CalcAppBar(
        title = "Fast List",
        currency = "$",
        isSortApplied = false,
        isSortEnabled = true,
        isDeleteEnabled = true,
        onCurrencyClicked = {},
        onSortClicked = {},
        onCreateListClicked = {},
        onDeleteListClicked = {},
        onNavigationIconClick = {}
    )
}