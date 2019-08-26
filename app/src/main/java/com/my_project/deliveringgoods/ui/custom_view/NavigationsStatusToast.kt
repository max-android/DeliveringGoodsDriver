package com.my_project.deliveringgoods.ui.custom_view

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.my_project.deliveringgoods.R


class NavigationsStatusToast(private val context: Context) : Toast(context) {

    private var toastText: TextView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.toast_gps_layout, null)
        val toastImage = rootView.findViewById<ImageView>(R.id.imgIcon)
        toastImage.setImageResource(R.drawable.ic_warning)
        toastText = rootView.findViewById(R.id.txtMassage)
        this.view = rootView
        this.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
        this.duration = Toast.LENGTH_LONG
    }

    fun makeText(text: CharSequence): NavigationsStatusToast {
        val result = NavigationsStatusToast(context)
        result.toastText.text = text.toString()
        return result
    }

    fun makeText(text: CharSequence, duration: Int): NavigationsStatusToast {
        val result = NavigationsStatusToast(context)
        result.duration = duration
        result.toastText.text = text.toString()
        return result
    }

    @Throws(Resources.NotFoundException::class)
    fun makeText(resId: Int, duration: Int): Toast {
        return makeText(context, context.resources.getText(resId), duration)
    }
}