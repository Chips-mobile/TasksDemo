package uk.co.diegonovati.tasksdemo.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.component_filter_bar.view.*
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType

typealias OnFilterStatusChanged = (TaskType, Boolean) -> Unit

class ComponentFilterBar
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    fun setFilterActiveList(taskTypeList: List<TaskType>) {
        componentButtonFilterButtonGeneral.setActive(TaskType.General in taskTypeList)
        componentButtonFilterButtonMedication.setActive(TaskType.Medication in taskTypeList)
        componentButtonFilterButtonHydration.setActive(TaskType.Hydration in taskTypeList)
        componentButtonFilterButtonNutrition.setActive(TaskType.Nutrition in taskTypeList)
    }

    fun setOnFilterStatusChanged(onFilterStatusChanged: OnFilterStatusChanged) {
        this.onFilterStatusChanged = onFilterStatusChanged
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_filter_bar, this, true)

        componentButtonFilterButtonGeneral.setOnChangeActive {
            onFilterStatusChanged?.invoke(TaskType.General, it)
        }
        componentButtonFilterButtonMedication.setOnChangeActive {
            onFilterStatusChanged?.invoke(TaskType.Medication, it)
        }
        componentButtonFilterButtonHydration.setOnChangeActive {
            onFilterStatusChanged?.invoke(TaskType.Hydration, it)
        }
        componentButtonFilterButtonNutrition.setOnChangeActive {
            onFilterStatusChanged?.invoke(TaskType.Nutrition, it)
        }
    }

    private var onFilterStatusChanged: OnFilterStatusChanged? = null
}
