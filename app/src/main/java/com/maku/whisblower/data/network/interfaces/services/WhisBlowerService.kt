package com.maku.whisblower.data.network.interfaces.services

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.maku.whisblower.data.db.entity.VictimeRequest
import com.maku.whisblower.data.network.response.VictimeResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.logging.Level


interface WhisBlowerService {

    @Headers("Content-Type: application/json")
    @POST("whisblower/addDetails")
     fun postVictimeRequest(@Body body: VictimeRequest): Call<VictimeResponse?>?

    companion object{
        operator fun invoke(
//            connectivityInterceptor: ConnectivityInterceptor
        ): WhisBlowerService {

            val okHttpClient = OkHttpClient.Builder()
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            // http logging intercepter LIFE SAVER
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            // set your desired log level
            logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }

            val httpClient = OkHttpClient.Builder()
            // add your other interceptors …

            // add logging as last interceptor
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging) // <-- this is the important line!


            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.maku.dev/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
                .create(WhisBlowerService::class.java)
        }

    }

}