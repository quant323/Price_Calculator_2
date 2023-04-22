package com.zedevstuds.price_equalizer.price_calculation.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.zedevstuds.price_equalizer.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer.price_calculation.domain.models.MeasureUnit
import com.zedevstuds.price_equalizer.price_calculation.domain.models.ProductModel
import com.zedevstuds.price_equalizer.price_calculation.domain.models.getMainUnit
import java.math.BigDecimal
import java.math.RoundingMode

private const val CALCULATED_AMOUNT = 1

@Composable
fun ProductListItem(
    product: ProductModel,
    bestPriceProduct: ProductModel?,
    currency: String,
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit = {},
    onDeleteClicked:() -> Unit = {},
) {
    val selectedUnit = stringResource(product.selectedMeasureUnit.toStringResId())
    val mainUnit = stringResource(product.selectedMeasureUnit.getMainUnit().toStringResId())
    val enteredParams = "${product.enteredAmount} $selectedUnit - ${product.enteredPrice} $currency"
    val calculatedParams = "$CALCULATED_AMOUNT $mainUnit - ${product.priceForOneUnit} $currency"

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(8.dp)
    ) {
        Row {
            Text(
                text = product.title,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEditClicked() },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Edit",
                modifier = Modifier.clickable { onDeleteClicked() }
            )
        }
        Row {
            Text(
                text = enteredParams,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge
            )
            if (product == bestPriceProduct) {
                Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "Best Price!")
            }
        }
        Text(
            text = calculatedParams + product.getPriceDiff(bestPriceProduct, currency),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
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

private fun ProductModel.getPriceDiff(bestPriceProduct: ProductModel?, currency: String): String {
    return if (bestPriceProduct != null && this != bestPriceProduct) {
        val diff = BigDecimal(priceForOneUnit - bestPriceProduct.priceForOneUnit)
            .setScale(2, RoundingMode.HALF_UP)
        " (+${diff} $currency)"
    } else ""
}


private fun getDummyProduct(id: Int = 1): ProductModel {
    return ProductModel(
        id = id,
        enteredAmount = "50",
        enteredPrice = "25$",
        selectedMeasureUnit = MeasureUnit.KG,
        priceForOneUnit = 52.0,
        title = "Product 1"
    )
}
