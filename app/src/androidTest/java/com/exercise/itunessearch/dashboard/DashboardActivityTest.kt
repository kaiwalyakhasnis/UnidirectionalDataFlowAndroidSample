package com.exercise.itunessearch.dashboard

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.exercise.itunessearch.R
import com.exercise.itunessearch.R.id.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashBoardActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(DashboardActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(mActivityTestRule.activity.okHttp3IdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mActivityTestRule.activity.okHttp3IdlingResource)
    }

    @Test
    fun dashboard_preview_list_valid_term() {
        onView(ViewMatchers.withId(edt_search_term))
                .perform(clearText(), typeText("akon"))

        onView(ViewMatchers.withId(btn_search))
                .perform(click())

        onView(ViewMatchers.withId(preview_list))
                .perform(RecyclerViewActions
                        .scrollToPosition<PreviewListAdapter.PreviewListHolders>
                        (3))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Beautiful"))));
    }

    @Test
    fun dashboard_preview_list_invalid_term() {
        onView(ViewMatchers.withId(edt_search_term))
                .perform(clearText(), typeText("weshjdhfcjbchfhfg"))

        onView(ViewMatchers.withId(btn_search))
                .perform(click())

        onView(ViewMatchers.withId(preview_list))
                .perform(RecyclerViewActions
                        .scrollToPosition<PreviewListAdapter.PreviewListHolders>
                        (1))
                .check(ViewAssertions.matches(ViewMatchers
                        .hasDescendant(ViewMatchers.withText("No search results available for given search term"))));
    }

    @Test
    fun dashboard_ui_visibility() {
        onView(
                Matchers.allOf(ViewMatchers.withText("iTunesSearch"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                                0)),
                                0),
                        ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText("iTunesSearch")))

        onView(
                Matchers.allOf(ViewMatchers.withText("iTunesSearch"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                                0)),
                                0),
                        ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_search),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.search_term_container),
                                        0),
                                1),
                        ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

