package com.konaire.currencycalculator.ui.currency.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

import com.konaire.currencycalculator.models.Currency
import com.konaire.currencycalculator.ui.list.BaseAdapter
import com.konaire.currencycalculator.ui.list.ListItemType
import com.konaire.currencycalculator.ui.list.OnItemClickedListener
import com.konaire.currencycalculator.util.OnValueChangedListener

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

import java.util.concurrent.TimeUnit

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class CurrencyAdapter(
    clickListener: OnItemClickedListener<Currency>
): BaseAdapter<Currency>(clickListener), OnValueChangedListener<Currency> {
    private val subject: PublishSubject<Currency> = PublishSubject.create()
    private var subscription: Disposable? = null
    lateinit var baseCurrency: Currency

    init {
        delegateAdapters[ListItemType.CURRENCY.ordinal] = CurrencyDelegateAdapter(clickListener, this)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        subscribeToSubject()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        subscription?.dispose()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val adapter = delegateAdapters[getItemViewType(position)]
        if (adapter is CurrencyDelegateAdapter) {
            adapter.baseCurrency = baseCurrency
        }

        super.onBindViewHolder(holder, position)
    }

    override fun getDiffCallback(oldItems: MutableList<Currency>, newItems: MutableList<Currency>): DiffUtil.Callback = object: DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition].name == newItems[newItemPosition].name

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = newItems[newItemPosition].name == baseCurrency.name

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size
    }

    override fun reinit(data: MutableList<Currency>) {
        updateBaseCurrencyRate(data)
        super.reinit(data)
    }

    override fun onValueChanged(value: Currency) {
        subject.onNext(value)
    }

    fun getTopItem(): Currency? = if (items.isNotEmpty()) items[0] else null

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

    private fun updateBaseCurrencyRate(data: MutableList<Currency>) {
        try {
            val index = data.indexOfFirst { item -> item.name == baseCurrency.name }
            if (index <= 0) {
                return
            }

            baseCurrency.rate = data[index].rate
        } catch (e: UninitializedPropertyAccessException) {
            baseCurrency = data[0]
        }
    }

    private fun subscribeToSubject() {
        subscription = subject.debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currency ->
                baseCurrency = currency
                notifyOnlyItemsThatChanged()
            }
    }
}