package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TypesShift(
    @SerializedName("type")
    @Expose
    val type: String
)
