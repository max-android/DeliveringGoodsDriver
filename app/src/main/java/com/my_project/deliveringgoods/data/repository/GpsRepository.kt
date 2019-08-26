package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.entities.ResponseResult
import com.my_project.deliveringgoods.data.network.ApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class GpsRepository @Inject constructor(private val api: ApiService) {

    fun sendLocationPoint(lat: String, lng: String): Single<ResponseResult> =
        api.sendLocationPointRequest(lat, lng)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}