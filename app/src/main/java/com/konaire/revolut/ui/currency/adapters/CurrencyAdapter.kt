package com.konaire.revolut.ui.currency.adapters

import com.konaire.revolut.models.Currency
import com.konaire.revolut.ui.list.BaseAdapter
import com.konaire.revolut.ui.list.ListItemType
import com.konaire.revolut.ui.list.OnItemClickedListener

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class CurrencyAdapter(
    listener: OnItemClickedListener<Currency>
): BaseAdapter<Currency>(listener) {
    init {
        delegateAdapters[ListItemType.CURRENCY.ordinal] = CurrencyDelegateAdapter(listener)
    }
}