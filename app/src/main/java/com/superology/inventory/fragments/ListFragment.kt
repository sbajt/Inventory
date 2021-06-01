package com.superology.inventory.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.scorealarm.functions.FunctionUtils
import com.superology.inventory.R
import com.superology.inventory.activities.MainActivity
import com.superology.inventory.databases.FirebaseDataService
import com.superology.inventory.list.DividerItemDecoration
import com.superology.inventory.list.ListItemActionListener
import com.superology.inventory.list.RecyclerAdapter
import com.superology.inventory.notifications.NotificationUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*
import org.joda.time.DateTime

class ListFragment :
    Fragment(R.layout.fragment_list), ListItemActionListener {

    private val TAG = ListFragment::class.java.canonicalName
    private val disposable = CompositeDisposable()
    private lateinit var adapter: RecyclerAdapter
    private var tabIndex: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabIndex = arguments?.getInt(MainActivity.KEY_TAB_INDEX)
        setHasOptionsMenu(true)
        initRecyclerView()
        initRefreshView()
        observeDataUpdate()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.sendImportant)?.isVisible =
            FirebaseDataService.items.isNullOrEmpty()
                && !NotificationUtils.hasUserSentImportant

        menu.findItem(R.id.editList)?.apply {
            iconTintList =
                ColorStateList.valueOf(context?.getColor(android.R.color.white) ?: Color.WHITE)
            when (adapter.mode) {
                RecyclerAdapter.ModeType.READ_ONLY -> {
                    setIcon(R.drawable.ic_list_read_only_mode)
                }
                RecyclerAdapter.ModeType.EDIT_ON_CLICK -> {
                    setIcon(R.drawable.ic_list_edit_mode)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendImportant -> {
                onImportant()
                true
            }
            R.id.editList -> {
                when (adapter.mode) {
                    RecyclerAdapter.ModeType.READ_ONLY -> adapter.mode =
                        RecyclerAdapter.ModeType.EDIT_ON_CLICK
                    RecyclerAdapter.ModeType.EDIT_ON_CLICK -> adapter.mode =
                        RecyclerAdapter.ModeType.READ_ONLY
                }
                activity?.invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(key: String, name: String, status: String, expirationDateTime: DateTime?) {
        if (adapter.mode == RecyclerAdapter.ModeType.EDIT_ON_CLICK) {
            FirebaseDataService.setElementStatus(
                context = context,
                elementKey = key,
                elementName = name,
                elementStatus = status,
                expirationDateTime = expirationDateTime
            )
        }
    }

    private fun initRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(DividerItemDecoration(context))
        adapter = RecyclerAdapter(
            emptyList(),
            this
        )
        recyclerView?.adapter = adapter
    }

    private fun initRefreshView() {
        refreshView?.isRefreshing = false
        refreshView?.setOnRefreshListener {
            FirebaseDataService.refreshData()
        }
    }

    private fun observeDataUpdate() {
        disposable.add(
            FirebaseDataService.dataSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    refreshView?.isRefreshing = false
                    adapter.initListItems(it)
                    activity?.invalidateOptionsMenu()
                }, {
                    Log.e(TAG, getString(R.string.rx_data_error), it)
                })
        )
    }

    private fun onImportant() {
        FunctionUtils.important()
        NotificationUtils.hasUserSentImportant = true
        activity?.invalidateOptionsMenu()
    }

    companion object {

        fun getInstance() = ListFragment()
    }
}