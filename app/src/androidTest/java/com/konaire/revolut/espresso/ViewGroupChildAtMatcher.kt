package com.konaire.revolut.espresso

import android.view.View
import android.view.ViewGroup

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by Evgeny Eliseyev on 16/05/2018.
 */
class ViewGroupChildAtMatcher private constructor(
    private val parentMatcher: Matcher<View>,
    private val position: Int
): TypeSafeMatcher<View>() {
    companion object {
        fun getChildAt(parentMatcher: Matcher<View>, position: Int): ViewGroupChildAtMatcher = ViewGroupChildAtMatcher(parentMatcher, position)
    }

    override fun matchesSafely(view: View?): Boolean {
        val parent = view?.parent
        return parent is ViewGroup &&
            parentMatcher.matches(parent) &&
            parent.getChildAt(position) == view
    }

    override fun describeTo(description: Description?) {
        description?.appendText("with $position child view")
    }
}