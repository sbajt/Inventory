package com.superology.inventory.databases

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.superology.inventory.R
import com.superology.inventory.models.Element
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject

object FirebaseDataService {

    private val TAG = FirebaseDataService::class.java.canonicalName

    val items = mutableListOf<Element>()
    val dataSubject: ReplaySubject<List<Element>> = ReplaySubject.create()
    private val disposable = CompositeDisposable()
    private val dbRef by lazy {
        FirebaseDatabase.getInstance().reference
    }

    init {
        listenForDbUpdate()
    }

    fun destroy() {
        disposable.dispose()
    }

    fun refreshData() {
        dataSubject.onNext(items)
    }

    fun addElement(
        context: Context?,
        elementName: String,
        elementStatus: String,
    ) {
        dbRef.child(if (items.isNullOrEmpty()) "0" else items.maxOf { it.key } + 1).setValue("$elementName, $elementStatus")
            .addOnCompleteListener { Log.d(TAG, context?.getString(R.string.firebase_add_element_success) ?: "") }
            .addOnFailureListener { Log.e(TAG, context?.getString(R.string.firebase_add_element_error) ?: "") }
    }

    fun deleteElement(context: Context?, key: String) {
        dbRef.child(key).removeValue()
            .addOnFailureListener { Log.e(TAG, context?.getString(R.string.firebase_remove_element_error) ?: "") }
    }

    fun changeElementStatus(
        context: Context?,
        elementKey: String,
        elementName: String,
        elementStatus: String
    ) {
        dbRef.child(elementKey).setValue("$elementName, $elementStatus")
            .addOnFailureListener { Log.e(TAG, context?.getString(R.string.firebase_change_element_error) ?: "") }
    }

    private fun listenForDbUpdate() {
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map {
                    val tokens = it.value.toString().split(',').map { it.trim() }
                    Element(
                        key = it.key ?: "",
                        name = tokens[0],
                        status = tokens[1]
                    )
                }
                onDataFetch(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message, error.toException())
            }
        })
    }

    private fun onDataFetch(records: List<Element>) {
        items.clear()
        items.addAll(records)
        dataSubject.onNext(records)
    }
}