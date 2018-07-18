package com.konaire.revolut.di

import android.content.Context

import com.konaire.revolut.App
import com.konaire.revolut.util.Config
import com.konaire.revolut.util.PreferenceManager

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