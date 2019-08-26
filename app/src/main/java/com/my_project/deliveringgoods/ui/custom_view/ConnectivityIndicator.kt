package com.my_project.deliveringgoods.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.my_project.deliveringgoods.R

class ConnectivityIndicator : LinearLayoutCompat {

    var mConnectivityIndicator: AppCompatTextView? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.connectivity_indicator, this)
        mConnectivityIndicator = view.findViewById(R.id.connectivityTextView)
    }

    fun updateStatus(isNetworkAvailable: Boolean) {

    }
}