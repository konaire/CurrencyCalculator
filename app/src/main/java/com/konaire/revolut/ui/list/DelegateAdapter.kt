package com.konaire.revolut.ui.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface DelegateAdapter<in T> where T: ViewType {
    fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder?, item: T)
}