package com.zedevstuds.price_equalizer_redesign.price_calculation.ui.enterparams

data class CurrencyUi(val sign: String, val name: String) {
    companion object {
        private const val DOLLAR_SIGN = "$"
        private const val DOLLAR_NAME = "USD"
        private const val EURO_SIGN = "€"
        private const val EURO_NAME = "EUR"
        private const val RUB_SIGN = "₽"
        private const val RUB_NAME = "RUB"
        private const val UKR_SIGN = "₴"
        private const val UKR_NAME = "UAH"
        private const val INDIAN_SIGN = "₹"
        private const val INDIAN_NAME = "INR"
        private const val BRAZIL_SIGN = "R$"
        private const val BRAZIL_NAME = "BRL"
        private const val KAZAKH_SIGN = "₸"
        private const val KAZAKH_NAME = "KZT"
        private const val MALAY_SIGN = "RM"
        private const val MALAY_NAME = "MYR"
        private const val KOREAN_SIGN = "₩"
        private const val KOREAN_NAME = "KRW"
        private const val PHILIP_SIGN ="₱"
        private const val PHILIP_NAME ="PHP"
        private const val MEXICAN_SIGN = "$"
        private const val MEXICAN_NAME = "MXN"

        val DOLLAR = CurrencyUi(DOLLAR_SIGN, DOLLAR_NAME)
        val EURO = CurrencyUi(EURO_SIGN, EURO_NAME)
        val RUB = CurrencyUi(RUB_SIGN, RUB_NAME)
        val UKR = CurrencyUi(UKR_SIGN, UKR_NAME)
        val INDIAN = CurrencyUi(INDIAN_SIGN, INDIAN_NAME)
        val BRAZIL = CurrencyUi(BRAZIL_SIGN, BRAZIL_NAME)
        val KAZAKH = CurrencyUi(KAZAKH_SIGN, KAZAKH_NAME)
        val MALAY = CurrencyUi(MALAY_SIGN, MALAY_NAME)
        val KOREAN = CurrencyUi(KOREAN_SIGN, KOREAN_NAME)
        val PHILIP = CurrencyUi(PHILIP_SIGN, PHILIP_NAME)
        val MEXICAN = CurrencyUi(MEXICAN_SIGN, MEXICAN_NAME)

        val currencyList = listOf(
            DOLLAR,
            EURO,
            RUB,
            UKR,
            INDIAN,
            BRAZIL,
            KAZAKH,
            MALAY,
            KOREAN,
            PHILIP,
            MEXICAN,
        )
    }
}
