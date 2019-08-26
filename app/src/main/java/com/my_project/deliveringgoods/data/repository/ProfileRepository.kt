package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.file_storage.InternalStorage
import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.data.network.ApiService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ProfileRepository @Inject constructor(
    private val api: ApiService,
    private val dataHolder: UserDataHolder,
    private val storage: InternalStorage
) {

    fun logout() = api
        .logout(dataHolder.nameUser)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun user(): UserDataHolder = dataHolder

    fun writeProfileImage(filename: String, fileContents: String) =
        Completable.fromAction {
            storage.writeFile(filename, fileContents)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun readProfileImage(filename: String) =
        Single.fromCallable { storage.readFile(filename) ?: InternalStorage.EMPTY_PROFILE }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun checkExistProfileImage() = storage.checkExistFile()

}