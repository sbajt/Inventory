package com.superology.inventory.list

data class ListItem(
    val itemType: RecyclerAdapter.ItemType,
    val key: String,
    val statusName: String = "",
    val status: String = ""
)