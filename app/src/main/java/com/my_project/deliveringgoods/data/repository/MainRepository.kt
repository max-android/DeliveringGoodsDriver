package com.my_project.deliveringgoods.data.repository

import com.my_project.deliveringgoods.data.network.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: ApiService)

