package com.zedevstuds.price_equalizer.price_calculation.domain.usecases.product

import com.zedevstuds.price_equalizer.price_calculation.domain.models.MeasureUnit
import java.math.BigDecimal
import java.math.RoundingMode

class GetPriceForOneUnitUseCase {

    fun execute(amount: String, price: String, measureUnit: MeasureUnit): Double {
        val result = price.toDouble() / (amount.toDouble() * measureUnit.multiplier)
        return BigDecimal(result)
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}
