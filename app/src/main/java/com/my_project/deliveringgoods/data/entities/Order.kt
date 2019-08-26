package com.my_project.deliveringgoods.data.entities

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Order(
    @SerializedName("order_id")
    @Expose
    val id: Int,
    @SerializedName("startTime")
    @Expose
    val startTime: String,
    @SerializedName("endTime")
    @Expose
    val endTime: String,
    @SerializedName("address")
    @Expose
    val address: String,
    @SerializedName("price")
    @Expose
    val price: Double,
    @SerializedName("order_description")
    @Expose
    val description: String,
    @SerializedName("lat")
    @Expose
    val latitude: Double,
    @SerializedName("lon")
    @Expose
    val longitude: Double,
    @SerializedName("user_order")
    @Expose
    val myOrder: Boolean,
    @SerializedName("url_image")
    @Expose
    val url:String? = null
) : Parcelable



