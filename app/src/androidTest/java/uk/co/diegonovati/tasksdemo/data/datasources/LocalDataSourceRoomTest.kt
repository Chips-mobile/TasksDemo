package uk.co.diegonovati.tasksdemo.data.datasources

import androidx.test.platform.app.InstrumentationRegistry
import com.github.kittinunf.result.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import java.util.*

class LocalDataSourceRoomTest {

    private lateinit var localDataSourceRoom: LocalDataSourceRoom

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        localDataSourceRoom = LocalDataSourceRoom(ProviderDatabase.createDatabase(appContext))
    }

    @Test
    fun testSaveLoad() = runBlocking {
        val taskDataModel = TaskDataModel(
            lastUpdate = Date(),
            taskList = listOf(
                TaskModel(100, "Name 100", "Description 100", "general"),
                TaskModel(101, "Name 101", "Description 101", "medication"),
            )
        )

        localDataSourceRoom.save(taskDataModel)
        val actual = localDataSourceRoom.load()

        assertTrue(actual is Result.Success)
        assertEquals(taskDataModel.lastUpdate.time, actual.get().lastUpdate.time)
        assertEquals(2, actual.get().taskList.size)
        assertEquals(taskDataModel.taskList[0], actual.get().taskList[0])
        assertEquals(taskDataModel.taskList[1], actual.get().taskList[1])
    }
}