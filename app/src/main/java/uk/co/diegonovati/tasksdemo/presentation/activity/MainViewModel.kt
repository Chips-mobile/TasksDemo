package uk.co.diegonovati.tasksdemo.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.kittinunf.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.diegonovati.tasksdemo.data.models.ConnectionStatus
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.domain.repositories.IChangeStateLister
import uk.co.diegonovati.tasksdemo.domain.usecases.*
import javax.inject.Inject

data class ModelData(
    val data: Result<TaskData, Exception>,
    val reloaded: Boolean
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseTaskList: UseCaseTaskList,
    private val useCaseTaskFilter: UseCaseTaskFilter,
    private val useCaseConnectivityMonitorStart: UseCaseConnectivityMonitorStart,
    private val useCaseConnectivityMonitorStop: UseCaseConnectivityMonitorStop,
    private val useCaseConnectivitySetChangeListener: UseCaseConnectivitySetChangeListener,
): ViewModel() {

    val filterBy = mutableListOf(TaskType.General, TaskType.Hydration, TaskType.Medication, TaskType.Nutrition)

    fun getData(): LiveData<ModelData> = data

    fun getInternetConnectionStatus(): LiveData<ConnectionStatus> = internetConnectionStatus

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

    fun doStartConnectionMonitor() {
        useCaseConnectivityMonitorStart.invoke(UseCase.None()) {
        }
    }

    fun doStopConnectionMonitor() {
        useCaseConnectivityMonitorStop.invoke(UseCase.None()) {
        }
    }

    private val data: MutableLiveData<ModelData> by lazy {
        MutableLiveData<ModelData>().also {
            loadData()
        }
    }

    private val internetConnectionStatus: MutableLiveData<ConnectionStatus> by lazy {
        MutableLiveData<ConnectionStatus>().also {
            monitorConnectionCallback()
        }
    }

    private fun loadData() {
        useCaseTaskList.invoke(filterBy) {
            data.value = ModelData(it, true)
        }
    }

    private fun filterData() {
        useCaseTaskFilter.invoke(filterBy) {
            data.value =  ModelData(it, false)
        }
    }

    private fun monitorConnectionCallback() {
        useCaseConnectivitySetChangeListener.invoke(object : IChangeStateLister{
            override fun stateChanges(connectionStatus: ConnectionStatus) {
                internetConnectionStatus.value = connectionStatus
            }
        }) {}
    }
}