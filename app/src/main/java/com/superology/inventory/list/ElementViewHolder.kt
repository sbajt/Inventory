package com.superology.inventory.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.superology.inventory.R

class ElementViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val nameView = item.findViewById<TextView?>(R.id.nameLabelView)
    private val statusView = item.findViewById<TextView?>(R.id.statusView)

    fun bind(
        key: String,
        name: String,
        status: String,
        mode: RecyclerAdapter.ModeType,
        itemActionListener: ListItemActionListener
    ) {
        nameView?.text = name
        statusView?.text = status
        itemView.setOnClickListener {
            itemActionListener.onClick(key, name, status)
        }
        itemView.setOnLongClickListener {
            itemActionListener.onLongPress()
            true
        }
        itemView.isClickable = (mode == RecyclerAdapter.ModeType.EDIT_ON_CLICK)
    }
}