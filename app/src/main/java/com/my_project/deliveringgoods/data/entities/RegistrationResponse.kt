package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class RegistrationResponse(
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("is_new_user")
    @Expose
    val isNewUser:Boolean
)
