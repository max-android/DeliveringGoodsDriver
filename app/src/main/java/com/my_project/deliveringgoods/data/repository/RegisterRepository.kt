package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.data.network.ApiService
import javax.inject.Inject


class RegisterRepository @Inject constructor(
    private val api: ApiService,
    private val dataHolder: UserDataHolder
) {

    suspend fun register(username: String, mail: String, password: String) = api
        .register(username, mail, password)

    fun user(): UserDataHolder = dataHolder
}