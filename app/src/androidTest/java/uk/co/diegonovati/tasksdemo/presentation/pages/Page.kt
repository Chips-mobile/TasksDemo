package uk.co.diegonovati.tasksdemo.presentation.pages

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf

open class Page(val pageMatcher: Matcher<View>)

fun <P : Page> P.closeKeyboard(): P {
    Espresso.closeSoftKeyboard()
    return this
}

fun <P : Page> P.pressBack(): P {
    Espresso.pressBack()
    return this
}

fun <P : Page> P.assertOnPage(): P {
    // As multiple fragments may be "visible" at a time (stacked on top of each other), we check
    // that the fragment is the last child in the parent (i.e., top most)
    onView(allOf(this.pageMatcher, isLastChildInParent())).check(matches(isDisplayed()))
    return this
}

fun <P : Page> P.assertNotOnPage(): P {
    // As multiple fragments may be "visible" at a time (stacked on top of each other), we check
    // that the fragment is the last child in the parent (i.e., top most)
    onView(allOf(this.pageMatcher, isLastChildInParent())).check(doesNotExist())
    return this
}

fun <P : Page> P.waitForPage(): P {
    waitUntil { !throwsException { this.assertOnPage() } }
    Thread.sleep(1000)
    return this
}

fun withRootId(resourceID: Int): Matcher<View> {
    return CoreMatchers.allOf(withId(resourceID))
}

fun waitUntil(timeOutMs: Int = 20000, testIntervalMs: Int = 100, tester: () -> Boolean) {
    var remainingTimeOutMs = timeOutMs
    while (!tester()) {
        if (remainingTimeOutMs <= 0) {
            throw Error("Wait failed after ${timeOutMs}ms")
        }
        Thread.sleep(testIntervalMs.toLong())
        remainingTimeOutMs -= testIntervalMs
    }
}

fun throwsException(tester: () -> Unit): Boolean {
    try {
        tester()
    } catch (ex: Exception) {
        return true
    }
    return false
}

fun isLastChildInParent(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("is last child in parent")
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return (parent is ViewGroup
                    && parent.getChildAt(parent.childCount - 1) === view)
        }

    }
}