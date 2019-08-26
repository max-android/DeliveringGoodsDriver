package com.my_project.deliveringgoods.data.network

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.my_project.deliveringgoods.utilities.ApiConst
import io.reactivex.Single
import java.util.*

class GeocoderManager(val context: Context) {

    val geocoder = Geocoder(context, Locale.getDefault())

    fun requestAddress(position: LatLng): Single<List<Address>> = Single.fromCallable {
        geocoder.getFromLocation(
            position.latitude,
            position.longitude,
            ApiConst.GEOCODER_MAX_RESULT
        )
    }
}