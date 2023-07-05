package com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.items

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
import androidx.compose.ui.tooling.preview.Preview
import com.zedevstuds.price_equalizer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcAppBar(
    title: String,
    currency: String,
    isSortEnabled: Boolean,
    isDeleteEnabled: Boolean,
    onCurrencyClicked: () -> Unit,
    onSortClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon"
                )
            }
        },
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { onSortClicked() }, enabled = isSortEnabled) {
                Icon(painter = painterResource(R.drawable.ic_sort_24), contentDescription = "Sort Products")
            }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Delete List") },
                    enabled = isDeleteEnabled,
                    onClick = {
                        onDeleteClicked()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Currency ($currency)") },
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
        isSortEnabled = true,
        isDeleteEnabled = true,
        onCurrencyClicked = {},
        onSortClicked = {},
        onDeleteClicked = {},
        onNavigationIconClick = {}
    )
}