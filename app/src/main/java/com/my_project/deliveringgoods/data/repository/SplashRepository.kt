package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import javax.inject.Inject


class SplashRepository @Inject constructor(private val dataHolder: UserDataHolder) {
    fun user(): UserDataHolder = dataHolder
}