package com.my_project.deliveringgoods.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order


class OrderMarker(val order: Order) : ClusterItem {

    override fun getSnippet(): String = order.address

    override fun getTitle(): String = order.price.toString()

    override fun getPosition(): LatLng = LatLng(order.latitude, order.longitude)

    fun getIcon(): BitmapDescriptor = if (order.url != null) {
        BitmapDescriptorFactory.fromBitmap(decodeBitmap(order.url))
    } else {
        if (order.myOrder) BitmapDescriptorFactory.fromResource(R.mipmap.my_destination)
        else BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
    }

    private fun decodeBitmap(image: String): Bitmap {
        val decodedString = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}