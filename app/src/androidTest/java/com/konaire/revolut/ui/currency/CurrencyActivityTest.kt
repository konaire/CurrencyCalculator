package com.konaire.revolut.ui.currency

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import com.konaire.revolut.MockApp
import com.konaire.revolut.R
import com.konaire.revolut.di.DaggerMockAppComponent
import com.konaire.revolut.espresso.RecyclerViewItemCountAssertion
import com.konaire.revolut.espresso.ViewGroupChildAtMatcher
import com.konaire.revolut.models.Currency
import com.konaire.revolut.models.CurrencyResponse
import com.konaire.revolut.network.Api

import io.reactivex.Single

import org.hamcrest.Matchers.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.*

import javax.inject.Inject

/**
 * Created by Evgeny Eliseyev on 26/04/2018.
 */
@RunWith(AndroidJUnit4::class)
class CurrencyActivityTest {
    @Rule @JvmField val activityRule: ActivityTestRule<CurrencyActivity> = ActivityTestRule(CurrencyActivity::class.java, true, false)

    @Inject lateinit var api: Api

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MockApp
        (app.component as DaggerMockAppComponent).inject(this)
    }

    @Test
    fun checkWhenEverythingIsFineAndThatBaseIsAlsoIncluded() {
        val networkResult = CurrencyResponse(Currency(), listOf(
            Currency(), Currency(), Currency(), Currency(), Currency(), Currency()
        ).toMutableList())

        mockNetwork(networkResult)
        activityRule.launchActivity(Intent())
        onView(withId(R.id.emptyView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(7))
    }

    @Test
    fun checkWhenNetworkCrashes() {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Single.error(Exception()))

        activityRule.launchActivity(Intent())
        onView(withId(R.id.emptyView)).check(matches(isDisplayed()))
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(0))
    }

    @Test
    fun checkThatRatesApplyCorrectlyInBaseCase() {
        val networkResult = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        mockNetwork(networkResult)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("4.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("6.00")))
    }

    @Test
    fun checkThatValuesAreUpdatedAfterChangingBase() {
        val networkResult = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        mockNetwork(networkResult)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).perform(clearText()).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).check(matches(withText("1.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("3.00")))

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).perform(clearText()).perform(typeText("9"))
        Thread.sleep(1000) // wait for debouncing

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).check(matches(withText("3.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("6.00")))
    }

    private fun mockNetwork(response: CurrencyResponse) {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Single.just(response))
    }
}