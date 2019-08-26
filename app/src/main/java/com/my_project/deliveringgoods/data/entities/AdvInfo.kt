package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdvInfo(
    @SerializedName("url_image")
    @Expose
    val image: String,
    @SerializedName("info")
    @Expose
    val info: String
)
