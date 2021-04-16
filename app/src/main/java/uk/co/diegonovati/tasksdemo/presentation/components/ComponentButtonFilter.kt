package uk.co.diegonovati.tasksdemo.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.component_button_filter.view.*
import uk.co.diegonovati.tasksdemo.R
import uk.co.diegonovati.tasksdemo.domain.entities.TaskType
import uk.co.diegonovati.tasksdemo.presentation.extensions.toResId

typealias OnChangeActive = (Boolean) -> Unit

class ComponentButtonFilter
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    // The type can be changed programmatically
    fun setTaskType(taskType: TaskType) {
        componentButtonFilterImage.setImageResource(taskType.toResId())
    }

    // The status can be changed programmatically
    fun setActive(active: Boolean) {
        this.currentActive = active

        val resId = if (active) R.drawable.button_active else R.drawable.button_not_active
        this.setBackgroundResource(resId)
    }

    fun setOnChangeActive(onChangeActive: OnChangeActive = {}) {
        this.onChangeActive = onChangeActive
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_button_filter, this, true)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ComponentButtonFilter, 0, 0)
        try {
            val taskType = when (ta.getInteger(R.styleable.ComponentButtonFilter_filterType, 0)) {
                0 -> TaskType.General
                1 -> TaskType.Hydration
                2 -> TaskType.Medication
                3 -> TaskType.Nutrition
                else -> TaskType.General
            }
            setTaskType(taskType)
            val filterActive = ta.getBoolean(R.styleable.ComponentButtonFilter_active, true)
            setActive(filterActive)
        } finally {
            ta.recycle()
        }

        setOnClickListener {
            onChangeActive?.invoke(!currentActive)
            setActive(!currentActive)
        }
    }

    private var currentActive = false
    private var onChangeActive: OnChangeActive? = null
}