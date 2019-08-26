package com.my_project.deliveringgoods.data.provider

import android.content.Context
import android.content.pm.PackageManager
import com.my_project.deliveringgoods.BuildConfig
import timber.log.Timber


class AppInfoProvider(private val context: Context) {

    companion object {
        const val RELEASE = "release"
        const val DEBUG = "debug"
    }

    val isRelease: Boolean
        get() = BuildConfig.BUILD_TYPE == RELEASE

    val isDebug: Boolean
        get() = BuildConfig.BUILD_TYPE == DEBUG

    val appVersionName: String
        get() {
            var version = ""
            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                version = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.e(e)
            }
            return version
        }

    val appVersionNameWithCode: String
        get() {
            var version = ""
            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                version = pInfo.versionName
                version += " (" + pInfo.versionCode + ")"
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.e(e)
            }
            return version
        }
    val typePlatform = "android"
}