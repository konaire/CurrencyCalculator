package com.konaire.revolut.ui.currency

import android.os.Bundle
import android.view.MenuItem

import com.konaire.revolut.R
import com.konaire.revolut.ui.BaseFragment
import com.konaire.revolut.util.Navigation

import dagger.android.support.DaggerAppCompatActivity

import kotlinx.android.synthetic.main.activity_currency.*

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
class CurrencyActivity: DaggerAppCompatActivity() {
    @Inject lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            navigation.showFragment(this, CurrencyCalculatorFragment.create(), true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigation.closeFragment(this)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as BaseFragment
        if (fragment.defaultBackButtonBehaviour()) {
            super.onBackPressed()
        }
    }
}