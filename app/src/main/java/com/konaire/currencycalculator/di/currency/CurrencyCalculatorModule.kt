package com.konaire.currencycalculator.di.currency

import com.konaire.currencycalculator.interactors.currency.CurrencyCalculatorInteractor
import com.konaire.currencycalculator.interactors.currency.CurrencyCalculatorInteractorImpl
import com.konaire.currencycalculator.presenters.currency.CurrencyCalculatorPresenter
import com.konaire.currencycalculator.presenters.currency.CurrencyCalculatorPresenterImpl
import com.konaire.currencycalculator.ui.currency.CurrencyCalculatorFragment
import com.konaire.currencycalculator.ui.currency.CurrencyCalculatorView

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