package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.items

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zedevstuds.price_equalizer_redesign.R
import com.zedevstuds.price_equalizer_redesign.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.getMainUnit
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams.toStringResId
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.models.ProductUiModel
import java.math.BigDecimal
import java.math.RoundingMode

private const val CALCULATED_AMOUNT = 1

@Composable
fun ProductListItem(
    product: ProductUiModel,
    bestPriceProduct: ProductUiModel?,
    currency: String,
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit = {},
    onDeleteClicked:() -> Unit = {},
) {
    val selectedUnit = stringResource(product.selectedMeasureUnit.toStringResId())
    val mainUnit = stringResource(product.selectedMeasureUnit.getMainUnit().toStringResId())
    val enteredParams = "${product.enteredAmount} $selectedUnit - ${product.enteredPrice} $currency"
    val calculatedParams = "$CALCULATED_AMOUNT $mainUnit - ${product.priceForOneUnit} $currency"
    val isBestProduct = product == bestPriceProduct
    val backgroundColor = if (isBestProduct) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.background

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .background(backgroundColor)
            .padding(8.dp)
    ) {
        Row {
            Text(
                text = "${product.index}. ${product.title}",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.remove_item_cont_desc),
                modifier = Modifier.clickable { onDeleteClicked() }
            )
        }
        Row {
            Text(
                text = enteredParams,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge
            )
            if (isBestProduct) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = stringResource(R.string.best_price_cont_desc)
                )
            }
        }
        Row {
            Text(
                text = calculatedParams + product.getPriceDiff(bestPriceProduct, currency),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.remove_item_cont_desc),
                modifier = Modifier.clickable { onEditClicked() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListItemPreview() {
    PriceCalculatorTheme {
        ProductListItem(
            product = getDummyProduct(id = 1),
            bestPriceProduct = getDummyProduct(id = 2),
            currency = "$",
            modifier = Modifier.fillMaxWidth(),
            onEditClicked = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductListItemPreviewDark() {
    PriceCalculatorTheme {
        ProductListItem(
            product = getDummyProduct(id = 1),
            bestPriceProduct = getDummyProduct(id = 2),
            currency = "$",
            modifier = Modifier.fillMaxWidth(),
            onEditClicked = {}
        )
    }
}

private fun ProductUiModel.getPriceDiff(bestPriceProduct: ProductUiModel?, currency: String): String {
    return if (bestPriceProduct != null && this != bestPriceProduct) {
        val diff = BigDecimal(priceForOneUnit - bestPriceProduct.priceForOneUnit)
            .setScale(2, RoundingMode.HALF_UP)
        " (+${diff} $currency)"
    } else ""
}


private fun getDummyProduct(id: Int = 1): ProductUiModel {
    return ProductUiModel(
        index = id,
        id = id,
        enteredAmount = "50",
        enteredPrice = "25$",
        selectedMeasureUnit = MeasureUnit.KG,
        priceForOneUnit = 52.0,
        title = "Product 1"
    )
}
