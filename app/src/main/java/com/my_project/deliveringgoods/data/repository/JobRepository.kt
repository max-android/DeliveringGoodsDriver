package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.entities.InfoResponse
import com.my_project.deliveringgoods.data.network.ApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class JobRepository @Inject constructor(private val api: ApiService) {

    fun sendInfoRequest(code: Int): Single<InfoResponse> =
        api.sendInfoRequest(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}