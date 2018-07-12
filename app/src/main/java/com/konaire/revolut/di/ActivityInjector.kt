package com.konaire.revolut.di

import com.konaire.revolut.di.currency.CurrencyActivityModule
import com.konaire.revolut.di.currency.CurrencyFragmentInjector
import com.konaire.revolut.di.scopes.ActivityScope
import com.konaire.revolut.ui.currency.CurrencyActivity

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