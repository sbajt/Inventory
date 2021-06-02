package com.superology.inventory.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.superology.inventory.R
import com.superology.inventory.models.Element

class RecyclerAdapter(
    elementList: List<Element>,
    private val itemActionListener: ListItemActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ModeType {
        READ_ONLY,
        EDIT_ON_CLICK
    }

    enum class ItemType {
        ITEM
    }

    var undo = false

    var mode = ModeType.READ_ONLY
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val items = mutableListOf<ListItem>()

    init {
        initListItems(elementList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.ITEM.ordinal -> {
                ElementViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ElementViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_list,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount() =
        items.count()

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].itemType) {
            ItemType.ITEM -> {
                (holder as ElementViewHolder).bind(
                    items[position].key,
                    items[position].statusName,
                    items[position].status,
                    mode,
                    itemActionListener
                )
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun initListItems(elementList: List<Element>) {
        items.clear()
        items.addAll(elementList.map { status ->
            ListItem(
                itemType = ItemType.ITEM,
                key = status.key,
                statusName = status.name,
                status = status.status
            )
        })
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = items[position]
}