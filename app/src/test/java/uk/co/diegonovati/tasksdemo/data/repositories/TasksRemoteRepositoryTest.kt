package uk.co.diegonovati.tasksdemo.data.repositories

import com.github.kittinunf.result.Result
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import uk.co.diegonovati.tasksdemo.common.BaseTest
import uk.co.diegonovati.tasksdemo.data.datasources.IRemoteDataSource
import uk.co.diegonovati.tasksdemo.data.models.TaskModel

class TasksRemoteRepositoryTest : BaseTest() {

    @Mock
    private lateinit var mockRemoteDataSource: IRemoteDataSource

    private lateinit var tasksRemoteRepository: TasksRemoteRepository

    @Before
    fun setUp() {
        tasksRemoteRepository = TasksRemoteRepository(mockRemoteDataSource)
    }

    @Test
    fun testListSuccessful() = runBlocking<Unit> {
        val taskModelList = listOf(TaskModel(1, "name 1", "description 1", "general"))

        whenever(mockRemoteDataSource.list()).thenReturn(Result.success(taskModelList))

        val actual = tasksRemoteRepository.list()

        assertTrue(actual is Result.Success)
        verify(mockRemoteDataSource).list()
    }

    @Test
    fun testListFailure() = runBlocking<Unit> {
        whenever(mockRemoteDataSource.list()).thenReturn(Result.error(Exception()))

        val actual = tasksRemoteRepository.list()

        assertTrue(actual is Result.Failure)
        verify(mockRemoteDataSource).list()
    }
}