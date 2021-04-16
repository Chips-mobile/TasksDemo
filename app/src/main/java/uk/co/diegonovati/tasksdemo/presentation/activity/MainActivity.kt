package uk.co.diegonovati.tasksdemo.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.data.models.ConnectionStatus

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var taskRecyclerAdapter: TaskRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        componentFilterBar.setFilterActiveList(mainViewModel.filterBy)
        componentFilterBar.setOnFilterStatusChanged { taskType, active ->
            mainViewModel.doFilter(taskType, active)
        }

        swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.doLoad()
        }

        mainViewModel.getData().observe(this, {
            swipeRefreshLayout.isRefreshing = false
            it.data.fold({ taskData ->
                taskRecyclerAdapter.setTaskList(taskData.taskList)
                if (it.reloaded) {
                    if (taskData.fromCache) {
                        componentBanner.showDisconnected(taskData.lastUpdate)
                    } else {
                        componentBanner.hide()
                    }
                }
            }) {
                componentBanner.showError(getString(R.string.errorNoData))
            }
        })

        mainViewModel.getInternetConnectionStatus().observe(this, {
            componentTitleBar.setDeviceOnline(it == ConnectionStatus.Connected)
        })
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.doStartConnectionMonitor()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.doStopConnectionMonitor()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        mainRecyclerView.layoutManager = linearLayoutManager
        mainRecyclerView.addItemDecoration(
            DividerItemDecoration(
                mainRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        taskRecyclerAdapter = TaskRecyclerAdapter(this)
        mainRecyclerView.adapter = taskRecyclerAdapter
    }
}