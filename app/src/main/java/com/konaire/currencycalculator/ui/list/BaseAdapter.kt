package com.konaire.currencycalculator.ui.list

import android.support.v4.util.ArrayMap
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
abstract class BaseAdapter<T>(
    protected val listener: OnItemClickedListener<T>? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() where T: ViewType {
    protected var delegateAdapters: MutableMap<Int, DelegateAdapter<T>> = ArrayMap()
    protected var items: MutableList<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegateAdapters[viewType]!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegateAdapters[getItemViewType(position)]!!.onBindViewHolder(holder, items[position])

    override fun getItemViewType(position: Int): Int = items[position].getViewType()

    override fun getItemCount(): Int = items.size

    abstract fun getDiffCallback(oldItems: MutableList<T>, newItems: MutableList<T>): DiffUtil.Callback

    open fun reinit(data: MutableList<T>) {
        val previousItems = items

        items = data
        notifyOnlyItemsThatChanged(previousItems, data)
    }

    fun notifyOnlyItemsThatChanged(oldItems: MutableList<T> = items, newItems: MutableList<T> = items) {
        val diffResult = DiffUtil.calculateDiff(getDiffCallback(oldItems, newItems))
        diffResult.dispatchUpdatesTo(this)
    }

    fun isEmpty() = itemCount == 0

    fun isNotEmpty() = itemCount > 0
}