package uk.co.diegonovati.tasksdemo.presentation.activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import uk.co.diegonovati.tasksdemo.domain.entities.Task
import uk.co.diegonovati.tasksdemo.presentation.components.ComponentTask

class TaskRecyclerAdapter(
    context: Context
) : BaseRecyclerAdapter<Task, ComponentTask>(context, Task::class.java) {

    fun setTaskList(taskList: List<Task>) {
        items.clear()
        taskList.forEach { task -> items.add(task) }
    }

    override fun onBindCell(cell: ComponentTask?, item: Task) {
        cell?.setTask(item)
    }

    override fun onCreateCell(parent: ViewGroup?, viewType: Int): View {
        return ComponentTask(context)
    }
}