package com.konaire.revolut.interactors.currency

import com.konaire.revolut.models.Currency
import com.konaire.revolut.models.CurrencyResponse
import com.konaire.revolut.network.Api
import com.konaire.revolut.util.Config
import com.konaire.revolut.util.PreferenceManager

import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subscribers.TestSubscriber

import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

import java.util.concurrent.TimeUnit

/**
 * Created by Evgeny Eliseyev on 18/07/2018.
 */
@RunWith(MockitoJUnitRunner.Silent::class)
class CurrencyCalculatorInteractorTest {
    @Mock private lateinit var api: Api
    @Mock private lateinit var config: Config
    @Mock private lateinit var preferenceManager: PreferenceManager

    @InjectMocks private lateinit var scheduler: TestScheduler
    @InjectMocks private lateinit var interactor: CurrencyCalculatorInteractorImpl

    @Test
    fun isCurrenciesSortedCorrectlyAfterLoading() {
        val currencies = listOf(
            Currency("A"), Currency("Z"), Currency("B"), Currency("R")
        ).toMutableList()

        mockPreferences()
        mockNetwork(CurrencyResponse(Currency(), currencies))
        val subscriber = TestSubscriber.create<CurrencyResponse>()
        interactor.getLatestCurrencyRates(scheduler).subscribe(subscriber)
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        scheduler.triggerActions()

        subscriber.assertValue { response ->
            response.currencies.size == 4 &&
            response.currencies[0].name == "A" &&
            response.currencies[1].name == "B" &&
            response.currencies[2].name == "R" &&
            response.currencies[3].name == "Z"
        }
    }

    private fun mockPreferences() {
        `when`(preferenceManager.getString(anyString(), anyString())).thenReturn("")
    }

    private fun mockNetwork(response: CurrencyResponse) {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Flowable.just(response))
    }
}