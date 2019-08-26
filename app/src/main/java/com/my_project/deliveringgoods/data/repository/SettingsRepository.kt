package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.local_storage.FirebaseHolder
import com.my_project.deliveringgoods.data.network.ApiService
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val api: ApiService,
    private val holder: FirebaseHolder
) {
    fun enabledPush() = holder
}