package com.my_project.deliveringgoods.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.my_project.deliveringgoods.data.local_storage.UserDataHolder
import com.my_project.deliveringgoods.data.network.ApiService
import com.my_project.deliveringgoods.data.network.GeocoderManager
import com.my_project.deliveringgoods.data.network.InterceptorManager
import com.my_project.deliveringgoods.utilities.ApiConst
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


@Module
class NetworkModule(appContext: Context) {

    private var context: Context = appContext

    @Provides
    @Singleton
    fun provideHostNameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, sslSession -> hostname == ApiConst.BASE_URL.replace("https://", "") }
    }

    @Provides
    @Singleton
    fun provideTrustManager(): X509TrustManager {

        var trustAllCerts = arrayOf<TrustManager>()

        try {
            trustAllCerts = arrayOf(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf<X509Certificate>()
                }
            })
        } catch (e: Exception) {
        }
        return trustAllCerts[0] as X509TrustManager
    }

    @Provides
    @Singleton
    fun provideSslSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("TLS")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        try {
            sslContext?.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        return sslContext!!.socketFactory
    }

    @Provides
    @Singleton
    fun provideInterceptorManager(userDataHolder: UserDataHolder) = InterceptorManager(context, userDataHolder)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        verifier: HostnameVerifier,
        socketFactory: SSLSocketFactory,
        trustManager: X509TrustManager,
        interceptor: InterceptorManager
    ): OkHttpClient =
        OkHttpClient.Builder()
            .sslSocketFactory(socketFactory, trustManager)
            .hostnameVerifier(verifier)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(interceptor.connectInterceptor())
            .addInterceptor(interceptor.authInterceptor())
            .addInterceptor(interceptor.headerInterceptor())
            .connectTimeout(ApiConst.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(ApiConst.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(ApiConst.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

    @Provides
    @Singleton
    fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
            .setDateFormat(ApiConst.DATE_TIME_FORMAT)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonBuilder: GsonBuilder): Retrofit = Retrofit.Builder()
        .baseUrl(ApiConst.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideGeocoderManager() = GeocoderManager(context)

}
