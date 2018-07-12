package com.konaire.revolut

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
class MockRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, MockApp::class.java.name, context)
    }
}