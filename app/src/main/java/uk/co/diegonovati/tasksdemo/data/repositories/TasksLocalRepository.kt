package uk.co.diegonovati.tasksdemo.data.repositories

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.data.datasources.ILocalDataSource
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksLocalRepository

class TasksLocalRepository(
    private val localDataSource: ILocalDataSource
): ITasksLocalRepository {

    override suspend fun load(): Result<TaskDataModel, Exception> = localDataSource.load()

    override suspend fun save(taskDataModel: TaskDataModel): Result<Unit, Exception> = localDataSource.save(taskDataModel)
}