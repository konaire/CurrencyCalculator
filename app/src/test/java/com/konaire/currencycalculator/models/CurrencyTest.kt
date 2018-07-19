package com.konaire.currencycalculator.models

import com.konaire.currencycalculator.R

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Evgeny Eliseyev on 15/07/2018.
 */
class CurrencyTest {
    @Test
    fun doesKnownCurrencyProduceCorrectInfo() {
        val currency = Currency("EUR")
        val descriptionRes = currency.currencyInfo.descriptionRes
        assertEquals(R.string.currency_description_eur, descriptionRes)
    }

    @Test
    fun doesUnknownCurrencyProduceDefaultInfo() {
        val currency = Currency("ABC")
        val descriptionRes = currency.currencyInfo.descriptionRes
        assertEquals(R.string.currency_description_unknown, descriptionRes)
    }
}