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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_element.*

class AddElementFragment : Fragment(R.layout.fragment_add_element) {

    private val TAG = AddElementFragment::class.java.canonicalName
    private val disposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun initViews() {
        (activity as? MainActivity)?.run {
            title = getString(R.string.add_element_action_bar_title)
            fabView?.visibility = View.GONE
        }
        buttonView?.isEnabled = false
        val nameObservable = RxTextView.textChangeEvents(nameLabelView).skip(1).map { it.text() }
        val statusObservable = RxTextView.textChangeEvents(statusView).skip(1).map { it.text() }
        disposable.add(Observable.combineLatest(nameObservable, statusObservable) { name, status -> Pair(name, status) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                buttonView?.isEnabled = it.first.trim().isNotBlank() && it.second.trim().isNotBlank()
            }) { Log.e(TAG, it.message.toString()) })
        buttonView?.setOnClickListener {
            FirebaseDataService.addElement(
                context = context,
                elementName = nameLabelView.text?.trim().toString(),
                elementStatus = statusView.text?.trim().toString()
            )
            activity?.onBackPressed()
        }
    }

    companion object {

        fun getInstance() = AddElementFragment()
    }
}