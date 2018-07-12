package com.konaire.revolut.di

import com.konaire.revolut.App
import com.konaire.revolut.di.network.NetworkModule

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

import javax.inject.Singleton

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityInjector::class, NetworkModule::class])
interface AppComponent: AndroidInjector<App> {
    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<App>()
}