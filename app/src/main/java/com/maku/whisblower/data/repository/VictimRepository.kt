package com.maku.whisblower.data.repository

import com.maku.whisblower.data.db.entity.VictimeRequest
import com.maku.whisblower.data.network.interfaces.services.WhisBlowerService
import com.maku.whisblower.data.network.response.VictimeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class VictimRepository {

    private var instance: VictimRepository? = null
    fun getInstance(): VictimRepository? {
        if (instance == null) {
            instance = VictimRepository()
        }
        return instance
    }

    //This is the method that calls API using Retrofit
    fun sendUserData(victim : VictimeRequest) {
        // Your Api Call with response callback
        WhisBlowerService().postVictimeRequest(victim)?.enqueue(object : Callback<VictimeResponse?> {
            override fun onFailure(call: Call<VictimeResponse?>, t: Throwable) {
                Timber.d("faileddddd " + t)
            }

            override fun onResponse(
                call: Call<VictimeResponse?>,
                response: Response<VictimeResponse?>
            ) {
                Timber.d("success " + response)
            }


        })
    }

//    suspend fun sendUserData(victim: VictimeRequest?): MutableLiveData<VictimeResponse>? {
//        val loginApiResponseMutableLiveData: MutableLiveData<VictimeResponse> =
//            MutableLiveData<VictimeResponse>()
//        if (victim != null) {
//            WhisBlowerService().postVictimeRequest(victim)?.enqueue(object : Callback<VictimeResponse?> {
//                override fun onResponse(
//                    call: Call<VictimeResponse?>,
//                    response: Response<VictimeResponse?>
//                ) {
//                    if (response.isSuccessful()) {
//                        Timber.d("success " + response)
//                    } else {
//                        val gson = Gson()
//                        if (response.errorBody() != null) {
//                            try {
//                                Timber.e("error %s", response.errorBody()!!.string())
//                            } catch (e: IOException) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<VictimeResponse?>,
//                    t: Throwable
//                ) {
//                    Timber.d("faileddddd " + t)
//                    Timber.d(t.message)
//                }
//            })
//        }
//        return loginApiResponseMutableLiveData
//    }

}
