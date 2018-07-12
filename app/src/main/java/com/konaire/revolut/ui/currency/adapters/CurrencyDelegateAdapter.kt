package com.konaire.revolut.ui.currency.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.konaire.revolut.R
import com.konaire.revolut.models.Currency
import com.konaire.revolut.ui.list.DelegateAdapter
import com.konaire.revolut.ui.list.OnItemClickedListener
import com.konaire.revolut.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class CurrencyDelegateAdapter(
    private val listener: OnItemClickedListener<Currency>
): DelegateAdapter<ViewType> {
    class CurrencyViewHolder(
        rootView: View,
        private val listener: OnItemClickedListener<Currency>
    ): RecyclerView.ViewHolder(rootView) {
        fun bind(currency: Currency)  = with(itemView) {
            val name = findViewById<TextView>(R.id.name)
            name.text = currency.name

            setOnClickListener { listener.onItemClicked(currency, this) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder =
        CurrencyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_currency, parent, false), listener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) =
        (holder as CurrencyViewHolder).bind(item as Currency)
}