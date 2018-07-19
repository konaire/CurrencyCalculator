package com.konaire.currencycalculator.di.currency

import com.konaire.currencycalculator.ui.currency.CurrencyCalculatorFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Evgeny Eliseyev on 25/04/2018.
 */
@Module
interface CurrencyFragmentInjector {
    @ContributesAndroidInjector(modules = [CurrencyCalculatorModule::class])
    fun provideCurrencyCalculatorFragment(): CurrencyCalculatorFragment
}