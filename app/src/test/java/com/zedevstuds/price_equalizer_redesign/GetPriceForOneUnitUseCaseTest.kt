package com.zedevstuds.price_equalizer_redesign

import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.usecases.product.GetPriceForOneUnitUseCase
import com.zedevstuds.price_equalizer_redesign.price_calculation.domain.models.MeasureUnit
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetPriceForOneUnitUseCaseTest {

    private lateinit var useCase: GetPriceForOneUnitUseCase

    @Before
    fun before() {
        useCase = GetPriceForOneUnitUseCase()
    }

    @Test
    fun `GIVEN WHEN THEN`() {
        val amount = "2"
        val price = "3"
        val result = useCase.execute(amount, price, MeasureUnit.KG)
        Assert.assertEquals(1.5, result, 0.01)
    }

    @Test
    fun `GIVEN WHEN THEN2`() {
        val amount = "2"
        val price = "3"
        val result = useCase.execute(amount, price, MeasureUnit.G)
        Assert.assertEquals(1500.0, result, 0.01)
    }
}