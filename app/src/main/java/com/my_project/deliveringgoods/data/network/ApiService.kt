package com.my_project.deliveringgoods.data.network

import com.my_project.deliveringgoods.data.entities.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun register(
        @Field("username") username: String,
        @Field("mail") mail: String,
        @Field("password") password: String
    ): Response<RegistrationResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<RegistrationResponse>

    @FormUrlEncoded
    @POST("token")
    suspend fun refreshToken(
        @Field("username") username: String
    ): Response<RegistrationResponse>

    @FormUrlEncoded
    @POST("settings/pushtoken")
    fun updatePushToken(
        @Field("token_firebase") firebaseToken: String,
        @Field("platform") typePlatform: String
    ): Call<ResponseResult>

    @Multipart
    @POST("upload/image")
    fun sendFile(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Single<ResponseResult>

    @POST("types/shift")
    fun typesShift(): Single<List<TypesShift>>

    @POST("select/shift/?")
    fun selectTypesShift(
        @Query("type") type: String
    ): Single<ResponseResult>

    @FormUrlEncoded
    @PUT("logout")
    fun logout(
        @Field("username") username: String
    ): Single<ResponseResult>

    @FormUrlEncoded
    @POST("info")
    fun sendInfoRequest(
        @Field("code") codeId: Int
    ): Single<InfoResponse>

    @FormUrlEncoded
    @POST("tracker")
    fun sendLocationPointRequest(
        @Field("lat") lat: String,
        @Field("lng") lng: String
    ): Single<ResponseResult>

    @POST("orders")
    suspend fun orders(): Response<List<Order>>

    @FormUrlEncoded
    @PUT("orders/{orderId}")
    suspend fun acceptOrder(
        @Path("orderId") relevantId: String,
        @Field("status") statusId: Int
    ): Response<ResponseResult>

    @POST("agreement")
    fun agreement(): Single<UserAgreement>

    @POST("privacy")
    fun privacy(): Single<PrivacyPolicy>

    @POST("terms")
    fun terms(): Single<TermsDelivery>

    @POST("info")
    fun info(): Single<List<AdvInfo>>

}