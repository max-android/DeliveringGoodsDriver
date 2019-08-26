package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.entities.ResponseResult
import com.my_project.deliveringgoods.data.local_storage.FirebaseHolder
import com.my_project.deliveringgoods.data.network.ApiService
import com.my_project.deliveringgoods.data.provider.AppInfoProvider
import retrofit2.Call
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val api: ApiService,
    private val appInfoProvider: AppInfoProvider,
    private val holder: FirebaseHolder
) {

    fun updatePushToken(): Call<ResponseResult> =
        api.updatePushToken(holder.token, appInfoProvider.typePlatform)

    fun token() = holder
}