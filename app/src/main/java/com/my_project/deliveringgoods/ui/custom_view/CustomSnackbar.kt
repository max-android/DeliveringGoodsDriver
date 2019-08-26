package com.my_project.deliveringgoods.ui.custom_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar


class CustomSnackbar(
    parentView: View,
    @LayoutRes layout: Int,
    duration: Int,
    background: Int = -1,
    swipeToDismiss: Boolean = true
) {
    private val snackbar: Snackbar = Snackbar.make(parentView, "", duration)
    val contentView: View

    init {
        val snackbarView = snackbar.view as Snackbar.SnackbarLayout
        if (background != -1) {
            snackbarView.setBackgroundResource(background)
        }
        if (!swipeToDismiss) {
            snackbarView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    snackbarView.viewTreeObserver.removeOnPreDrawListener(this)
                    (snackbarView.layoutParams as CoordinatorLayout.LayoutParams).behavior = null
                    return true
                }
            })
        }
        snackbarView.setPadding(0, 0, 0, 0)
        snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).visibility = View.INVISIBLE
        snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action).visibility =
            View.INVISIBLE
        contentView = LayoutInflater.from(parentView.context).inflate(layout, null)
        snackbarView.addView(contentView, 0)
    }

    fun show() = snackbar.show()

    fun isShown() = snackbar.isShown

    fun dismiss() = snackbar.dismiss()
}
