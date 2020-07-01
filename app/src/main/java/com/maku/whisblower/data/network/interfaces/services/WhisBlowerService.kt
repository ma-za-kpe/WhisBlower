package com.maku.whisblower.data.network.interfaces.services

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.maku.whisblower.data.db.entity.VictimeRequest
import com.maku.whisblower.data.network.interfaces.connection.ConnectivityInterceptor
import com.maku.whisblower.data.network.response.VictimeResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface WhisBlowerService {

    @POST("whisblower/addDetails")
    suspend fun postVictimeRequest(@Body body: String?): Call<VictimeRequest?>?

    companion object{
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): WhisBlowerService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.maku.dev/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WhisBlowerService::class.java)
        }

    }

}