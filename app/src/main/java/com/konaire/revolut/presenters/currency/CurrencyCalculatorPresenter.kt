package com.konaire.revolut.presenters.currency

import com.konaire.revolut.R
import com.konaire.revolut.interactors.currency.CurrencyCalculatorInteractor
import com.konaire.revolut.presenters.BasePresenter
import com.konaire.revolut.ui.currency.CurrencyCalculatorView

import io.reactivex.disposables.Disposable

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
abstract class CurrencyCalculatorPresenter: BasePresenter() {
    abstract fun getLatestCurrencyRates(displayProgress: Boolean = true)
    abstract fun stopUpdatingCurrencyRates()

    abstract fun setLastBaseCurrency(currency: String)
}

class CurrencyCalculatorPresenterImpl @Inject constructor(
    private val interactor: CurrencyCalculatorInteractor,
    private val view: CurrencyCalculatorView
): CurrencyCalculatorPresenter() {
    private var getLatestCurrencyRatesSubscription: Disposable? = null

    override fun getLatestCurrencyRates(displayProgress: Boolean) {
        if (displayProgress) {
            view.showProgress()
        }

        getLatestCurrencyRatesSubscription?.dispose()
        getLatestCurrencyRatesSubscription = interactor.getLatestCurrencyRates()
            .doFinally { view.hideProgress() }
            .subscribe(
                { response ->
                    val data = ArrayList(response.currencies)
                    data.add(0, response.base)
                    view.showData(data)
                    view.hideProgress()
                }, { view.showMessage(R.string.network_error) }
            )

        disposables.add(getLatestCurrencyRatesSubscription!!)
    }

    override fun stopUpdatingCurrencyRates() {
        getLatestCurrencyRatesSubscription?.dispose()
    }

    override fun setLastBaseCurrency(currency: String) {
        interactor.setLastBaseCurrency(currency)
        getLatestCurrencyRates(false)
    }
}