package com.vitalykalenik.topsecret.data

import com.google.gson.GsonBuilder
import com.vitalykalenik.topsecret.models.request.RegisterRequest
import com.vitalykalenik.topsecret.models.request.Secret
import com.vitalykalenik.topsecret.models.response.LoginResponse
import com.vitalykalenik.topsecret.models.response.RegisterResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @author Vitaly Kalenik
 */
interface Api {

    @POST("/register")
    fun registerUser(@Body body: RegisterRequest): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/oauth/token")
    @Headers("Authorization:Basic MTIzNDU1Njc4OTpYWTdrbXpvTnpsMTAw")
    fun login(
        @Field("grant_type") grant_type: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("/getSalt")
    fun getSalt(@Body body: RegisterRequest): Call<RegisterResponse>

    @POST("/secret")
    fun updateSecret(@Header("Authorization") token: String, @Body body: Secret): Call<RegisterResponse>

    @GET("/secret")
    fun getSecret(@Header("Authorization") token: String): Call<RegisterResponse>

    companion object {
        fun create(): Api {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(Api::class.java)
        }
    }
}