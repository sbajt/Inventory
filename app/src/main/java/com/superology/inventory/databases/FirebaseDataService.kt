package com.superology.inventory.databases

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.superology.inventory.R
import com.superology.inventory.models.Element
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject
import org.joda.time.DateTime

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

    fun setElementStatus(
        context: Context?,
        elementKey: String,
        elementName: String,
        elementStatus: String,
        expirationDateTime: DateTime?
    ) {
        setStatusDb(context, elementKey, elementName, elementStatus, expirationDateTime ?: DateTime(0))
    }

    private fun setStatusDb(
        context: Context?,
        elementKey: String,
        elementName: String,
        elementStatus: String,
        expirationTime: DateTime
    ) {
        dbRef.child(elementKey).setValue("$elementName, $elementStatus, ${expirationTime.toString("dd/mm/yyyy hh:MM")}")
            .addOnCompleteListener { Log.d(TAG, context?.getString(R.string.firebase_data_success) ?: "") }
            .addOnFailureListener { Log.e(TAG, context?.getString(R.string.firebase_data_error) ?: "") }
    }

    private fun listenForDbUpdate() {
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map {
                    val tokens = it.value.toString().split(',')
                    Element(
                        it.key ?: "",
                        tokens[0],
                        tokens[1],
                        if (tokens[2].trim() == "null") null else DateTime.parse(tokens[2].trim())
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