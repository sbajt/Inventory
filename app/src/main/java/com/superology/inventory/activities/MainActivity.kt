package com.superology.inventory.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.superology.inventory.R
import com.superology.inventory.databases.FirebaseDataService
import com.superology.inventory.fragments.ListFragment
import com.superology.inventory.notifications.NotificationUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.canonicalName
    private val disposable = CompositeDisposable()

    private var isInstanceStateSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showFragments()
        if (savedInstanceState != null)
            isInstanceStateSaved = true
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

    fun getTutorialShowFlag(): Boolean =
        getPreferences(MODE_PRIVATE).getBoolean(PREF_KEY_TUTORIAL, true)


    fun setTutorialShownFlag() {
        getPreferences(MODE_PRIVATE).edit()
            .putBoolean(PREF_KEY_TUTORIAL, false)
            .apply()
    }

    private fun showFragments() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ListFragment.getInstance(),
                getString(R.string.tag_fragment_list)
            )
            .commitAllowingStateLoss()
    }

    private fun initNotification() {
        NotificationUtils.init()
        NotificationUtils.createChannel(this)
        NotificationUtils.createTopic(this)
    }

    companion object {

        const val KEY_TAB_INDEX = "tabIndex"
        const val PREF_KEY_TUTORIAL = "tutorial"
    }
}

