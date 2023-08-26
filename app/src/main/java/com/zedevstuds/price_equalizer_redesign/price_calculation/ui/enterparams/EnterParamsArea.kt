package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.listOfUnits
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items.ProductTitleDialog

@Composable
fun EnterParamsArea(
    viewModel: EnterParamsViewModel,
    modifier: Modifier = Modifier,
) {
    var showEditTitleDialog by remember { mutableStateOf(false) }

    EnterParamsAreaContent(
        viewState = viewModel.enterParamsViewState.value,
        onTitleClicked = {
            showEditTitleDialog = true
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

    if (showEditTitleDialog) {
        ProductTitleDialog(
            currentTitle = viewModel.enterParamsViewState.value.title,
            onConfirm = { title ->
                viewModel.onTitleChanged(title)
                showEditTitleDialog = false
            },
            onDismiss = { showEditTitleDialog = false }
        )
    }
}

@Composable
fun EnterParamsAreaContent(
    viewState: EnterParamsViewModel.EnterParamsViewState,
    onTitleClicked: () -> Unit,
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
        Spacer(modifier = Modifier.height(6.dp))
        EnterChipsSection(
            title = viewState.title,
            isHidden = isHidden,
            selectedUnit = viewState.selectedUnit,
            unitList = viewState.listOfUnits,
            onTitleClicked = onTitleClicked,
            onOkClicked = {
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
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                EnterField(
                    text = viewState.customAmount,
                    hint = stringResource(R.string.calc_amount_hint),
                    modifier = Modifier.weight(1f),
                    trailingText = stringResource(id = viewState.mainUnit.toStringResId()),
                    readOnly = true,
                )
                EqualsIcon()
                EnterField(
                    text = viewState.priceForCustomAmount,
                    hint = stringResource(R.string.calc_price_hint),
                    modifier = Modifier.weight(1f),
                    trailingText = viewState.currency.sign,
                    readOnly = true,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                EnterField(
                    text = viewState.enteredAmount,
                    hint = stringResource(R.string.amount_hint),
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
                EqualsIcon()
                EnterField(
                    text = viewState.enteredPrice,
                    hint = stringResource(R.string.price_hint),
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

@Composable
fun EnterChipsSection(
    title: String,
    isHidden: Boolean,
    selectedUnit: MeasureUnit,
    unitList: List<MeasureUnit>,
    onTitleClicked: () -> Unit,
    onOkClicked: () -> Unit,
    onClear: () -> Unit,
    onHide: () -> Unit,
    onUnitSelected: (MeasureUnit) -> Unit
) {
    var isUnitsVisible by remember { mutableStateOf(false) }

    if (isUnitsVisible) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(unitList) {
                CustomChip(
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
        Spacer(modifier = Modifier.height(10.dp))
    }
    Row {
        Row(modifier = Modifier.weight(1f)) {
            CustomChip(
                onClick = { isUnitsVisible = !isUnitsVisible },
                label = {
                    Text(
                        text = stringResource(id = selectedUnit.toStringResId()),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
            VerticalSpacer()
            CustomChip(
                onClick = onTitleClicked,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = title.ifEmpty { stringResource(R.string.default_product_title) },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
        VerticalSpacer()
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1f)) {
            CustomChip(
                onClick = onClear,
                label = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.clear_list_cont_desc)
                    )
                },
                modifier = Modifier.weight(1f)
            )
            VerticalSpacer()
            CustomChip(
                onClick = onHide,
                label = {
                    Icon(
                        imageVector = if (isHidden) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.hide_params_cont_desc)
                    )
                },
                modifier = Modifier.weight(1f)
            )
            VerticalSpacer()
            CustomChip(
                onClick = onOkClicked,
                label = {
                    Text(
                        text = stringResource(R.string.ok_dialog_button_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                modifier = Modifier.weight(1f)
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
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit = {},
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        label = { Text(hint) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        trailingIcon = { Text(text = trailingText) },
        leadingIcon = null,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        readOnly = readOnly,
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun CustomChip(
    label: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    val containerSize = 36.dp
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (selected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            )
            .sizeIn(minWidth = containerSize, minHeight = containerSize)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        label()
    }
}

@Composable
fun EqualsIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_keyboard_double_arrow_right_24),
        contentDescription = null,
    )
}

@Composable
fun VerticalSpacer() {
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
            testEnterParamsViewState,
            {},
            {},
            {},
            {},
            {},
            {},
        )
    }
}

private val testEnterParamsViewState = EnterParamsViewModel.EnterParamsViewState(
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