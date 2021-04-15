package uk.co.diegonovati.tasksdemo.domain.entities

import uk.co.diegonovati.tasksdemo.data.models.TaskDataModel
import uk.co.diegonovati.tasksdemo.data.models.TaskModel
import java.util.*

enum class TaskType(val value: String) {
    General("general"),
    Hydration("hydration"),
    Medication("medication"),
    Nutrition("nutrition"),
}

fun String.toTaskType(): TaskType = when (this) {
    TaskType.General.value -> TaskType.General
    TaskType.Hydration.value -> TaskType.Hydration
    TaskType.Medication.value -> TaskType.Medication
    TaskType.Nutrition.value -> TaskType.Nutrition
    else -> TaskType.General
}

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val type: TaskType
)

fun TaskModel.toTask(): Task = Task(
    id = this.id,
    name = this.name,
    description = this.description,
    type = this.type.toTaskType(),
)

fun List<TaskModel>.toTaskList(): List<Task> = this.map { it.toTask() }

data class TaskData(
    val fromCache: Boolean,
    val lastUpdate: Date,
    val taskList: List<Task>,
)

fun TaskDataModel.toTaskData(fromCache: Boolean): TaskData = TaskData(
    fromCache = fromCache,
    lastUpdate = this.lastUpdate,
    taskList = this.taskList.toTaskList()
)

fun List<Task>.filterBy(filters: List<TaskType>): List<Task> = this.filter { it.type in filters }

fun TaskData.filterBy(filters: List<TaskType>): TaskData = this.copy(taskList = this.taskList.filterBy(filters))