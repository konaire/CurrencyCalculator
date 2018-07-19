package com.konaire.currencycalculator.network

import com.konaire.currencycalculator.models.CurrencyResponse

import io.reactivex.Flowable

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface Api {
    @GET("latest")
    fun getLatestCurrencyRates(
        @Query("base") base: String
    ): Flowable<CurrencyResponse>
}