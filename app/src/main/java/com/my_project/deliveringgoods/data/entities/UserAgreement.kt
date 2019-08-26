package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserAgreement(
    @SerializedName("user_agreement")
    @Expose
    val agreement: String
)
