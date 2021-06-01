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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment :
    Fragment(R.layout.fragment_list), ListItemActionListener {

    private val TAG = ListFragment::class.java.canonicalName
    private val disposable = CompositeDisposable()
    private lateinit var adapter: RecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initRefreshView()
        initRecyclerView()
        initFab()
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

    override fun onClick(key: String, name: String, status: String) {
        Log.d(TAG, "Open change element status dialog...")
        /*FirebaseDataService.changeElementStatus(
            context = context,
            elementKey = key,
            elementName = name,
            elementStatus = status,
        )*/
    }

    private fun initFab() {
        fabView?.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddElementFragment.getInstance())
                .addToBackStack(getString(R.string.add_element_fragment_tag))
                .commit()
        }
    }

    private fun initRecyclerView() {
        recyclerView?.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context))
            adapter = RecyclerAdapter(emptyList(), this@ListFragment)
        }
        adapter = recyclerView.adapter as RecyclerAdapter
        /*ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (adapter.mode == RecyclerAdapter.ModeType.EDIT_ON_CLICK && direction == ItemTouchHelper.LEFT)
                    FirebaseDataService.deleteElement(context, adapter.getItem(viewHolder.adapterPosition).key)

            }
        }).attachToRecyclerView(recyclerView)*/
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