package com.my_project.deliveringgoods.data.repository

import android.location.Address
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsRoute
import com.google.maps.model.TravelMode
import com.my_project.deliveringgoods.data.network.ApiService
import com.my_project.deliveringgoods.data.network.GeocoderManager
import com.my_project.deliveringgoods.utilities.ApiConst
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.Instant
import javax.inject.Inject

class DeliveryMapRepository @Inject constructor(
    private val api: ApiService,
    private val geocoder: GeocoderManager
) {

    val geoApiContext: GeoApiContext = GeoApiContext.Builder().apiKey(ApiConst.GEO_KEY).build()

    fun requestAddress(position: com.google.android.gms.maps.model.LatLng): Single<List<Address>> {
        return geocoder.requestAddress(position)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun route(
        origin: com.google.maps.model.LatLng,
        destination: com.google.maps.model.LatLng,
        waypoints: List<com.google.maps.model.LatLng> = emptyList()
    ): Single<DirectionsRoute> =
        Single.fromCallable {
            DirectionsApi.newRequest(geoApiContext)
                .origin(origin)
                .destination(destination)
                .waypoints(*waypoints.toTypedArray())
                .departureTime(Instant.now())
                .mode(TravelMode.DRIVING)
                .await()
                .routes[0]
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    suspend fun orders() = api.orders()

}

