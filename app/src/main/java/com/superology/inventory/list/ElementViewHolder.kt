package com.superology.inventory.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.superology.inventory.R
import org.joda.time.DateTime

class ElementViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val nameView = item.findViewById<TextView?>(R.id.nameView)
    private val dateTimeView = item.findViewById<TextView?>(R.id.dateTimeView)
    private val statusView = item.findViewById<TextView?>(R.id.statusView)

    fun bind(
        key: String,
        name: String,
        status: String,
        expirationDateTime: DateTime?,
        mode: RecyclerAdapter.ModeType,
        itemActionListener: ListItemActionListener
    ) {
        nameView?.text = name
        statusView?.text = status
        dateTimeView?.visibility = if (expirationDateTime == null) View.GONE else View.VISIBLE
        dateTimeView?.text = when {
            expirationDateTime == DateTime(0) -> dateTimeView.context.getString(R.string.status_duration_all_day)
            expirationDateTime?.isAfter(DateTime(0)) ?: false -> expirationDateTime?.toString("dd/mm/yyyy hh:MM")
            else -> ""
        }
        itemView.setOnClickListener {
            itemActionListener.onClick(key, name, status, expirationDateTime)
        }
        itemView.isClickable = (mode == RecyclerAdapter.ModeType.EDIT_ON_CLICK)
    }
}