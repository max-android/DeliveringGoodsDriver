package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.network.ApiService
import javax.inject.Inject


class ListOrdersRepository @Inject constructor(private val api: ApiService) {

   suspend fun orders() = api.orders()

}