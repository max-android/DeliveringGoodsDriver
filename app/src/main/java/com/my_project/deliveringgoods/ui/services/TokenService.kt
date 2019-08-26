package com.my_project.deliveringgoods.ui.services

import CONST
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class TokenService(private val context: Context) {

    val workManager = WorkManager.getInstance(context)

    fun launchService() {

        val constraints = Constraints.Builder()
            .apply { setRequiredNetworkType(NetworkType.CONNECTED) }
            .build()

        val myWorkRequest = PeriodicWorkRequest.Builder(
            TokenWorker::class.java,
            CONST.REPEAT_REFRESH_TOKEN_INTERVAL,
            TimeUnit.MINUTES,
            CONST.FLEX_REFRESH_TOKEN_INTERVAL,
            TimeUnit.MINUTES
        ).apply {
            setConstraints(constraints)
        }.build()
        workManager.enqueue(myWorkRequest)
    }

}