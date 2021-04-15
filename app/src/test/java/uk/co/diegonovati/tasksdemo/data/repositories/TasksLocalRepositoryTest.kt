package uk.co.diegonovati.tasksdemo.data.repositories

import com.github.kittinunf.result.Result
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import uk.co.diegonovati.tasksdemo.common.BaseTest
import uk.co.diegonovati.tasksdemo.data.datasources.ILocalDataSource
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import java.util.*

class TasksLocalRepositoryTest: BaseTest() {

    @Mock
    private lateinit var mockLocalDataSource: ILocalDataSource

    private lateinit var tasksLocalRepository: TasksLocalRepository

    private val taskDataModel = TaskDataModel(
        lastUpdate = Date(),
        taskList = listOf(
            TaskModel(1, "Name 1", "Description 1", "general"),
        )
    )

    @Before
    fun setUp() {
        tasksLocalRepository = TasksLocalRepository(mockLocalDataSource)
    }

    @Test
    fun testLoadReturningSuccess() = runBlocking {
        whenever(mockLocalDataSource.load()).thenReturn(Result.success(taskDataModel))

        val actual = tasksLocalRepository.load()

        assertTrue(actual is Result.Success)
    }

    @Test
    fun testLoadReturningFailure() = runBlocking {
        whenever(mockLocalDataSource.load()).thenReturn(Result.error(Exception()))

        val actual = tasksLocalRepository.load()

        assertTrue(actual is Result.Failure)
    }

    @Test
    fun testSaveReturningSuccess() = runBlocking {
        whenever(mockLocalDataSource.save(any())).thenReturn(Result.success(Unit))

        val actual = tasksLocalRepository.save(taskDataModel)

        assertTrue(actual is Result.Success)
    }

    @Test
    fun testSaveReturningFailiure() = runBlocking {
        whenever(mockLocalDataSource.save(any())).thenReturn(Result.error(Exception()))

        val actual = tasksLocalRepository.save(taskDataModel)

        assertTrue(actual is Result.Failure)
    }
}