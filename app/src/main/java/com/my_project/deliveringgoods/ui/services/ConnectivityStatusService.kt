package com.my_project.deliveringgoods.ui.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import com.my_project.deliveringgoods.ui.receivers.NetworkBroadcastReceiver


class ConnectivityStatusService : Service() {

    companion object {
        fun bindService(context: Context, serviceConnection: ServiceConnection): Boolean {
            val intent = Intent(context, ConnectivityStatusService::class.java)
            return context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private var mStatusListener: OnConnectionStatusChangedListener? = null
    private lateinit var mNetworkReceiver: NetworkBroadcastReceiver

    override fun onBind(intent: Intent?): IBinder? {
        startBroadcasting()
        return LocalBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopBroadcasting()
        return false
    }

    private fun startBroadcasting() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mNetworkReceiver = NetworkBroadcastReceiver(applicationContext)
        applicationContext.registerReceiver(mNetworkReceiver, intentFilter)
    }

    private fun stopBroadcasting() {
        applicationContext.unregisterReceiver(mNetworkReceiver)
    }

    fun setOnConnectionStatusChangedListener(statusListener: OnConnectionStatusChangedListener?) {
        this.mStatusListener = statusListener
        mNetworkReceiver.setOnConnectionStatusChangedListener(statusListener)
    }

    interface OnConnectionStatusChangedListener {
        fun onConnectionStatusChanged(isNetworkAvailable: Boolean)
    }

    inner class LocalBinder : Binder() {
        fun getService(): ConnectivityStatusService {
            return this@ConnectivityStatusService
        }
    }
}