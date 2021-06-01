package com.superology.inventory.list

import org.joda.time.DateTime

interface ListItemActionListener {

    fun onClick(key: String, name: String, status: String, expirationDateTime: DateTime?)

}