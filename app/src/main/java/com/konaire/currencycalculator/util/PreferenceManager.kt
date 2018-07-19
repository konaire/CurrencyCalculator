package com.konaire.currencycalculator.util

import android.content.Context

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 18/07/2018.
 */
open class PreferenceManager @Inject constructor(
    private val context: Context
) {
    open fun getString(id: String, defaultValue: String = ""): String {
        val preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, 0)
        return preferences.getString(id, defaultValue)
    }

    open fun setString(id: String, value: String) {
        val preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, 0)
        val editor = preferences.edit()
        editor.putString(id, value)
        editor.apply()
    }
}