package uk.co.diegonovati.tasksdemo.domain.repositories

import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.data.models.TaskModel

interface ITasksRemoteRepository {
    suspend fun list(): Result<List<TaskModel>, Exception>
}