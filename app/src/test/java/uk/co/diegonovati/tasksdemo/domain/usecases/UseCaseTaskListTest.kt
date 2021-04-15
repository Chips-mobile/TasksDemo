package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import uk.co.diegonovati.tasksdemo.common.BaseTest
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksLocalRepository
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksRemoteRepository
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class UseCaseTaskListTest : BaseTest() {

    @Mock
    private lateinit var mockTasksLocalRepository: ITasksLocalRepository

    @Mock
    private lateinit var mockTasksRemoteRepository: ITasksRemoteRepository

    private lateinit var useCaseTaskList: UseCaseTaskList

    private val taskModelList = listOf(
        TaskModel(1, "name 1", "description 1", TaskType.General.value),
        TaskModel(2, "name 2", "description 2", TaskType.Hydration.value),
    )
    private val noFilter =
        listOf(TaskType.General, TaskType.Hydration, TaskType.Medication, TaskType.Nutrition)

    @Before
    fun setUp() {
        useCaseTaskList = UseCaseTaskList(
            tasksLocalRepository = mockTasksLocalRepository,
            tasksRemoteRepository = mockTasksRemoteRepository,
        )
    }

    @Test
    fun `Given the remote list is successful When the use case is invoked Then it lists all the tasks`() =
        runBlocking {
            Dispatchers.setMain(Dispatchers.Unconfined)

            whenever(mockTasksRemoteRepository.list()).thenReturn(Result.success(taskModelList))

            val countdown = CountDownLatch(1)
            useCaseTaskList.invoke(noFilter) {
                assertTrue(it is Result.Success)
                assertNotNull(it.get().lastUpdate)
                assertFalse(it.get().fromCache)
                assertEquals(taskModelList.size, it.get().taskList.size)
                assertEquals(taskModelList[0].id, it.get().taskList[0].id)
                assertEquals(taskModelList[1].id, it.get().taskList[1].id)

                countdown.countDown()
            }

            assertTrue(countdown.await(5, TimeUnit.SECONDS))

            verify(mockTasksRemoteRepository).list()
            verify(mockTasksLocalRepository).save(any())
            verifyNoMoreInteractions(mockTasksLocalRepository)
        }

    @Test
    fun `Given the remote list fails When the use case is invoked Then it returns the data from the local cache`() =
        runBlocking<Unit> {
            Dispatchers.setMain(Dispatchers.Unconfined)

            val taskDataModel = TaskDataModel(
                lastUpdate = Date(),
                taskList = taskModelList,
            )
            whenever(mockTasksRemoteRepository.list()).thenReturn(Result.error(Exception()))
            whenever(mockTasksLocalRepository.load()).thenReturn(Result.success(taskDataModel))

            val countdown = CountDownLatch(1)
            useCaseTaskList.invoke(noFilter) {
                assertTrue(it is Result.Success)
                assertNotNull(it.get().lastUpdate)
                assertTrue(it.get().fromCache)
                assertEquals(taskModelList.size, it.get().taskList.size)
                assertEquals(taskModelList[0].id, it.get().taskList[0].id)
                assertEquals(taskModelList[1].id, it.get().taskList[1].id)

                countdown.countDown()
            }

            assertTrue(countdown.await(5, TimeUnit.SECONDS))

            verify(mockTasksRemoteRepository).list()
            verify(mockTasksLocalRepository).load()
        }

    @Test
    fun `Given the remote list fails And the local load fails too When the use case is invoked Then it returns an error`() =
        runBlocking<Unit> {
            Dispatchers.setMain(Dispatchers.Unconfined)

            whenever(mockTasksRemoteRepository.list()).thenReturn(Result.error(Exception()))
            whenever(mockTasksLocalRepository.load()).thenReturn(Result.error(Exception()))

            val countdown = CountDownLatch(1)
            useCaseTaskList.invoke(noFilter) {
                assertTrue(it is Result.Failure)

                countdown.countDown()
            }

            assertTrue(countdown.await(5, TimeUnit.SECONDS))

            verify(mockTasksRemoteRepository).list()
            verify(mockTasksLocalRepository).load()
        }

    @Test
    fun `Given the remote list is successful And there is filter active When the use case is invoked Then it returns a filtered list of tasks`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        whenever(mockTasksRemoteRepository.list()).thenReturn(Result.success(taskModelList))

        val countdown = CountDownLatch(1)
        useCaseTaskList.invoke(listOf(TaskType.General)) {
            assertTrue(it is Result.Success)
            assertNotNull(it.get().lastUpdate)
            assertFalse(it.get().fromCache)
            assertEquals(1, it.get().taskList.size)
            assertEquals(taskModelList[0].id, it.get().taskList[0].id)

            countdown.countDown()
        }

        assertTrue(countdown.await(5, TimeUnit.SECONDS))

        verify(mockTasksRemoteRepository).list()
        verify(mockTasksLocalRepository).save(any())
        verifyNoMoreInteractions(mockTasksLocalRepository)
    }

    @Test
    fun `Given the remote list fails And there is filter active When the use case is invoked Then it returns a filtered list of cached tasks`() = runBlocking<Unit> {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val taskDataModel = TaskDataModel(
            lastUpdate = Date(),
            taskList = taskModelList,
        )
        whenever(mockTasksRemoteRepository.list()).thenReturn(Result.error(Exception()))
        whenever(mockTasksLocalRepository.load()).thenReturn(Result.success(taskDataModel))

        val countdown = CountDownLatch(1)
        useCaseTaskList.invoke(listOf(TaskType.General)) {
            assertTrue(it is Result.Success)
            assertNotNull(it.get().lastUpdate)
            assertTrue(it.get().fromCache)
            assertEquals(1, it.get().taskList.size)
            assertEquals(taskModelList[0].id, it.get().taskList[0].id)

            countdown.countDown()
        }

        assertTrue(countdown.await(5, TimeUnit.SECONDS))

        verify(mockTasksRemoteRepository).list()
        verify(mockTasksLocalRepository).load()
    }
}