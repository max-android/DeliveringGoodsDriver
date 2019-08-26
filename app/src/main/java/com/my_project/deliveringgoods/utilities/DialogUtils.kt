package com.my_project.deliveringgoods.utilities

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.my_project.deliveringgoods.R


class DialogUtils(val context: Context) {

    fun showNoConnect() {
        MaterialDialog(context).show {
            title(R.string.no_connect)
            message(R.string.no_connect_message)
            cornerRadius(16f)
            icon(R.drawable.ic_signal_wifi_off)
            positiveButton(R.string.ok_value) { dialog ->
                dialog.dismiss()
            }
        }
    }

    fun showMessage(title: String, message: String, action: () -> Unit? = { null }) {
        MaterialDialog(context).show {
            title(null, title)
            message(null, message, null)
            cornerRadius(16f)
            positiveButton(R.string.cancel_value) { dialog ->
                dialog.dismiss()
            }
            negativeButton(R.string.ok_value) { dialog ->
                action()
                dialog.dismiss()
            }
        }
    }

    fun showBottomSheet(title: String, message: String, action: () -> Unit? = { null }) {
        MaterialDialog(context, BottomSheet()).show {
            title(null, title)
            message(null, message, null)
            cornerRadius(16f)
            negativeButton(R.string.ok_value) { dialog ->
                action()
                dialog.dismiss()
            }
        }
    }
}