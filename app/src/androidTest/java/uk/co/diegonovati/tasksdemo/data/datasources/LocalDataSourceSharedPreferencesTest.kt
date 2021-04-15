package uk.co.diegonovati.tasksdemo.data.datasources

import androidx.test.platform.app.InstrumentationRegistry
import com.github.kittinunf.result.Result
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import java.util.*

class LocalDataSourceSharedPreferencesTest {

    private lateinit var localDataSourceSharedPreferences: LocalDataSourceSharedPreferences

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val taskDataModel = TaskDataModel(
        lastUpdate = Date(),
        taskList = listOf(
            TaskModel(100, "Name 100", "Description 100", "general"),
            TaskModel(101, "Name 101", "Description 101", "medication"),
        )
    )

    @Before
    fun setUp() {
        localDataSourceSharedPreferences = LocalDataSourceSharedPreferences(appContext)
    }

    @After
    fun tearDown() {
        localDataSourceSharedPreferences.clear()
    }

    @Test
    fun testSaveLoad() = runBlocking {
        localDataSourceSharedPreferences.save(taskDataModel)
        val actual = localDataSourceSharedPreferences.load()

        TestCase.assertTrue(actual is Result.Success)
        TestCase.assertEquals(
            taskDataModel.lastUpdate.toString(),
            actual.get().lastUpdate.toString()
        )
        TestCase.assertEquals(taskDataModel.taskList, actual.get().taskList)
    }

    @Test
    fun testLoad() = runBlocking {
        val actual = localDataSourceSharedPreferences.load()

        TestCase.assertTrue(actual is Result.Failure)
    }

    @Test
    fun testClear() = runBlocking {
        localDataSourceSharedPreferences.save(taskDataModel)

        var actual = localDataSourceSharedPreferences.load()

        TestCase.assertTrue(actual is Result.Success)

        localDataSourceSharedPreferences.clear()

        actual = localDataSourceSharedPreferences.load()

        TestCase.assertTrue(actual is Result.Failure)
    }
}