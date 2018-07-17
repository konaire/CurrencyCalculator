package com.konaire.revolut.ui.currency

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.konaire.revolut.R
import com.konaire.revolut.models.Currency
import com.konaire.revolut.presenters.currency.CurrencyCalculatorPresenter
import com.konaire.revolut.ui.BaseFragment
import com.konaire.revolut.ui.BaseListView
import com.konaire.revolut.ui.list.DividerDecoration
import com.konaire.revolut.ui.currency.adapters.CurrencyAdapter
import com.konaire.revolut.util.OnValueChangedListener
import com.konaire.revolut.util.hideKeyboard

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.fragment_currency_calculator.*

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 23/04/2018.
 */
interface CurrencyCalculatorView: BaseListView<Currency>, OnValueChangedListener<Currency>

class CurrencyCalculatorFragment: BaseFragment(), CurrencyCalculatorView {
    @Inject lateinit var presenter: CurrencyCalculatorPresenter
    private var adapter: CurrencyAdapter? = null

    companion object {
        private const val TAG = "CURRENCY_LIST"

        fun create(): CurrencyCalculatorFragment {
            val fragment = CurrencyCalculatorFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_currency_calculator, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (adapter == null) {
            adapter = CurrencyAdapter(this, this)
        }

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(DividerDecoration(activity!!))
        list.setOnTouchListener { _, _ ->
            activity?.hideKeyboard()
            false
        }

        swipe.setOnRefreshListener { presenter.getLatestCurrencyRates("EUR") }
        emptyView?.visibility = if (adapter!!.isNotEmpty()) View.GONE else View.VISIBLE

        presenter.getLatestCurrencyRates("EUR")
    }

    override fun onStop() {
        super.onStop()
        presenter.stopSubscriptions()
    }

    override fun getTitle(): String = getString(R.string.app_name)

    override fun getFragmentTag(): String = TAG

    override fun isRoot(): Boolean = true

    override fun showProgress() {
        swipe?.isRefreshing = true
    }

    override fun hideProgress() {
        swipe?.isRefreshing = false
    }

    override fun showData(data: MutableList<Currency>) {
        if (data.isNotEmpty()) {
            adapter?.baseCurrency = data[0]
            adapter?.reinit(data)

            emptyView?.visibility = View.GONE
        } else {
            emptyView?.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(item: Currency, view: View?) {
        adapter?.baseCurrency = item
    }

    override fun onValueChanged(value: Currency) {
        adapter?.baseCurrency = value
        adapter?.notifyAllItemsExcept(value)
    }
}