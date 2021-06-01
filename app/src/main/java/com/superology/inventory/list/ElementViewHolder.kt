package com.superology.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.superology.inventory.R
import com.superology.inventory.list.ListItemActionListener
import com.superology.inventory.list.RecyclerAdapter
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
        dateTimeView?.text = when {
            expirationDateTime == null -> dateTimeView.context.getString(R.string.status_duration_all_day)
            expirationDateTime.millis > 0 -> expirationDateTime.toString("dd/mm/yyyy hh:MM")
            else -> ""
        }
        itemView.setOnClickListener{
            itemActionListener.onClick(key, name, status, expirationDateTime)
        }
        itemView.isClickable = (mode == RecyclerAdapter.ModeType.EDIT_ON_CLICK)
    }
}