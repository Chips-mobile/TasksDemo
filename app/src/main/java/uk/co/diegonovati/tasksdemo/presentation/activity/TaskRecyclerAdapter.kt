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

        println("********** TaskRecyclerAdapter.setTaskList: taskList.size = ${taskList.size}")
        println("********** TaskRecyclerAdapter.setTaskList: items.size = ${items.size()}")
        for (i in 1..(items.size()-1)) {
            println("********** TaskRecyclerAdapter.setTaskList: item = ${items[i]}")
        }
    }

    override fun onBindCell(cell: ComponentTask?, item: Task) {
        println("********** TaskRecyclerAdapter.onBindCell: item = $item")

        cell?.setTask(item)
    }

    override fun onCreateCell(parent: ViewGroup?, viewType: Int): View {
        println("********** TaskRecyclerAdapter.onCreateCell")

        return ComponentTask(context)
    }
    //override fun onCreateCell(parent: ViewGroup?, viewType: Int): View = ComponentTask(context)
}