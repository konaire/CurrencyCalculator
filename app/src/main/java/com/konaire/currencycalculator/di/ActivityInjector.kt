package com.konaire.currencycalculator.di

import com.konaire.currencycalculator.di.currency.CurrencyActivityModule
import com.konaire.currencycalculator.di.currency.CurrencyFragmentInjector
import com.konaire.currencycalculator.di.scopes.ActivityScope
import com.konaire.currencycalculator.ui.currency.CurrencyActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Evgeny Eliseyev on 25/04/2018.
 */
@Module
interface ActivityInjector {
    @ActivityScope
    @ContributesAndroidInjector(modules = [CurrencyActivityModule::class, CurrencyFragmentInjector::class])
    fun provideCurrencyActivity(): CurrencyActivity
}