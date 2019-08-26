package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.data.network.ApiService
import javax.inject.Inject


class WorkerRepository @Inject constructor(
    private val api: ApiService,
    private val dataHolder: UserDataHolder
) {

    suspend fun refreshToken() = api.refreshToken(dataHolder.nameUser)

    fun updateToken(token: String) {
        dataHolder.token = token
    }
}