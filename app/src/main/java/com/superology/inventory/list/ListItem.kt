package com.superology.inventory.list

import org.joda.time.DateTime


data class ListItem(
    val index: Int,
    val itemType: RecyclerAdapter.ItemType,
    val statusName: String = "",
    val status: String = "",
    val expirationDateTime: DateTime? = DateTime(0)
)