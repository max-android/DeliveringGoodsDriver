package com.my_project.deliveringgoods.data.repository

import CONST
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.my_project.deliveringgoods.data.file_storage.ExternalStorage
import com.my_project.deliveringgoods.data.network.ApiService
import com.my_project.deliveringgoods.utilities.ApiConst
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class PhotoRepository @Inject constructor(
    private val api: ApiService,
    private val storage: ExternalStorage
) {

    fun writeImageBitmap(bitmap: Bitmap) =
        Completable.fromAction {
            storage.writeImage(CONST.CAR_FILE_NAME, bitmap)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun readImageBitmap(@DrawableRes defaultId: Int) =
        Single.fromCallable { storage.readImage(CONST.CAR_FILE_NAME) ?: storage.bitmapFromVectorDrawable(defaultId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun sendFile() = api
        .sendFile(
            RequestBody.create(MediaType.parse("multipart/form-data"), ApiConst.DESCRIPTION),
            MultipartBody.Part.createFormData(
                ApiConst.PICTURE,
                storage.readImageFile(CONST.CAR_FILE_NAME).name,
                RequestBody.create(MediaType.parse("multipart/form-data"), storage.readImageFile(CONST.CAR_FILE_NAME))
            )
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}