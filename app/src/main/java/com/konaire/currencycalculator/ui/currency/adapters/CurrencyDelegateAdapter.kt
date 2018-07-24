package com.konaire.currencycalculator.ui.currency.adapters

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.konaire.currencycalculator.R
import com.konaire.currencycalculator.models.Currency
import com.konaire.currencycalculator.ui.list.DelegateAdapter
import com.konaire.currencycalculator.ui.list.OnItemClickedListener
import com.konaire.currencycalculator.ui.list.ViewType
import com.konaire.currencycalculator.util.OnValueChangedListener
import com.konaire.currencycalculator.util.OnTextChangeListenerWithValue

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class CurrencyDelegateAdapter(
    private val clickListener: OnItemClickedListener<Currency>,
    private val valueChangedListener: OnValueChangedListener<Currency>
): DelegateAdapter<ViewType> {
    class CurrencyViewHolder(
        rootView: View,
        private val clickListener: OnItemClickedListener<Currency>,
        private val textWatcher: OnTextChangeListenerWithValue<Currency>
    ): RecyclerView.ViewHolder(rootView) {
        lateinit var baseCurrency: Currency

        init {
            val value = itemView.findViewById<TextView>(R.id.value)
            value.onFocusChangeListener = textWatcher
            value.addTextChangedListener(textWatcher)
        }

        fun bind(currency: Currency) = with(itemView) {
            val flag = findViewById<TextView>(R.id.flag)
            val name = findViewById<TextView>(R.id.name)
            val description = findViewById<TextView>(R.id.description)
            val value = findViewById<TextView>(R.id.value)

            textWatcher.value = currency

            name.text = currency.name
            flag.text = currency.getFlag()
            description.text = currency.getDescription(context)
            value.text = currency.calculateValue(context, baseCurrency)

            setOnClickListener { clickListener.onItemClicked(currency, this) }
        }
    }

    lateinit var baseCurrency: Currency

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder =
        CurrencyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_currency, parent, false), clickListener, createWatcher())

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) {
        val currencyHolder = holder as CurrencyViewHolder
        currencyHolder.baseCurrency = baseCurrency
        currencyHolder.bind(item as Currency)
    }

    private fun createWatcher() = object: OnTextChangeListenerWithValue<Currency> {
        override var value: Currency? = null

        override fun onFocusChange(view: View?, hasFocus: Boolean) {
            val currency = value ?: return
            if (hasFocus) {
                valueChangedListener.onValueChanged(currency)
            }
        }

        override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
            val currency = value ?: return

            try {
                currency.value = str.toString().toFloat()
                if (currency.name == baseCurrency.name) {
                    valueChangedListener.onValueChanged(currency)
                }
            } catch (e: NumberFormatException) {
                // do nothing
            }
        }

        override fun afterTextChanged(str: Editable?) { }
    }
}