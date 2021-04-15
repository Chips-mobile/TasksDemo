package uk.co.diegonovati.tasksdemo.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.usecases.UseCaseTaskFilter
import uk.co.diegonovati.tasksdemo.domain.usecases.UseCaseTaskList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var useCaseTaskList: UseCaseTaskList
    @Inject
    lateinit var useCaseTaskFilter: UseCaseTaskFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("********** MainActivity.onCreate: useCaseTaskList = $useCaseTaskList")
        println("********** MainActivity.onCreate: useCaseTaskFilter = $useCaseTaskFilter")

        useCaseTaskList.invoke(listOf()) {
            it.fold({
                println("********** useCaseTaskList: SUCCESSFUL")
            }){
                println("********** useCaseTaskList: FAILURE")
            }
        }
    }
}