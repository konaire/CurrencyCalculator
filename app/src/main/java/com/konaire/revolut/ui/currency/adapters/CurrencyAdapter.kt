package com.konaire.revolut.ui.currency.adapters

import android.support.v7.widget.RecyclerView

import com.konaire.revolut.models.Currency
import com.konaire.revolut.ui.list.BaseAdapter
import com.konaire.revolut.ui.list.ListItemType
import com.konaire.revolut.ui.list.OnItemClickedListener
import com.konaire.revolut.util.OnValueChangedListener
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.subjects.PublishSubject

import java.util.concurrent.TimeUnit

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class CurrencyAdapter(
    clickListener: OnItemClickedListener<Currency>
): BaseAdapter<Currency>(clickListener), OnValueChangedListener<Currency> {
    private val subject: PublishSubject<Currency> = PublishSubject.create()
    var baseCurrency: Currency? = null

    init {
        delegateAdapters[ListItemType.CURRENCY.ordinal] = CurrencyDelegateAdapter(clickListener, this)
        subscribeToSubject()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val adapter = delegateAdapters[getItemViewType(position)]
        if (adapter is CurrencyDelegateAdapter) {
            adapter.baseCurrency = baseCurrency!!
        }

        super.onBindViewHolder(holder, position)
    }

    override fun onValueChanged(value: Currency) {
        subject.onNext(value)
    }

    fun getFirstItem(): Currency = items[0]

    fun updateRates(data: MutableList<Currency>) {
        if (isEmpty()) {
            reinit(data)
            return
        }

        var index = 0
        while (index < items.size && index < data.size) {
            if (items[index].name == data[index].name) {
                items[index].rate = data[index].rate
                if (items[index] != baseCurrency) {
                    notifyItemChanged(index)
                }

                index++
            } else if (index == 0 && data[index].name == baseCurrency?.name) { // base currency was updated
                items[index] = data[index]
                notifyItemChanged(index)
            } else if (items[index].name > data[index].name) { // data has a new currency
                items.add(index, data[index])
                notifyItemInserted(index)
            } else { // data has no such currency
                items.removeAt(index)
                notifyItemRemoved(index)
            }
        }

        while (index < data.size) {
            items.add(data[index])
            notifyItemInserted(index)

            index++
        }

        while (index < items.size) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateBaseCurrency(currency: Currency) {
        val index = items.indexOf(currency)
        if (index <= 0) {
            return
        }

        currency.rate = 1F
        baseCurrency = currency
        items.removeAt(index)

        notifyItemRemoved(index)
    }

    private fun notifyAllItemsExcept(currency: Currency) {
        val firstIndex = 0
        val lastIndex = itemCount - 1
        val index = items.indexOf(currency)
        if (index < firstIndex) {
            return
        }

        if (index > firstIndex) {
            notifyItemRangeChanged(firstIndex, index)
        }

        if (index < lastIndex) {
            notifyItemRangeChanged(index + 1, lastIndex - index)
        }
    }

    private fun subscribeToSubject() {
        subject.debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currency ->
                baseCurrency = currency
                notifyAllItemsExcept(currency)
            }
    }
}