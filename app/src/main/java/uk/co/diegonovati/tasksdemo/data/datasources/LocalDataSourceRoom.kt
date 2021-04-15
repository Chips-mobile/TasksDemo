package uk.co.diegonovati.tasksdemo.data.datasources

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.data.models.LastUpdate
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel

class LocalDataSourceRoom(
    private val tasksDemoDatabase: TasksDemoDatabase
): ILocalDataSource {
    override suspend fun load(): Result<TaskDataModel, Exception> =
        try {
            val taskDataModel = TaskDataModel(
                lastUpdate = tasksDemoDatabase.lastUpdateDao().get().lastUpdate,
                taskList = tasksDemoDatabase.taskModelDao().list(),
            )
            Result.success(taskDataModel)
        } catch (e: Exception) {
            Result.error(e)
        }

    override suspend fun save(taskDataModel: TaskDataModel): Result<Unit, Exception> =
        try {
            tasksDemoDatabase.lastUpdateDao().save(LastUpdate(
                id = 1,
                lastUpdate = taskDataModel.lastUpdate
            ))
            tasksDemoDatabase.taskModelDao().clear()
            taskDataModel.taskList.forEach {
                tasksDemoDatabase.taskModelDao().save(it)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
}