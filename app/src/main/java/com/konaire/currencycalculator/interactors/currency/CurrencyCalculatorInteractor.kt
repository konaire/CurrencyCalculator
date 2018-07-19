package com.konaire.currencycalculator.interactors.currency

import com.konaire.currencycalculator.models.CurrencyResponse
import com.konaire.currencycalculator.network.Api
import com.konaire.currencycalculator.util.Config
import com.konaire.currencycalculator.util.Constants
import com.konaire.currencycalculator.util.PreferenceManager

import io.reactivex.Scheduler
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers

import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface CurrencyCalculatorInteractor {
    fun getLatestCurrencyRates(
        uiScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Flowable<CurrencyResponse>

    fun setLastBaseCurrency(currency: String)
}

class CurrencyCalculatorInteractorImpl @Inject constructor(
    private val api: Api,
    private val config: Config,
    private val preferenceManager: PreferenceManager
): CurrencyCalculatorInteractor {
    override fun getLatestCurrencyRates(uiScheduler: Scheduler): Flowable<CurrencyResponse> {
        val base = preferenceManager.getString(Constants.PREFERENCE_LAST_BASE_CURRENCY, "EUR")
        var flowable = api.getLatestCurrencyRates(base)
        if (config.useAutoUpdate) {
            flowable =
                flowable
                    .retryWhen { data -> data.delay(1, TimeUnit.SECONDS) }
                    .repeatWhen { data -> data.delay(1, TimeUnit.SECONDS) }
        }

        return flowable.map { response ->
            Collections.sort(response.currencies, compareBy { it.name })
            response
        }.observeOn(uiScheduler)
    }

    override fun setLastBaseCurrency(currency: String) =
        preferenceManager.setString(Constants.PREFERENCE_LAST_BASE_CURRENCY, currency)
}