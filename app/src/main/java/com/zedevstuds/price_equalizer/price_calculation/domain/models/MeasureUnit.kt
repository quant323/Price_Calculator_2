package com.zedevstuds.price_equalizer.price_calculation.domain.models

private const val MILLI = 0.001
private const val NO_MULTIPLIER = 1.0

enum class MeasureUnit(val id: Int, val multiplier: Double = NO_MULTIPLIER) {
    KG(id = 0),
    G(id = 1, multiplier = MILLI),
    L(id = 2),
    ML(id = 3, multiplier = MILLI),
    M(id = 4),
    MM(id = 5, multiplier = MILLI),
    PCS(id = 6)
}

val listOfUnits = listOf(
    MeasureUnit.KG,
    MeasureUnit.G,
    MeasureUnit.L,
    MeasureUnit.ML,
    MeasureUnit.M,
    MeasureUnit.MM,
    MeasureUnit.PCS,
)

fun MeasureUnit.getMainUnit(): MeasureUnit {
    return when(this) {
        MeasureUnit.KG, MeasureUnit.G -> MeasureUnit.KG
        MeasureUnit.L, MeasureUnit.ML -> MeasureUnit.L
        MeasureUnit.M, MeasureUnit.MM -> MeasureUnit.M
        MeasureUnit.PCS -> MeasureUnit.PCS
    }
}
