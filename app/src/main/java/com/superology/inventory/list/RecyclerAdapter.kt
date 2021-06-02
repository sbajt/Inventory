package com.superology.inventory.list

import android.util.Log
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

    private val TAG = RecyclerAdapter::class.java.canonicalName

    var isDeleteItemUndoed = false

    var mode = ModeType.READ_ONLY
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val items = mutableListOf<ListItem>()

    private var tempItemData = Pair<Int, ListItem?>(-1, null)

    init {
        setListItems(elementList)
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

    fun setListItems(elementList: List<Element>) {
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

    fun removeListItem(position: Int) {
        Log.d(TAG, position.toString())
        tempItemData = Pair(position, items[position])
        isDeleteItemUndoed = false
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun undoDeleteItem(position: Int) {
        when {
            tempItemData.first > -1 && tempItemData.second != null -> {
                if (items.size > position)
                    items.add(tempItemData.second!!)
                else
                    items.add(tempItemData.first, tempItemData.second!!)
                isDeleteItemUndoed = true
            }
        }
        notifyItemInserted(position)
    }
}