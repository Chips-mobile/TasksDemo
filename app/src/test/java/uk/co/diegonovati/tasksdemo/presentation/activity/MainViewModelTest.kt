package uk.co.diegonovati.tasksdemo.presentation.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.github.kittinunf.result.Result
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import uk.co.diegonovati.tasksdemo.common.BaseTest
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.usecases.*

class MainViewModelTest: BaseTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockSavedStateHandle: SavedStateHandle
    @Mock
    private lateinit var mockUseCaseTaskList: UseCaseTaskList
    @Mock
    private lateinit var mockUseCaseTaskFilter: UseCaseTaskFilter
    @Mock
    private lateinit var mockUseCaseConnectivityMonitorStart: UseCaseConnectivityMonitorStart
    @Mock
    private lateinit var mockUseCaseConnectivityMonitorStop: UseCaseConnectivityMonitorStop
    @Mock
    private lateinit var mockUseCaseConnectivitySetChangeListener: UseCaseConnectivitySetChangeListener

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mockSavedStateHandle, mockUseCaseTaskList, mockUseCaseTaskFilter,
            mockUseCaseConnectivityMonitorStart, mockUseCaseConnectivityMonitorStop,
            mockUseCaseConnectivitySetChangeListener)

        mainViewModel.getData()     // This is needed to avoid the loop during the doLoad() testing
    }

    @Test
    fun `When doLoad is invoked Then the getData notifies an update`() {
        val mockTaskData = mock<TaskData>()
        whenever(mockUseCaseTaskList.invoke(eq(mainViewModel.filterBy), any())).thenAnswer {
            it.getArgument<(Result<TaskData, Exception>) -> Unit>(1).invoke(Result.success(mockTaskData))
        }

        mainViewModel.doLoad()

        assertTrue(mainViewModel.getData().value!!.reloaded)
        assertTrue(mainViewModel.getData().value!!.data is Result.Success)
        assertEquals(mockTaskData, mainViewModel.getData().value!!.data.get())
    }

    @Test
    fun `When doLoad is invoked And the usecase returns an error Then the getData notifies an error`() {
        val mockException = mock<Exception>()
        whenever(mockUseCaseTaskList.invoke(eq(mainViewModel.filterBy), any())).thenAnswer {
            it.getArgument<(Result<TaskData, Exception>) -> Unit>(1).invoke(Result.error(mockException))
        }

        mainViewModel.doLoad()

        assertTrue(mainViewModel.getData().value!!.reloaded)
        assertTrue(mainViewModel.getData().value!!.data is Result.Failure)
        assertEquals(mockException, mainViewModel.getData().value!!.data.component2())
    }

    @Test
    fun `Given the viewmodel has just been created Then all the filters are disable`() {
        val actual = mainViewModel.filterBy

        assertTrue(TaskType.General in actual)
        assertTrue(TaskType.Hydration in actual)
        assertTrue(TaskType.Medication in actual)
        assertTrue(TaskType.Nutrition in actual)
    }

    @Test
    fun `When doFilter is invoked Then the corresponding TaskType is added-removed from the filterBy`() {
        assertTrue(TaskType.Hydration in mainViewModel.filterBy)

        mainViewModel.doFilter(TaskType.Hydration, false)

        assertFalse(TaskType.Hydration in mainViewModel.filterBy)

        mainViewModel.doFilter(TaskType.Hydration, true)

        assertTrue(TaskType.Hydration in mainViewModel.filterBy)
    }

    @Test
    fun `When doFilter is invoked Then the getData notifies an update`() {
        val mockTaskData = mock<TaskData>()
        whenever(mockUseCaseTaskFilter.invoke(eq(mainViewModel.filterBy), any())).thenAnswer {
            it.getArgument<(Result<TaskData, Exception>) -> Unit>(1).invoke(Result.success(mockTaskData))
        }

        mainViewModel.doFilter(TaskType.Hydration, false)

        assertFalse(mainViewModel.getData().value!!.reloaded)
        assertTrue(mainViewModel.getData().value!!.data is Result.Success)
        assertEquals(mockTaskData, mainViewModel.getData().value!!.data.get())
    }
}