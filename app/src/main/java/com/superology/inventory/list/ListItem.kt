package com.superology.inventory.list

data class ListItem(
    val index: Int,
    val itemType: RecyclerAdapter.ItemType,
    val statusName: String = "",
    val status: String = ""
)