package com.konaire.revolut.presenters.currency

import com.konaire.revolut.R
import com.konaire.revolut.interactors.currency.CurrencyCalculatorInteractor
import com.konaire.revolut.presenters.BasePresenter
import com.konaire.revolut.ui.currency.CurrencyCalculatorView

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
abstract class CurrencyCalculatorPresenter: BasePresenter() {
    abstract fun getLatestCurrencyRates(base: String)
}

class CurrencyCalculatorPresenterImpl @Inject constructor(
    private val interactor: CurrencyCalculatorInteractor,
    private val view: CurrencyCalculatorView
): CurrencyCalculatorPresenter() {
    override fun getLatestCurrencyRates(base: String) {
        view.showProgress()
        disposables.add(interactor.getLatestCurrencyRates(base)
            .doFinally { view.hideProgress() }
            .subscribe(
                { response -> view.showData(ArrayList(response.rates)) },
                { view.showMessage(R.string.network_error) }
            )
        )
    }
}