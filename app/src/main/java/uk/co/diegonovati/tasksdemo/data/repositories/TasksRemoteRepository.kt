package uk.co.diegonovati.tasksdemo.data.repositories

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.data.datasources.IRemoteDataSource
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksRemoteRepository

class TasksRemoteRepository(
    private val remoteDataSource: IRemoteDataSource,
): ITasksRemoteRepository {
    override suspend fun list(): Result<List<TaskModel>, Exception> = remoteDataSource.list()
}