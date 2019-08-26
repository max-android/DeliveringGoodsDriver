package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.data.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginRepository @Inject constructor(
    private val api: ApiService,
    private val dataHolder: UserDataHolder
) {

    fun login(username: String, password: String) = api
        .login(username, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun user(): UserDataHolder = dataHolder
}