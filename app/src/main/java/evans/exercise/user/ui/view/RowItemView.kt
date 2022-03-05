package evans.exercise.user.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import evans.exercise.user.R

/**
 * Created by evans.lai on 2022/3/5.
 */
class RowItemView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.layout_row, this)

        val icon: ImageView = findViewById(R.id.iv_icon)
        val title: TextView = findViewById(R.id.tv_title)
        val content: TextView = findViewById(R.id.tv_content)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RowItemView)
        icon.apply {
            val drawable = attributes.getDrawable(R.styleable.RowItemView_row_icon)
            drawable?.let {
                setImageDrawable(it)
            }
        }
        title.apply {
            text = attributes.getString(R.styleable.RowItemView_row_title)
        }
        content.apply {
            text = attributes.getString(R.styleable.RowItemView_row_content)
            if (!text.isNullOrEmpty()) visibility = View.VISIBLE
        }

        attributes.recycle()
    }

    fun setRowTitle(title: String?) {
        findViewById<TextView>(R.id.tv_title).text = title
    }

}