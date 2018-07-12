package com.konaire.revolut.di.currency

import com.konaire.revolut.di.scopes.ActivityScope
import com.konaire.revolut.util.Navigation

import dagger.Module
import dagger.Provides

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
class CurrencyActivityModule {
    @ActivityScope
    @Provides
    fun provideNavigation(): Navigation = Navigation()
}