package com.my_project.deliveringgoods.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.tasks.Task
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MapLocationManager(val context: Context, private val locationCallback: LocationCallback) {

    private var locationRequest: LocationRequest = LocationRequest()
    private var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var task: Task<LocationSettingsResponse>
    var placeDetectionClient: PlaceDetectionClient

    init {
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = TimeUnit.SECONDS.toMillis(30L)
        }
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
        val settingsClient = LocationServices.getSettingsClient(context)
        task = settingsClient.checkLocationSettings(locationSettingsRequest)
        placeDetectionClient = Places.getPlaceDetectionClient(context)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun showPlaceUserLocation() {
        placeDetectionClient.getCurrentPlace(null).addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val likelyPlaces: PlaceLikelihoodBufferResponse = task.result!!
                likelyPlaces.forEachIndexed { index, placeLikelihood ->
                    if (index > 5) {
                        return@forEachIndexed
                    } else {
                        Timber.tag("--MAP").d(placeLikelihood.place.name.toString())
                        Timber.tag("--MAP").d(placeLikelihood.place.locale.toString())
                    }
                }
                likelyPlaces.release()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun lastLocation() {
        locationClient.lastLocation
            .addOnSuccessListener { location -> {} }
            .addOnFailureListener { e -> {} }
    }
}