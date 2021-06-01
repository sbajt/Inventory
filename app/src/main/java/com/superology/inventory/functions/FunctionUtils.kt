package com.scorealarm.functions

import com.google.firebase.functions.FirebaseFunctions

object FunctionUtils {

    private val TAG = FunctionUtils::class.java.canonicalName

    private val functions by lazy {
        FirebaseFunctions.getInstance()
    }

    fun important() {

    }

}