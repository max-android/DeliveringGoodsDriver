package com.my_project.deliveringgoods.utilities

import android.os.Handler
import android.os.Looper


class HandlerScheduler(private val runnable: Runnable, private val delay: Long) {

    private val handler = Handler(Looper.getMainLooper())

    fun launch() {
        handler.postDelayed(runnable, delay)
    }

    fun cancel() {
        handler.removeCallbacks(runnable)
    }
}