package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class InfoResponse(
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("content")
    @Expose
    val contentInfo: String
)
