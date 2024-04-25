package com.dicoding.githubuser.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.githubuser.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{
    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertGetSurfaceArea() {
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()))
        onView(withId(R.id.topAppBar)).check(matches(isDisplayed()))
        onView(withId(R.id.menu1)).check(matches(isDisplayed()))
        onView(withId(R.id.menu2)).check(matches(isDisplayed()))
        onView(withId(R.id.menu3)).check(matches(isDisplayed()))
    }
}