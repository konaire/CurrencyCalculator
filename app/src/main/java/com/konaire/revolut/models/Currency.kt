package com.konaire.revolut.models

import com.konaire.revolut.ui.list.ListItemType
import com.konaire.revolut.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
open class Currency(
    val name: String = "",
    val rate: Float = 0F
): ViewType {
    override fun getViewType(): Int = ListItemType.CURRENCY.ordinal
}

data class CurrencyResponse(
    val base: String = "",
    val rates: MutableList<Currency> = ArrayList()
)