package com.superology.inventory.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.superology.inventory.R
import com.superology.inventory.activities.MainActivity
import com.superology.inventory.databases.FirebaseDataService
import kotlinx.android.synthetic.main.fragment_add_element.*

class AddElementFragment : Fragment(R.layout.fragment_add_element) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
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