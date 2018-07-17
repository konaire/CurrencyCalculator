package com.konaire.revolut.interactors.currency

import com.konaire.revolut.models.CurrencyResponse
import com.konaire.revolut.network.Api

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

import java.util.Collections
import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface CurrencyCalculatorInteractor {
    fun getLatestCurrencyRates(
        base: String, uiScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Single<CurrencyResponse>
}

class CurrencyCalculatorInteractorImpl @Inject constructor(
    private val api: Api
): CurrencyCalculatorInteractor {
    override fun getLatestCurrencyRates(base: String, uiScheduler: Scheduler): Single<CurrencyResponse> =
        api.getLatestCurrencyRates(base).map { response ->
            Collections.sort(response.currencies, compareBy { it.name })
            response
        }.observeOn(uiScheduler)
}