package uk.co.diegonovati.tasksdemo.domain.repositories

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel

interface ITasksLocalRepository {
    suspend fun load(): Result<TaskDataModel, Exception>
    suspend fun save(taskDataModel: TaskDataModel): Result<Unit, Exception>
}