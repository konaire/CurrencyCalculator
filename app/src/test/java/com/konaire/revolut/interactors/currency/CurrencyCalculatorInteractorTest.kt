package com.konaire.revolut.interactors.currency

import com.konaire.revolut.models.CurrencyResponse
import com.konaire.revolut.network.Api

import io.reactivex.Single
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
 * Created by Evgeny Eliseyev on 26/04/2018.
 */
@RunWith(MockitoJUnitRunner.Silent::class)
class CurrencyCalculatorInteractorTest {
    @Mock private lateinit var api: Api

    @InjectMocks private lateinit var scheduler: TestScheduler
    @InjectMocks private lateinit var interactor: CurrencyCalculatorInteractorImpl

    @Test
    fun isRequestShowsEmptyList() {
        mockNetwork(CurrencyResponse("", ArrayList()))

        val subscriber = TestSubscriber.create<CurrencyResponse>()
        interactor.getLatestCurrencyRates("", scheduler).toFlowable().subscribe(subscriber)
        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        scheduler.triggerActions()

        subscriber.assertValue { response -> response.rates.isEmpty() }
    }

    private fun mockNetwork(response: CurrencyResponse) {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Single.just(response))
    }
}