package com.my_project.deliveringgoods.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.my_project.deliveringgoods.R

class LocationPermission {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 107
    }

    fun requestPermission(context: Context, action: () -> Unit) {

        if (Build.VERSION.SDK_INT >= 23) {
            val accessCoarsePermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            val accessFinePermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED || accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as AppCompatActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    showDialog(context)
                } else {
                    request(context)
                }
            } else {
                action()
            }
        } else {
            action()
        }
    }

    private fun showDialog(context: Context) {
        AlertDialog.Builder(context).apply {
            setMessage(R.string.permission_location_explanation)
            setPositiveButton(android.R.string.ok) { _, _ -> request(context) }
            setCancelable(false)
        }.create().show()
    }

    private fun request(context: Context) {
        ActivityCompat.requestPermissions(
            context as AppCompatActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}