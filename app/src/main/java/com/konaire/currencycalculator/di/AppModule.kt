package com.konaire.currencycalculator.di

import android.content.Context

import com.konaire.currencycalculator.App
import com.konaire.currencycalculator.util.Config
import com.konaire.currencycalculator.util.PreferenceManager

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Singleton
    @Provides
    fun providePreferenceManager(context: Context): PreferenceManager = PreferenceManager(context)

    @Singleton
    @Provides
    fun provideConfig(): Config = Config()
}