package com.konaire.currencycalculator.di

import android.content.Context

import com.konaire.currencycalculator.MockApp
import com.konaire.currencycalculator.network.Api
import com.konaire.currencycalculator.util.Config
import com.konaire.currencycalculator.util.PreferenceManager

import dagger.Module
import dagger.Provides

import org.mockito.Mockito

import javax.inject.Singleton

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
@Module
class MockAppModule {
    @Singleton
    @Provides
    fun provideContext(app: MockApp): Context = app.applicationContext

    @Singleton
    @Provides
    fun providePreferenceManager(): PreferenceManager = Mockito.mock(PreferenceManager::class.java)

    @Singleton
    @Provides
    fun provideConfig(): Config = Config()

    @Singleton
    @Provides
    fun provideApi(): Api = Mockito.mock(Api::class.java)
}