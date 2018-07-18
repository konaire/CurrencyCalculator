package com.konaire.revolut.interactors.currency

import com.konaire.revolut.models.CurrencyResponse
import com.konaire.revolut.network.Api
import com.konaire.revolut.util.Config

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
        base: String, uiScheduler: Scheduler = AndroidSchedulers.mainThread()
    ): Flowable<CurrencyResponse>
}

class CurrencyCalculatorInteractorImpl @Inject constructor(
    private val api: Api,
    private val config: Config
): CurrencyCalculatorInteractor {
    override fun getLatestCurrencyRates(base: String, uiScheduler: Scheduler): Flowable<CurrencyResponse> {
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
}