package com.my_project.deliveringgoods.ui.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.my_project.deliveringgoods.App
import com.my_project.deliveringgoods.data.entities.RegistrationResponse
import com.my_project.deliveringgoods.data.repository.WorkerRepository
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject


class TokenWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var wRepository: WorkerRepository

    init {
        App.appComponent.inject(this)
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val response: Response<RegistrationResponse> = wRepository.refreshToken()
            if (response.isSuccessful) {
                response.body()?.token?.let {
                    wRepository.updateToken(it)
                }
            } else {
                Result.failure()
            }
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }
}