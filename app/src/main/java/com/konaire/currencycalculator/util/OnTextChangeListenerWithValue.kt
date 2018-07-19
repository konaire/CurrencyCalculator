package com.konaire.currencycalculator.util

import android.text.TextWatcher
import android.view.View

/**
 * Created by Evgeny Eliseyev on 17/07/2018.
 */
interface OnTextChangeListenerWithValue<T>: View.OnFocusChangeListener, TextWatcher {
    var value: T?
}