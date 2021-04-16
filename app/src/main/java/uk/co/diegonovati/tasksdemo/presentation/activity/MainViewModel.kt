package uk.co.diegonovati.tasksdemo.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.usecases.UseCaseTaskFilter
import uk.co.diegonovati.tasksdemo.domain.usecases.UseCaseTaskList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseTaskList: UseCaseTaskList,
    private val useCaseTaskFilter: UseCaseTaskFilter,
): ViewModel() {

    val filterBy = mutableListOf(TaskType.General, TaskType.Hydration, TaskType.Medication, TaskType.Nutrition)

    fun getData(): LiveData<Result<TaskData, Exception>> = data

    fun doLoad() {
        loadData()
    }

    fun doFilter(taskType: TaskType, active: Boolean) {
        if (active) {
            filterBy.add(taskType)
        } else {
            filterBy.remove(taskType)
        }
        filterData()
    }

    private val data: MutableLiveData<Result<TaskData, Exception>> by lazy {
        MutableLiveData<Result<TaskData, Exception>>().also {
            loadData()
        }
    }

    private fun loadData() {
        println("********** loadData()")
        useCaseTaskList.invoke(filterBy) {
            data.value = it
        }
    }

    private fun filterData() {
        println("********** filterData()")
        useCaseTaskFilter.invoke(filterBy) {
            data.value = it
        }
    }
}