package uk.co.diegonovati.tasksdemo.presentation.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.data.models.ConnectionStatus
import uk.co.diegonovati.tasksdemo.presentation.activity.MainActivity

class MainPage: Page(withRootId(R.id.mainActivity)) {

    fun setDeviceConnectionStatus(mainActivity: MainActivity, connectionStatus: ConnectionStatus): MainPage {
        mainActivity.getViewModel().updateInternetConnectionStatus(connectionStatus)
        return this
    }

    fun checkListIsVisible(): MainPage {
        onView(allOf(withText("Take the rubbish out"))).check(ViewAssertions.matches(isDisplayed()))
        return this
    }

    fun ensureConnectionStatusIsOnline(): MainPage {
        Thread.sleep(500)
        onView(withId(R.id.deviceOnline)).check(matches(ViewMatchers.isDisplayed()))
        return this
    }

    fun ensureConnectionStatusIsOffline(): MainPage {
        Thread.sleep(500)
        onView(withId(R.id.deviceOffline)).check(matches(ViewMatchers.isDisplayed()))
        return this
    }
}