package com.konaire.revolut.presenters

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
abstract class BasePresenter {
    protected val disposables: CompositeDisposable = CompositeDisposable()

    fun stopSubscriptions() {
        disposables.clear()
    }
}