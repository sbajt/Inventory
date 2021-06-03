package com.superology.inventory.list

interface ListItemActionListener {

    fun onClick(key: String, name: String, oldStatus: String)

}