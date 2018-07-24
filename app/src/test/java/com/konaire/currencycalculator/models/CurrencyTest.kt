package com.konaire.currencycalculator.models

import android.content.Context

import com.konaire.currencycalculator.R

import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Evgeny Eliseyev on 15/07/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class CurrencyTest {
    @Mock private lateinit var context: Context

    @Test
    fun doesKnownCurrencyProduceCorrectInfo() {
        mockGetString()
        val currency = Currency("EUR")
        currency.getDescription(context)
        verify(context).getString(R.string.currency_description_eur)
    }

    @Test
    fun doesUnknownCurrencyProduceDefaultInfo() {
        mockGetString()
        val currency = Currency("ABC")
        currency.getDescription(context)
        verify(context).getString(R.string.currency_description_unknown)
    }

    private fun mockGetString() {
        `when`(context.getString(anyInt())).thenReturn("")
    }
}