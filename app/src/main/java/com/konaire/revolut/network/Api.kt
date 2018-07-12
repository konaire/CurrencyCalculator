package com.konaire.revolut.network

import com.konaire.revolut.models.CurrencyResponse

import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface Api {
    @GET("latest")
    fun getLatestCurrencyRates(
        @Query("base") base: String
    ): Single<CurrencyResponse>
}