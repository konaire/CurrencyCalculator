package com.konaire.currencycalculator.util

import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity

import com.konaire.currencycalculator.R
import com.konaire.currencycalculator.ui.BaseFragment

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 * Do not use this class directly. It should be created via DI.
 */
class Navigation {
    fun showFragment(activity: AppCompatActivity?, fragment: BaseFragment, notAddToBackStack: Boolean = false) {
        if (activity == null) {
            return
        }

        val manager = activity.supportFragmentManager
        val transaction = manager.beginTransaction()
        if (fragment.isRoot()) {
            val previousFragment = manager.findFragmentByTag(fragment.getFragmentTag())

            if (previousFragment == null || !previousFragment.isVisible) {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                transaction.replace(R.id.mainContainer, fragment, fragment.getFragmentTag()).commit()
            }
        } else {
            val previousFragment = manager.findFragmentById(R.id.mainContainer)

            if (previousFragment is BaseFragment &&
                previousFragment.getFragmentTag().isNotEmpty() &&
                previousFragment.getFragmentTag() == fragment.getFragmentTag()) {

                return
            }

            if (notAddToBackStack) {
                transaction.replace(R.id.mainContainer, fragment, fragment.getFragmentTag()).commit()
            } else {
                transaction.replace(R.id.mainContainer, fragment, fragment.getFragmentTag()).addToBackStack(null).commit()
            }
        }
    }

    fun closeFragment(activity: AppCompatActivity?) {
        if (activity == null) {
            return
        }

        val fragment = activity.supportFragmentManager.findFragmentById(R.id.mainContainer)

        if (fragment is BaseFragment) {
            if (fragment.defaultBackArrowBehaviour()) {
                if (fragment.isRoot()) {
                    activity.onBackPressed()
                } else {
                    activity.supportFragmentManager.popBackStack()
                }
            }
        } else {
            activity.onBackPressed()
        }
    }
}