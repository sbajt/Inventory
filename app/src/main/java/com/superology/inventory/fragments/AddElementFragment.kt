package com.superology.inventory.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.widget.RxTextView
import com.superology.inventory.R
import com.superology.inventory.activities.MainActivity
import com.superology.inventory.databases.FirebaseDataService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_add_element.*

class AddElementFragment : Fragment(R.layout.fragment_add_element) {

    private val TAG = AddElementFragment::class.java.canonicalName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        buttonView?.isEnabled = false
        val nameObservable = RxTextView.textChangeEvents(nameView).skip(1).map { it.text() }
        val statusObservable = RxTextView.textChangeEvents(statusView).skip(1).map { it.text() }
        Observable.combineLatest(nameObservable, statusObservable) { name, status -> Pair(name, status) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                buttonView?.isEnabled = it.first.trim().isNotBlank() && it.second.trim().isNotBlank()
            }) { Log.e(TAG, it.localizedMessage) }
        (activity as MainActivity).title = getString(R.string.add_element_action_bar_title)
        buttonView?.setOnClickListener {
            FirebaseDataService.addElement(
                context = context,
                elementName = nameView.text?.trim().toString(),
                elementStatus = statusView.text?.trim().toString()
            )
            activity?.onBackPressed()
        }
    }

    companion object {

        fun getInstance() = AddElementFragment()
    }
}