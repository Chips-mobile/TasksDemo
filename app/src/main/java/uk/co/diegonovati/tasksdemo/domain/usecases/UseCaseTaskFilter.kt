package uk.co.diegonovati.tasksdemo.domain.usecases

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.entities.filterBy
import uk.co.diegonovati.tasksdemo.domain.entities.toTaskData
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksLocalRepository

open class UseCaseTaskFilter(
    private val tasksLocalRepository: ITasksLocalRepository,
): UseCase<TaskData, List<TaskType>>() {

    override suspend fun run(params: List<TaskType>): Result<TaskData, Exception> =
        tasksLocalRepository.load()
            .flatMap { Result.success(it.toTaskData(true)) }
            .flatMap { Result.success(it.filterBy(params)) }
}