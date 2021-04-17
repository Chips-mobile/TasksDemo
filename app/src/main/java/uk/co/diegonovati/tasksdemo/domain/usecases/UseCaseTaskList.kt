package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.entities.filterBy
import uk.co.diegonovati.tasksdemo.domain.entities.toTaskData
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksLocalRepository
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksRemoteRepository
import java.util.*

open class UseCaseTaskList(
    private val tasksLocalRepository: ITasksLocalRepository,
    private val tasksRemoteRepository: ITasksRemoteRepository,
): UseCase<TaskData, List<TaskType>>() {

    override suspend fun run(params: List<TaskType>): Result<TaskData, Exception> =
        tasksRemoteRepository.list().fold({
            val taskDataModel = TaskDataModel(
                lastUpdate = Date(),
                taskList = it,
            )
            tasksLocalRepository.save(taskDataModel)
            Result.success(taskDataModel.toTaskData(fromCache = false).filterBy(params))
        }){
            tasksLocalRepository.load().flatMap { Result.success(it.toTaskData(fromCache = true).filterBy(params)) }
        }
}