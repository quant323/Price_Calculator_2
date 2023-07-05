package com.zedevstuds.price_equalizer.price_calculation.ui.enterparams

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer.R
import com.zedevstuds.price_equalizer.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer.price_calculation.domain.models.listOfUnits
import com.zedevstuds.price_equalizer.price_calculation.ui.mainscreen.MainScreenViewModel

@Composable
fun EnterParamsArea(
    viewModel: EnterParamsViewModel,
    modifier: Modifier = Modifier,
) {
    EnterParamsAreaContent(
        viewState = viewModel.enterParamsViewState.value,
        onTitleChanged = {
            if (it.length <= MainScreenViewModel.MAX_TITLE_LENGTH) {
                viewModel.onTitleChanged(it)
            }
        },
        onAmountChanged = {
            viewModel.onAmountChanged(it)
        },
        onPriceChanged = {
            viewModel.onPriceChanged(it)
        },
        onOkClicked = {
            viewModel.onOkClicked()
        },
        onClearClicked = {
            viewModel.onCleanClicked()
        },
        onUnitSelected = { unit ->
            viewModel.onMeasureUnitSelected(unit)
        },
        modifier = modifier
    )
}

@Composable
fun EnterParamsAreaContent(
    viewState: EnterParamsViewModel.EnterParamsViewState,
    onTitleChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onPriceChanged: (String) -> Unit,
    onOkClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onUnitSelected: (MeasureUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHidden by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        EnterChipsSection(
            title = viewState.title,
            isHidden = isHidden,
            selectedUnit = viewState.selectedUnit,
            unitList = viewState.listOfUnits,
            onTitleChanged = onTitleChanged,
            onOk = {
                onOkClicked()
                if (!isHidden) {
                    focusRequester.requestFocus()
                }
            },
            onClear = onClearClicked,
            onHide = { isHidden = !isHidden },
            onUnitSelected = onUnitSelected
        )
        if (!isHidden) {
            Row {
                EnterField(
                    text = viewState.customAmount,
                    hint = "Custom Amount",
                    modifier = Modifier.weight(1f),
                    trailingText = stringResource(id = viewState.mainUnit.toStringResId()),
                    readOnly = true,
                )
                HorizontalSpacer()
                EnterField(
                    text = viewState.priceForCustomAmount,
                    hint = "Custom Price",
                    modifier = Modifier.weight(1f),
                    trailingText = viewState.currency.sign,
                    readOnly = true,
                )
            }
            Row {
                EnterField(
                    text = viewState.enteredAmount,
                    hint = "Amount",
                    onTextChanged = onAmountChanged,
                    modifier = Modifier
                        .weight(1.0f)
                        .focusRequester(focusRequester),
                    trailingText = stringResource(id = viewState.selectedUnit.toStringResId()),
                    onDone = {
                        onOkClicked()
                        focusRequester.requestFocus()
                    }
                )
                HorizontalSpacer()
                EnterField(
                    text = viewState.enteredPrice,
                    hint = "Price",
                    onTextChanged = onPriceChanged,
                    modifier = Modifier.weight(1.0f),
                    trailingText = viewState.currency.sign,
                    onDone = {
                        onOkClicked()
                        focusRequester.requestFocus()
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterChipsSection(
    title: String,
    isHidden: Boolean,
    selectedUnit: MeasureUnit,
    unitList: List<MeasureUnit>,
    onTitleChanged: (String) -> Unit,
    onOk: () -> Unit,
    onClear: () -> Unit,
    onHide: () -> Unit,
    onUnitSelected: (MeasureUnit) -> Unit
) {
    var isUnitsVisible by remember { mutableStateOf(false) }

    if (isUnitsVisible) {
        // TODO issue with paddings
        LazyRow {
            items(unitList) {
                InputChip(
                    selected = selectedUnit == it,
                    onClick = {
                        onUnitSelected(it)
                        isUnitsVisible = false
                    },
                    label = {
                        Text(
                            text = stringResource(id = it.toStringResId()),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                )
            }
        }
    }
    Row {
        Row(modifier = Modifier.weight(1f)) {
            InputChip(
                selected = false,
                modifier = Modifier.padding(end = 4.dp),
                onClick = { isUnitsVisible = !isUnitsVisible },
                label = {
                    Text(
                        text = stringResource(id = selectedUnit.toStringResId()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
            InputChip(
                selected = false,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    BasicTextField(
                        value = title,
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        onValueChange = onTitleChanged,
                    )
                }
            )
        }
        HorizontalSpacer()
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1f)) {
            InputChip(
                selected = false,
                onClick = onClear,
                label = {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Clear")
                }
            )
            InputChip(
                selected = false,
                onClick = onHide,
                label = {
                    Icon(
                        imageVector = if (isHidden) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Hide Icon"
                    )
                }
            )
            InputChip(
                selected = false,
                onClick = onOk,
                label = { Text(text = "OK", style = MaterialTheme.typography.titleMedium) }
            )
        }
    }
}

@Composable
fun EnterField(
    text: String,
    hint: String,
    trailingText: String,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit = {},
    readOnly: Boolean = false,
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        label = { Text(hint) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
        trailingIcon = { Text(text = trailingText) },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        readOnly = readOnly,
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        )
    )
}

@Composable
fun HorizontalSpacer() {
    Spacer(modifier = Modifier.width(8.dp))
}

fun MeasureUnit.toStringResId(): Int {
    return when(this) {
        MeasureUnit.KG -> R.string.kilogram
        MeasureUnit.G -> R.string.gram
        MeasureUnit.L -> R.string.liter
        MeasureUnit.ML -> R.string.milliliter
        MeasureUnit.M -> R.string.meter
        MeasureUnit.MM -> R.string.millimeter
        MeasureUnit.PCS -> R.string.pics
    }
}

@Preview(showBackground = true)
@Composable
fun EnterParamsAreaPreview() {
    PriceCalculatorTheme {
        EnterParamsAreaContent(
            defaultEnterParamsViewState,
            {},
            {},
            {},
            {},
            {},
            {},
        )
    }
}

private val defaultEnterParamsViewState = EnterParamsViewModel.EnterParamsViewState(
    enteredAmount = "5",
    enteredPrice = "10",
    customAmount = "1",
    priceForCustomAmount = "50",
    selectedUnit = MeasureUnit.KG,
    mainUnit = MeasureUnit.KG,
    title = "Some Product",
    currency = CurrencyUi.currencyList.first(),
    listOfUnits = listOfUnits
)