package com.superology.inventory.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.superology.inventory.R
import com.superology.inventory.databases.FirebaseDataService
import kotlinx.android.synthetic.main.dialog_edit_status.*

class EditElementStatusDialogFragment : DialogFragment() {

    private var inputValue = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context)
            .setCancelable(true)
            .setView(R.layout.dialog_edit_status)
            .setTitle(R.string.dialog_edit_status_title)
            .setPositiveButton(R.string.dialog_edit_status_button_confirm) { _, _ ->
                FirebaseDataService.changeElementStatus(
                    context, arguments?.getString(ARGS_ELEMENT_KEY_KEY) ?: "",
                    inputValue
                )
            }
            .setNegativeButton(R.string.dialog_edit_status_button_cancel) { _, _ -> dialog?.dismiss() }
            .show()
        initViews(dialog)

        return dialog
    }

    private fun initViews(dialog: Dialog) {
        dialog.run {
            nameView?.text = arguments?.getString(ARGS_NAME_KEY)
            oldStatusView?.text = arguments?.getString(ARGS_STATUS_KEY)
            inputValue = newStatusInputView?.text.toString()
        }
    }

    companion object {

        val TAG = EditElementStatusDialogFragment::class.java.canonicalName
        val ARGS_ELEMENT_KEY_KEY = "argsElementKeyKey"
        val ARGS_NAME_KEY = "argsNameKey"
        val ARGS_STATUS_KEY = "argsStatusKey"

        fun getInstance(key: String, name: String, oldStatus: String) = EditElementStatusDialogFragment().also {
            it.arguments = Bundle().also {
                it.putString(ARGS_ELEMENT_KEY_KEY, key)
                it.putString(ARGS_NAME_KEY, name)
                it.putString(ARGS_STATUS_KEY, oldStatus)
            }
        }
    }
}