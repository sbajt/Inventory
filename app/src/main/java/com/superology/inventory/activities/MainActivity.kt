package com.superology.inventory.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.superology.inventory.R
import com.superology.inventory.databases.FirebaseDataService
import com.superology.inventory.fragments.ListFragment
import com.superology.inventory.fragments.NoDataFragment
import com.superology.inventory.models.Element
import com.superology.inventory.notifications.NotificationUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.canonicalName
    private val disposable = CompositeDisposable()

    private var isInstanceStateSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null)
            isInstanceStateSaved = true
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
        isInstanceStateSaved = false
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseDataService.destroy()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    private fun observeData() {
        FirebaseDataService.dataSubject
            .subscribeOn(Schedulers.io())
            .filter { !isInstanceStateSaved }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setupLayout) { Log.e(TAG, it.message.toString(), it) }
    }

    private fun setupLayout(elements: List<Element>?) {
        elements?.run {
            if (elements.isEmpty())
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        NoDataFragment.getInstance(),
                        getString(R.string.tag_fragment_no_data)
                    )
                    .commitAllowingStateLoss()
            else
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        ListFragment.getInstance(),
                        getString(R.string.tag_fragment_list)
                    )
                    .commitAllowingStateLoss()
        }
    }

    private fun initNotification() {
        NotificationUtils.init()
        NotificationUtils.createChannel(this)
        NotificationUtils.createTopic(this)
    }

    companion object {

        const val KEY_TAB_INDEX = "tabIndex"
    }
}

