package com.my_project.deliveringgoods.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TermsDelivery (
    @SerializedName("terms_delivery")
    @Expose
    val terms: String
)
