package com.my_project.deliveringgoods.data.network

import addClearStackFlags
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.ui.login.LoginActivity
import com.my_project.deliveringgoods.utilities.ApiConst
import com.my_project.deliveringgoods.utilities.NetworkUtil
import okhttp3.Interceptor
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection


class InterceptorManager(val context: Context, val userDataHolder: UserDataHolder) {

    fun authInterceptor() = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            userDataHolder.deleteDataUser()
            Handler(Looper.getMainLooper()).post {
                val login = LoginActivity.newInstance(context).addClearStackFlags()
                context.startActivity(login)
            }
        }
        response
    }

    fun connectInterceptor() = Interceptor { chain ->
        if (!NetworkUtil.isOnline(context)) {
            throw ConnectException()
        }
        val builder = chain.request().newBuilder()
        val responce = chain.proceed(builder.build())
        responce
    }

    fun headerInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .build()
        val requestBuilder = original.newBuilder()
            .url(url)
        val token = userDataHolder.token
        Timber.tag("OkHttp").d("Auth-Token: %s", token)
        token?.let {
            val valueAuthorization = ApiConst.TOKEN_TYPE + token
            requestBuilder.addHeader("Authorization", valueAuthorization)
        }
        val request = requestBuilder.build()
        chain.proceed(request)
    }
}