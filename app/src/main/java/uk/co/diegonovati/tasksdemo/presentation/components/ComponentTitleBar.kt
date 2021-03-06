package uk.co.diegonovati.tasksdemo.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.component_title_bar.view.*
import uk.co.diegonovati.tasksdemo.R

class ComponentTitleBar
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    fun setDeviceOnline(isDeviceOnline: Boolean) {
        deviceOnline.visibility = if (isDeviceOnline) View.VISIBLE else View.GONE
        deviceOffline.visibility = if (isDeviceOnline) View.GONE else View.VISIBLE
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_title_bar, this, true)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ComponentTitleBar, 0, 0)
        try {
            val deviceOnline = ta.getBoolean(R.styleable.ComponentTitleBar_deviceOnline, true)
            setDeviceOnline(deviceOnline)
        } finally {
            ta.recycle()
        }
    }
}
