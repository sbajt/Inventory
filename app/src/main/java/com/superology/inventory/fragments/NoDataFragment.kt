package com.superology.inventory.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.superology.inventory.R
import com.superology.inventory.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class NoDataFragment : Fragment(R.layout.fragment_no_data) {

    init {
        (activity as? MainActivity)?.run {
            fabView?.visibility = View.VISIBLE
        }
    }

    companion object {

        fun getInstance() = NoDataFragment()

    }
}