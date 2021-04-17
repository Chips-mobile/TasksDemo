package uk.co.diegonovati.tasksdemo.presentation

import androidx.test.core.app.launchActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.co.diegonovati.tasksdemo.data.datasources.IConnectivityMonitorDataSource
import uk.co.diegonovati.tasksdemo.data.models.ConnectionStatus
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksLocalRepository
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksRemoteRepository
import uk.co.diegonovati.tasksdemo.presentation.activity.MainActivity
import uk.co.diegonovati.tasksdemo.presentation.pages.MainPage
import uk.co.diegonovati.tasksdemo.presentation.pages.waitForPage
import javax.inject.Inject

@HiltAndroidTest
class MainView {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockTasksRemoteRepository: ITasksRemoteRepository
    @Inject
    lateinit var mockTasksLocalRepository: ITasksLocalRepository
    @Inject
    lateinit var fakeConnectivityMonitorDataSource: IConnectivityMonitorDataSource

    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun whenStartedTheAppShowsAList() {
        launchActivity()

        MainPage()
            .waitForPage()
            .checkListIsVisible()
    }

    @Test
    fun whenTheDeviceIsOffLineItDisplaysARedDot() {
        launchActivity()

        MainPage()
            .waitForPage()
            .setDeviceConnectionStatus(mainActivity, ConnectionStatus.Disconnected)
            .ensureConnectionStatusIsOffline()
    }

    @Test
    fun whenTheDeviceIsOnLineItDisplaysAGreenDot() {
        launchActivity()

        MainPage()
            .waitForPage()
            .setDeviceConnectionStatus(mainActivity, ConnectionStatus.Connected)
            .ensureConnectionStatusIsOnline()
    }

    private fun launchActivity() {
        launchActivity<MainActivity>().onActivity {
            mainActivity = it
        }
    }
}