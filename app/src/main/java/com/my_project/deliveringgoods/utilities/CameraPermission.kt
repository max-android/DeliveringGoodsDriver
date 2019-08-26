package com.my_project.deliveringgoods.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.my_project.deliveringgoods.R


class CameraPermission {

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 104
    }

    fun requestPermission(context: Context, action: () -> Unit) {

        if (Build.VERSION.SDK_INT >= 23) {
            val cameraPermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )
            val writePermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (cameraPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as AppCompatActivity,
                        Manifest.permission.CAMERA
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
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
            setMessage(R.string.permission_camera_explanation)
            setPositiveButton(android.R.string.ok) { _, _ -> request(context) }
            setCancelable(false)
        }.create().show()
    }

    private fun request(context: Context) {
        ActivityCompat.requestPermissions(
            context as AppCompatActivity,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

}