package com.my_project.deliveringgoods.utilities

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.my_project.deliveringgoods.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast


fun MapView.showOnGoogleMaps(position: LatLng, title: String) {
    val uri = Uri.parse("geo:0,0?q=${position.latitude},${position.longitude}($title)")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
        context?.startActivity(intent)
    } else {
        context?.longToast(R.string.error_no_maps)
    }
}

fun GoogleMap.showStreetView(position: LatLng, context: Context?) {
    val uri = Uri.parse("google.streetview:cbll=${position.latitude},${position.longitude}")
    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (context?.let { mapIntent.resolveActivity(it.packageManager) } != null) {
        context.startActivity(mapIntent)
    } else {
        context?.longToast(R.string.error_no_maps)
    }
}

//показать рестораны по месту локации
fun GoogleMap.showRestaurants(position: LatLng, context: Context?) {
    val uri = Uri.parse("geo:${position.latitude},${position.longitude}?q=restaurants")
    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (context?.let { mapIntent.resolveActivity(it.packageManager) } != null) {
        context.startActivity(mapIntent)
    } else {
        context?.longToast(R.string.error_no_maps)
    }
}

//показать рестораны по определенному городу  mMap?.search("spb",context)
fun GoogleMap.search(param: String, context: Context?) {

    val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=restaurants+in+$param+ru")
    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
    mapIntent.data = uri
    if (context?.let { mapIntent.resolveActivity(it.packageManager) } != null) {
        context.startActivity(mapIntent)
    } else {
        context?.longToast(R.string.error_no_maps)
    }
}

fun Context.checkLocationServiceEnabled(): Boolean {
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.showDialogLocation() {
    alert(R.string.enable_gps_on_your_device) {
        positiveButton(R.string.positive_value) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        negativeButton(R.string.negative_value) { dialog -> dialog.dismiss() }
    }.show()
}


fun LatLng.distanceFrom(other: LatLng): Double {
    val result = FloatArray(1)
    Location.distanceBetween(latitude, longitude, other.latitude, other.longitude, result)
    return result[0].toDouble()
}

fun LatLng.getPointAtDistance(distance: Double): LatLng {
    val radiusOfEarth = 6371009.0
    val radiusAngle = (Math.toDegrees(distance / radiusOfEarth)
            / Math.cos(Math.toRadians(latitude)))
    return LatLng(latitude, longitude + radiusAngle)
}
