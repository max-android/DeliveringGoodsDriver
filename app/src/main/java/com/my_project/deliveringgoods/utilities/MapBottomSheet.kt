package com.my_project.deliveringgoods.utilities

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order


class MapBottomSheet(val context: Context) {

    private val dialog = MaterialDialog(context, BottomSheet()).apply {
        customView(R.layout.map_bottom_sheet, null, true, true, false, true)
        cornerRadius(16f)
    }

    fun show(order: Order, action: () -> Unit) {
        dialog.getCustomView().apply {
            findViewById<AppCompatTextView>(R.id.addressBsTextView).text = order.address
            findViewById<AppCompatTextView>(R.id.timeBsTextView).text =
                order.startTime.simpleTimeFormat + "-" + order.endTime.simpleTimeFormat
            findViewById<AppCompatTextView>(R.id.priceBsTextView).text =
                context.getString(R.string.price_order, order.price.toString())
            findViewById<AppCompatTextView>(R.id.descriptionBsTextView).text = order.description
            findViewById<AppCompatButton>(R.id.directionBsButton).setOnClickListener {
                action()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}