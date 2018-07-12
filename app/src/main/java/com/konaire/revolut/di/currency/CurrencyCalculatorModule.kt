package com.konaire.revolut.di.currency

import com.konaire.revolut.interactors.currency.CurrencyCalculatorInteractor
import com.konaire.revolut.interactors.currency.CurrencyCalculatorInteractorImpl
import com.konaire.revolut.presenters.currency.CurrencyCalculatorPresenter
import com.konaire.revolut.presenters.currency.CurrencyCalculatorPresenterImpl
import com.konaire.revolut.ui.currency.CurrencyCalculatorFragment
import com.konaire.revolut.ui.currency.CurrencyCalculatorView

import dagger.Binds
import dagger.Module

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
interface CurrencyCalculatorModule {
    @Binds fun provideCurrencyCalculatorInteractor(interactor: CurrencyCalculatorInteractorImpl): CurrencyCalculatorInteractor

    @Binds fun provideCurrencyCalculatorPresenter(presenter: CurrencyCalculatorPresenterImpl): CurrencyCalculatorPresenter

    @Binds fun provideCurrencyCalculatorView(fragment: CurrencyCalculatorFragment): CurrencyCalculatorView
}