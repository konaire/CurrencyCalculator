package com.konaire.revolut

import android.app.Activity
import android.app.Application

import com.konaire.revolut.di.DaggerAppComponent

import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
class App: Application(), HasActivityInjector {
    @Inject lateinit var injector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().create(this).inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = injector
}