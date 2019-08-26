package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InfoRepository @Inject constructor(private val api: ApiService) {

    fun agreement() = api
        .agreement()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun privacy() = api
        .privacy()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun terms() = api
        .terms()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}