package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.ListModel
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.CurrencyUi

@Composable
fun ProductTitleDialog(
    currentTitle: String,
    onConfirm: (title: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var productName by remember { mutableStateOf(currentTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(productName) }
            ) {
                Text(text = stringResource(R.string.ok_dialog_button_title))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel_dialog_button_title))
            }
        },
        title = {
            CleanableTextField(
                title = productName,
                hint = stringResource(R.string.edit_product_title_dialog_hint),
                onValueChange = {
                    productName = it
                },
                onClear = { productName = "" }
            )
        }
    )
}

@Composable
fun ListTitleDialog(
    initialListName: String,
    listsOfProducts: List<ListModel>,
    onConfirm: (title: String) -> Unit,
    onDismiss: () -> Unit,
) {

    fun String.isNewTitle(): Boolean = listsOfProducts.none { it.name == this.trim() }

    var listName by remember { mutableStateOf(initialListName) }
    var isConfirmEnabled by remember {
        mutableStateOf(
            initialListName.isNewTitle()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(listName) },
                enabled = isConfirmEnabled,
            ) {
                Text(text = stringResource(R.string.ok_dialog_button_title))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel_dialog_button_title))
            }
        },
        title = {
            CleanableTextField(
                title = listName,
                hint = stringResource(R.string.edit_list_title_dialog_hint),
                onValueChange = {
                    listName = it
                    isConfirmEnabled = it.isNotEmpty() && it.isNewTitle()
                },
                onClear = {
                    listName = ""
                    isConfirmEnabled = false
                }
            )
        }
    )
}

@Composable
fun DeleteListDialog(
    listTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm() }
            ) {
                Text(text = stringResource(R.string.delete_dialog_button_title))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel_dialog_button_title))
            }
        },
        title = {
            Text(text = stringResource(R.string.delete_dialog_text, listTitle))
        }
    )
}

@Composable
fun SelectCurrencyDialog(
    currencyList: List<CurrencyUi>,
    currentCurrency: CurrencyUi,
    onConfirm: (CurrencyUi) -> Unit,
    onDismiss: () -> Unit,
) {
    var currency = currentCurrency

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(currency) }
            ) {
                Text(text = stringResource(R.string.ok_dialog_button_title))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel_dialog_button_title))
            }
        },
        title = {
            SelectCurrencyDropDown(
                currencyList = currencyList,
                currentCurrency = currency,
                onItemClicked = { selectedCurrency ->
                    currency = selectedCurrency
                }
            )
        }
    )
}

@Composable
fun CleanableTextField(
    title: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    TextField(
        value = title,
        label = {
            Text(hint)
        },
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onClear) {
                Icon(
                    painter = painterResource(R.drawable.ic_clear_24),
                    contentDescription = stringResource(R.string.edit_list_cont_desc)
                )
            }
        },
        onValueChange = onValueChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCurrencyDropDown(
    currencyList: List<CurrencyUi>,
    currentCurrency: CurrencyUi,
    onItemClicked: (CurrencyUi) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val initialText = getCurrencyText(currentCurrency)
    var selectedOptionText by remember { mutableStateOf(initialText) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(stringResource(id = R.string.currency_title)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            currencyList.forEach {
                val text = getCurrencyText(it)
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        selectedOptionText = text
                        onItemClicked(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun getCurrencyText(currency: CurrencyUi) = "${currency.name} (${currency.sign})"

@Preview
@Composable
fun EditTitleDialogPreview() {
    ProductTitleDialog(
        currentTitle = "Product 1",
        onConfirm = {},
        onDismiss = {}
    )
}

@Preview
@Composable
fun SelectCurrencyDialogPreview() {
    SelectCurrencyDialog(
        currencyList = CurrencyUi.currencyList,
        currentCurrency = CurrencyUi.currencyList.first(),
        onConfirm = {},
        onDismiss = {}
    )
}

@Preview
@Composable
fun ListTitleDialogPreview() {
    ListTitleDialog(
        initialListName = "List 1",
        listsOfProducts = emptyList(),
        onConfirm = {},
        onDismiss = {}
    )
}

@Preview
@Composable
fun DeleteListDialogPreview() {
    DeleteListDialog(
        listTitle = "List 1",
        onConfirm = {},
        onDismiss = {}
    )
}
