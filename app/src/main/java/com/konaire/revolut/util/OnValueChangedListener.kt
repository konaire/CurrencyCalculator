package com.konaire.revolut.util

/**
 * Created by Evgeny Eliseyev on 17/07/2018.
 */
interface OnValueChangedListener<T> {
    fun onValueChanged(value: T)
}