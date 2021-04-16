package uk.co.diegonovati.tasksdemo.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.entities.TaskData
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        componentFilterBar.setFilterActiveList(mainViewModel.filterBy)

        componentFilterBar.setOnFilterStatusChanged { taskType, active ->
            //mainViewModel.
        }

        swipeRefreshLayout.setOnRefreshListener {
//            val reloading = mainViewModel.refreshOffers() ?: false
//            if (! reloading) {
                swipeRefreshLayout.isRefreshing = false
//            }
        }

        mainViewModel.data.observe(this, Observer<Result<TaskData, Exception>> {
            it.fold({ taskData ->
                taskRecyclerAdapter.setTaskList(taskData.taskList)
                if (taskData.fromCache) {
                    componentBanner.showDisconnected(taskData.lastUpdate)
                }
            }) {
                componentBanner.showError(getString(R.string.errorNoData))
            }
        })

        componentBanner.hide()
        mainViewModel.doLoad()
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

    private lateinit var taskRecyclerAdapter: TaskRecyclerAdapter
}