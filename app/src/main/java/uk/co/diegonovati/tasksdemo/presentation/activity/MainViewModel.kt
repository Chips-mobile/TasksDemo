package uk.co.diegonovati.tasksdemo.presentation.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.Result
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.usecases.UseCaseTaskFilter
import uk.co.diegonovati.tasksdemo.domain.usecases.UseCaseTaskList
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val useCaseTaskList: UseCaseTaskList,
    private val useCaseTaskFilter: UseCaseTaskFilter,
): ViewModel() {

    var data: MutableLiveData<Result<TaskData, Exception>> = MutableLiveData()

    val filterBy = listOf(TaskType.General, TaskType.Hydration, TaskType.Medication, TaskType.Nutrition)

    fun doLoad() {
        useCaseTaskList.invoke(filterBy) {
            data.value = it
        }
    }
}