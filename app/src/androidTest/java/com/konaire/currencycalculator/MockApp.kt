package com.konaire.currencycalculator

import android.app.Activity
import android.app.Application

import com.konaire.currencycalculator.di.DaggerMockAppComponent

import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
class MockApp: Application(), HasActivityInjector {
    @Inject lateinit var injector: DispatchingAndroidInjector<Activity>

    lateinit var component: AndroidInjector<MockApp>

    override fun onCreate() {
        super.onCreate()
        component = DaggerMockAppComponent.builder().create(this)
        component.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = injector
}