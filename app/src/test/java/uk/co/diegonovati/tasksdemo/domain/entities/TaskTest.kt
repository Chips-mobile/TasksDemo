package uk.co.diegonovati.tasksdemo.domain.entities

import junit.framework.TestCase.assertEquals
import org.junit.Test
import uk.co.diegonovati.tasksdemo.data.models.TaskModel

class TaskTest {

    @Test
    fun testToTaskType() {
        assertEquals(TaskType.General, "general".toTaskType())
        assertEquals(TaskType.Hydration, "hydration".toTaskType())
        assertEquals(TaskType.Medication, "medication".toTaskType())
        assertEquals(TaskType.Nutrition, "nutrition".toTaskType())
        assertEquals(TaskType.General, "any other string value".toTaskType())
    }

    @Test
    fun testTaskModelToTask() {
        val taskModel = TaskModel(
            id = 1234,
            name = "name 1234",
            description = "description 1234",
            type = "hydration"
        )

        val actual = taskModel.toTask()

        assertEquals(taskModel.id, actual.id)
        assertEquals(taskModel.name, actual.name)
        assertEquals(taskModel.description, actual.description)
        assertEquals(taskModel.type, actual.type.value)
    }

    @Test
    fun tesTaskListFilterBy() {
        val list = listOf(
            Task(1, "name 1", "description 1", TaskType.Nutrition),
            Task(2, "name 2", "description 2", TaskType.General),
            Task(3, "name 3", "description 3", TaskType.Hydration),
            Task(4, "name 4", "description 4", TaskType.Medication),
            Task(5, "name 5", "description 5", TaskType.Nutrition),
        )

        val actual = list.filterBy(listOf(TaskType.General, TaskType.Medication))

        assertEquals(2, actual.size)
        assertEquals(2, actual[0].id)
        assertEquals(4, actual[1].id)
    }
}