package com.konaire.revolut.ui.currency

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.contrib.RecyclerViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView

import com.konaire.revolut.MockApp
import com.konaire.revolut.R
import com.konaire.revolut.di.DaggerMockAppComponent
import com.konaire.revolut.espresso.RecyclerViewItemCountAssertion
import com.konaire.revolut.espresso.ViewGroupChildAtMatcher
import com.konaire.revolut.models.Currency
import com.konaire.revolut.models.CurrencyResponse
import com.konaire.revolut.network.Api
import com.konaire.revolut.util.Config
import com.konaire.revolut.util.PreferenceManager

import io.reactivex.Flowable

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
    @Inject lateinit var config: Config
    @Inject lateinit var preferenceManager: PreferenceManager

    @Before
    fun setUp() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MockApp
        (app.component as DaggerMockAppComponent).inject(this)
        config.useAutoUpdate = false
    }

    @Test
    fun checkWhenEverythingIsFineAndThatBaseIsAlsoIncluded() {
        val networkResult = CurrencyResponse(Currency(), listOf(
            Currency(), Currency(), Currency(), Currency(), Currency(), Currency()
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult)
        activityRule.launchActivity(Intent())
        onView(withId(R.id.emptyView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(7))
    }

    @Test
    fun checkWhenNetworkCrashes() {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Flowable.error(Exception()))
        mockPreferences()

        activityRule.launchActivity(Intent())
        onView(withId(R.id.emptyView)).check(matches(isDisplayed()))
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(0))
    }

    @Test
    fun checkThatRatesApplyCorrectlyInBaseCase() {
        val networkResult = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        mockPreferences()
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

        mockPreferences()
        mockNetwork(networkResult)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).check(matches(withText("1.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("3.00")))

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).perform(clearText()).perform(typeText("9"))
        Thread.sleep(1000) // wait for debouncing

        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).check(matches(withText("3.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("6.00")))
    }

    @Test
    fun checkThatRatesUpdateIfDataIsTheSame() {
        val networkResult1 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        val networkResult2 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 4F), Currency("C", 6F)
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult1, networkResult2)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("4.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("6.00")))

        onView(withId(R.id.swipe)).perform(swipeDown())
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("8.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("12.00")))
    }

    @Test
    fun checkThatRatesUpdateIfDataHasNewElement() {
        val networkResult1 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("C", 3F)
        ).toMutableList())

        val networkResult2 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 4F), Currency("C", 6F)
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult1, networkResult2)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(2))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("6.00")))

        onView(withId(R.id.swipe)).perform(swipeDown())
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("8.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("12.00")))
    }

    @Test
    fun checkThatRatesUpdateIfDataHasNewElementInTheEnd() {
        val networkResult1 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F)
        ).toMutableList())

        val networkResult2 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 4F), Currency("C", 6F)
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult1, networkResult2)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(2))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("4.00")))

        onView(withId(R.id.swipe)).perform(swipeDown())
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("8.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("12.00")))
    }

    @Test
    fun checkThatRatesUpdateIfDataLacksOneElement() {
        val networkResult1 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        val networkResult2 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("C", 6F)
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult1, networkResult2)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("4.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("6.00")))

        onView(withId(R.id.swipe)).perform(swipeDown())
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(2))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("12.00")))
    }

    @Test
    fun checkThatRatesUpdateIfDataLacksOneElementInTheEnd() {
        val networkResult1 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        val networkResult2 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 4F)
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult1, networkResult2)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("4.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("6.00")))

        onView(withId(R.id.swipe)).perform(swipeDown())
        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(2))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("8.00")))
    }

    @Test
    fun checkThatBaseCurrencyIsUpdatedCorrectly() {
        val networkResult1 = CurrencyResponse(Currency("A", 1F), listOf(
            Currency("B", 2F), Currency("C", 3F)
        ).toMutableList())

        val networkResult2 = CurrencyResponse(Currency("B", 1F), listOf(
            Currency("A", 0.5F), Currency("C", 0.25F)
        ).toMutableList())

        mockPreferences()
        mockNetwork(networkResult1, networkResult2)
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).perform(typeText("2"))
        Thread.sleep(1000) // wait for debouncing

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("4.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("6.00")))

        onView(withId(R.id.list)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(withId(R.id.list)).check(RecyclerViewItemCountAssertion.withItemCount(3))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 0)))).check(matches(withText("4.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 1)))).check(matches(withText("2.00")))
        onView(allOf(withId(R.id.value), isDescendantOfA(ViewGroupChildAtMatcher.getChildAt(withId(R.id.list), 2)))).check(matches(withText("1.00")))
    }

    private fun mockPreferences() {
        `when`(preferenceManager.getString(anyString(), anyString())).thenReturn("")
    }

    private fun mockNetwork(response: CurrencyResponse) {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Flowable.just(response))
    }

    private fun mockNetwork(response1: CurrencyResponse, response2: CurrencyResponse) {
        `when`(api.getLatestCurrencyRates(anyString())).thenReturn(Flowable.just(response1)).thenReturn(Flowable.just(response2))
    }
}