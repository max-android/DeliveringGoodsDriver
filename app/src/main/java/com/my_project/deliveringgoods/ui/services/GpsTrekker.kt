package com.my_project.deliveringgoods.ui.services

import CONST
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.maps.model.LatLng
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.repository.GpsRepository
import com.my_project.deliveringgoods.ui.custom_view.NavigationsStatusToast
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject


class GpsTrekker @Inject constructor(
    private val context: Context,
    private val gpsRepository: GpsRepository
) : GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private lateinit var googleApiClient: GoogleApiClient
    private val compositeDisposable = CompositeDisposable()
    private var locationManager: LocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    private var location: Location? = null

    var checkGPS: Boolean = false
    var checkNetwork: Boolean = false

    init {
        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onLocationChanged(location: Location?) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onConnectionFailed(result: ConnectionResult) {
        NavigationsStatusToast(context).makeText(context.getString(R.string.connection_failed), Toast.LENGTH_LONG).show()
    }

    //1 способ
    @SuppressLint("MissingPermission")
    fun startLocationManager() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                CONST.LOCATION_MANAGER_MIN_TIME,
                CONST.LOCATION_MANAGER_MIN_DISTANCE,
                this
            )
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun startTrekking() {
        buildGoogleApiClient()
        googleApiClient.connect()
    }

    //2 способ
   private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .addConnectionCallbacks(connectionCallback)
            .addOnConnectionFailedListener(this)
            .build()
    }

    @SuppressLint("MissingPermission")
    fun registerRequestUpdate() {
        val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        sendPositionIntoServer(lastLocation)
        val latLng = LatLng(lastLocation?.latitude?:1.0, lastLocation?.longitude?:1.0)
    }

    fun stopTrekking() {
        if (googleApiClient.isConnecting) {
            googleApiClient.disconnect()
        }
    }

    private fun sendPositionIntoServer(location: Location?) {
        compositeDisposable.addAll(gpsRepository.sendLocationPoint(
            location?.latitude.toString(),
            location?.longitude.toString()
        ).subscribe(
                { responce -> Timber.d("---GpsTrekker---$responce.toString()") },
                { error -> Timber.d("---GpsTrekker---$error.toString()") }
            )
        )
    }

    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }

    private val connectionCallback = object : GoogleApiClient.ConnectionCallbacks {
        override fun onConnected(p0: Bundle?) {
            registerRequestUpdate()
        }

        override fun onConnectionSuspended(p0: Int) {
        }
    }
}