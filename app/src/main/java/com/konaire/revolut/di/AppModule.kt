package com.konaire.revolut.di

import android.content.Context

import com.konaire.revolut.App

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
}