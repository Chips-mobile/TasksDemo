package uk.co.diegonovati.tasksdemo.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.component_banner.view.*
import uk.co.diegonovati.tasksdemo.R
import java.text.DateFormat
import java.text.MessageFormat
import java.util.*

class ComponentBanner
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    fun hide() {
        this.visibility = View.GONE
    }

    fun showError(errorMessage: String) {
        componentBannerTitle.text = context.getString(R.string.titleError)
        componentBannerTitle.visibility = View.VISIBLE
        componentBannerDescription.text = errorMessage
        componentBannerDescription.visibility = View.VISIBLE

        this.visibility = View.VISIBLE
    }

    fun showDisconnected(lastUpdate: Date?) {
        componentBannerTitle.text = context.getString(R.string.titleDeviceDisconnected)
        componentBannerTitle.visibility = View.VISIBLE

        lastUpdate?.let {
            val dateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(lastUpdate)
            val description = MessageFormat.format(context.getString(R.string.descriptionLastUpdate), dateTime)
            componentBannerDescription.text = description
        }
        componentBannerDescription.visibility = if (lastUpdate != null) View.VISIBLE else View.GONE

        this.visibility = View.VISIBLE
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.component_banner, this, true)
    }
}