package com.superology.inventory.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.superology.inventory.R
import com.superology.inventory.models.Element
import com.superology.list.ElementViewHolder

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
                        R.layout.item_status,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ElementViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_status,
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
                    items[position].index.toString(),
                    items[position].statusName,
                    items[position].status,
                    items[position].expirationDateTime,
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
        items.addAll(elementList.mapIndexed { index, status ->
            ListItem(
                index = index,
                itemType = ItemType.ITEM,
                statusName = status.name,
                status = status.status,
                expirationDateTime = status.expirationDateTime
            )
        })
        notifyDataSetChanged()
    }
}