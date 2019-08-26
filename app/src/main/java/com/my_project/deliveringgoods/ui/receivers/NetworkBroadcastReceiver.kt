package com.my_project.deliveringgoods.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.my_project.deliveringgoods.ui.services.ConnectivityStatusService


class NetworkBroadcastReceiver() : BroadcastReceiver() {

    private var mIsConnected: Boolean = false
    private var mListener: ConnectivityStatusService.OnConnectionStatusChangedListener? = null

    constructor(context: Context?) : this() {
        checkConnectionState(context)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        checkConnectionState(context)
    }

    private fun checkConnectionState(context: Context?) {
        if (context != null) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.detailedState == NetworkInfo.DetailedState.CONNECTED) {
                mIsConnected = true
            } else if (netInfo == null || netInfo.detailedState == NetworkInfo.DetailedState.DISCONNECTED) {
                mIsConnected = false
            }
        }
        notifyListener()
    }

    fun setOnConnectionStatusChangedListener(statusListener: ConnectivityStatusService.OnConnectionStatusChangedListener?) {
        this.mListener = statusListener
        notifyListener()
    }

    private fun notifyListener() {
        mListener?.onConnectionStatusChanged(mIsConnected)
    }
}