package com.konaire.currencycalculator.di.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import com.konaire.currencycalculator.BuildConfig
import com.konaire.currencycalculator.models.CurrencyResponse
import com.konaire.currencycalculator.network.Api
import com.konaire.currencycalculator.network.deserializers.CurrencyResponseDeserializer
import com.konaire.currencycalculator.util.Constants

import dagger.Module
import dagger.Provides

import io.reactivex.schedulers.Schedulers

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)

        val loggingLevel = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = loggingLevel

        builder.addInterceptor(interceptor)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(CurrencyResponse::class.java, CurrencyResponseDeserializer())

        return builder.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        val builder = Retrofit.Builder().client(client)
        builder.addConverterFactory(GsonConverterFactory.create(gson))
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        builder.baseUrl(Constants.BASE_URL)

        return builder.build()
    }

    @Provides
    fun provideApi(
        retrofit: Retrofit
    ): Api {
        return retrofit.create(Api::class.java)
    }
}