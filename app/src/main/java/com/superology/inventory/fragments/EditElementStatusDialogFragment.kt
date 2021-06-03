package com.superology.inventory.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import com.superology.inventory.R
import com.superology.inventory.databases.FirebaseDataService
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.dialog_edit_status.*

class EditElementStatusDialogFragment : DialogFragment() {

    private var newState = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
            .setCancelable(true)
            .setView(R.layout.dialog_edit_status)
            .setTitle(R.string.dialog_edit_status_title)
            .setPositiveButton(R.string.dialog_edit_status_button_confirm) { _, _ ->
                if (newState.isNotBlank())
                    FirebaseDataService.changeElementStatus(context,
                        arguments?.getString(ARGS_ELEMENT_KEY_KEY) ?: "",
                        arguments?.getString(ARGS_ELEMENT_NAME_KEY) ?: "",
                        newState)
            }
            .setNegativeButton(R.string.dialog_edit_status_button_cancel) { dialog, _ -> dialog?.dismiss() }
            .show()
        initViews(dialog)

        return dialog
    }

    private fun initViews(dialog: Dialog) {
        dialog.run {
            nameView?.text = arguments?.getString(ARGS_ELEMENT_NAME_KEY)
            oldStatusView?.text = arguments?.getString(ARGS_ELEMENT_STATUS_KEY)
            RxTextView.textChangeEvents(newStatusInputView)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    newState = it.text().toString()
                }) { Log.e(TAG, it.message, it) }
        }
    }

    companion object {

        val TAG = EditElementStatusDialogFragment::class.java.canonicalName
        val ARGS_ELEMENT_KEY_KEY = "argsElementKeyKey"
        val ARGS_ELEMENT_NAME_KEY = "argsNameKey"
        val ARGS_ELEMENT_STATUS_KEY = "argsStatusKey"

        fun getInstance(key: String, name: String, oldStatus: String) = EditElementStatusDialogFragment().also {
            it.arguments = Bundle().also {
                it.putString(ARGS_ELEMENT_KEY_KEY, key)
                it.putString(ARGS_ELEMENT_NAME_KEY, name)
                it.putString(ARGS_ELEMENT_STATUS_KEY, oldStatus)
            }
        }
    }
}