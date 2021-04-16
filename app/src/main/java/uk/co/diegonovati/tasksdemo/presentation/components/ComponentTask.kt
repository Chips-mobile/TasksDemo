package uk.co.diegonovati.tasksdemo.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.component_task.view.*
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.entities.Task
import uk.co.diegonovati.tasksdemo.presentation.extensions.toResId

class ComponentTask
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    fun setTask(task: Task) {
        componentTaskImage.setImageResource(task.type.toResId())
        this.componentTaskName.text = task.name
        componentTaskDescription.text = task.description
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_task, this, true)

        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        rootView.layoutParams = layoutParams
    }
}