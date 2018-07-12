package com.konaire.revolut.ui

import com.konaire.revolut.ui.list.OnItemClickedListener
import com.konaire.revolut.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
interface BaseView {
    fun showMessage(message: String)
    fun showMessage(messageResource: Int)
}

interface BaseLoadingView: BaseView {
    fun showProgress()
    fun hideProgress()
}

interface BaseListView<T>: BaseLoadingView, OnItemClickedListener<T> where T: ViewType {
    fun showData(data: MutableList<T>)
}