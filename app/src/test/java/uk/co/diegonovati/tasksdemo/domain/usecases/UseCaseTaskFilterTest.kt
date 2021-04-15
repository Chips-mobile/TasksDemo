package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class UseCaseTaskFilterTest : BaseTest() {

    @Mock
    private lateinit var mockTasksLocalRepository: ITasksLocalRepository

    private lateinit var useCaseTaskFilter: UseCaseTaskFilter

    @Before
    fun setUp() {
        useCaseTaskFilter = UseCaseTaskFilter(mockTasksLocalRepository)
    }

    @Test
    fun `Given a taskData with a list of tasks And a filter with some values When use case is invoked Then is returned a taskData with a filtered list of tasks`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val taskDataModel = TaskDataModel(
            lastUpdate = Date(),
            taskList = listOf(
                TaskModel(1, "name 1", "description 1", TaskType.Medication.value),
                TaskModel(2, "name 2", "description 2", TaskType.General.value),
                TaskModel(3, "name 3", "description 3", TaskType.Hydration.value),
                TaskModel(4, "name 4", "description 4", TaskType.Nutrition.value),
            )
        )
        whenever(mockTasksLocalRepository.load()).thenReturn(Result.success(taskDataModel))

        val countdown = CountDownLatch(1)
        useCaseTaskFilter(listOf(TaskType.Medication, TaskType.Nutrition)) {
            assertTrue(it is Result.Success)
            assertTrue(it.get().fromCache)
            assertEquals(2, it.get().taskList.size)
            assertEquals(1, it.get().taskList[0].id)
            assertEquals(4, it.get().taskList[1].id)

            countdown.countDown()
        }

        assertTrue(countdown.await(5, TimeUnit.SECONDS))
    }

    @Test
    fun `Given a taskData with a list of tasks And a filter with zero values When use case is invoked Then is returned a taskData with an empty list of tasks`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val taskDataModel = TaskDataModel(
            lastUpdate = Date(),
            taskList = listOf(
                TaskModel(1, "name 1", "description 1", TaskType.Medication.value),
                TaskModel(2, "name 2", "description 2", TaskType.General.value),
                TaskModel(3, "name 3", "description 3", TaskType.Hydration.value),
                TaskModel(4, "name 4", "description 4", TaskType.Nutrition.value),
            )
        )
        whenever(mockTasksLocalRepository.load()).thenReturn(Result.success(taskDataModel))

        val countdown = CountDownLatch(1)
        useCaseTaskFilter(listOf()) {
            assertTrue(it is Result.Success)
            assertTrue(it.get().fromCache)
            assertEquals(0, it.get().taskList.size)

            countdown.countDown()
        }

        assertTrue(countdown.await(5, TimeUnit.SECONDS))
    }
}