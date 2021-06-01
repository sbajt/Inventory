package com.superology.inventory.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.superology.inventory.R

class GroupHeaderViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val wcGroup by lazy {
        item.findViewById<TextView>(R.id.wcGroupNameView)
    }

    fun bind(name: String) {
        wcGroup?.text = if (name.subSequence(0, 3).any { it.isDigit() })
            name.substring(3)
        else name
    }

}