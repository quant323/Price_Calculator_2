package com.zedevstuds.price_equalizer.price_calculation.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zedevstuds.price_equalizer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcAppBar(
    currency: String,
    isSortEnabled: Boolean,
    isSaveEnabled: Boolean,
    onCurrencyClicked: () -> Unit,
    onSortClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon"
                )
            }
        },
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = { onSortClicked() }, enabled = isSortEnabled) {
                Icon(painter = painterResource(R.drawable.ic_sort_24), contentDescription = "Sort")
            }
            IconButton(onClick = { onSaveClicked() }, enabled = isSaveEnabled) {
                Icon(painter = painterResource(R.drawable.ic_save_24), contentDescription = "Save")
            }
            IconButton(onClick = { onCurrencyClicked() }) {
                Text(text = currency, style = MaterialTheme.typography.titleLarge)
            }
        }
        //TODO sets appBar color. Just in case it is needed
//        colors = TopAppBarDefaults.smallTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//        )
    )
}

@Composable
@Preview(showBackground = true)
fun CalcAppBarPreview() {
    CalcAppBar(
        currency = "$",
        isSortEnabled = true,
        isSaveEnabled = true,
        onCurrencyClicked = {},
        onSortClicked = {},
        onSaveClicked = {},
        onNavigationIconClick = {}
    )
}