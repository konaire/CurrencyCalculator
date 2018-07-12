package com.konaire.revolut.di.currency

import com.konaire.revolut.ui.currency.CurrencyCalculatorFragment

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