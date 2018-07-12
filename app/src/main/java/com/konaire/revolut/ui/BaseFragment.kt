package com.konaire.revolut.ui

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import com.konaire.revolut.util.toast

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
abstract class BaseFragment: Fragment(), BaseView {
    override fun onResume() {
        super.onResume()
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getTitle()
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(!isRoot())
    }

    override fun showMessage(message: String) {
        activity?.toast(message)
    }

    override fun showMessage(messageResource: Int) {
        activity?.toast(getString(messageResource))
    }

    abstract fun getTitle(): String

    abstract fun getFragmentTag(): String

    open fun isRoot(): Boolean = false

    open fun defaultBackArrowBehaviour(): Boolean = true

    open fun defaultBackButtonBehaviour(): Boolean = true
}