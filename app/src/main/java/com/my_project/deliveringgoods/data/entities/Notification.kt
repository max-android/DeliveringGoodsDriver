package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Notification(
    @SerializedName("notification_id")
    @Expose
    val id: Int,
    @SerializedName("url_info")
    @Expose
    val url: String,
    @SerializedName("message")
    @Expose
    val message: String
)
