package com.konaire.currencycalculator.ui.list

import android.view.View

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface OnItemClickedListener<in T> where T: ViewType {
    fun onItemClicked(item: T, view: View?)
}