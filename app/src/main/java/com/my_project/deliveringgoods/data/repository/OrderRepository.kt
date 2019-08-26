package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.network.ApiService
import javax.inject.Inject

 class OrderRepository @Inject constructor(private val api: ApiService) {
 suspend fun acceptOrder(relevantId: String,statusId: Int) = api.acceptOrder(relevantId,statusId)
}