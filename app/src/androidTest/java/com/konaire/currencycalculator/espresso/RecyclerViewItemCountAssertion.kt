package com.konaire.currencycalculator.espresso

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.v7.widget.RecyclerView
import android.view.View

import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*

/**
 * Created by Evgeny Eliseyev on 27/04/2018.
 */
class RecyclerViewItemCountAssertion(
    private val matcher: Matcher<Int>
): ViewAssertion {
    companion object {
        fun withItemCount(expectedCount: Int): RecyclerViewItemCountAssertion = RecyclerViewItemCountAssertion(`is`(expectedCount))

        fun withItemCount(matcher: Matcher<Int>): RecyclerViewItemCountAssertion  = RecyclerViewItemCountAssertion(matcher)
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        assertThat(recyclerView.adapter.itemCount, matcher)
    }
}