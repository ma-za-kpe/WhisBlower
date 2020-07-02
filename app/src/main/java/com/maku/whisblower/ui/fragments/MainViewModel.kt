package com.maku.whisblower.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maku.whisblower.data.db.entity.VictimeRequest
import com.maku.whisblower.data.repository.VictimRepository

class MainViewModel : ViewModel() {

    var attackerType = MutableLiveData<String>()
    var spouseNumber = MutableLiveData<String>()
    var message = MutableLiveData<String>()
    var organisation = MutableLiveData<String>()
    var location = MutableLiveData<String>()

    private var userMutableLiveData: MutableLiveData<VictimeRequest>? = null

    fun getFormData(): MutableLiveData<VictimeRequest>? {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData<VictimeRequest>()
        }
        return userMutableLiveData
    }

     fun onClick() {
        val sendVictimData = VictimeRequest("spouse", "homMe", "pleEase help", "ngoX", "+25476967521")
        VictimRepository().sendUserData(sendVictimData)
//        userMutableLiveData!!.setValue(sendVictimData)
    }

   suspend fun victimData(victimRequest: VictimeRequest) {
        val victimRepository = VictimRepository()
        return victimRepository.sendUserData(victimRequest)
    }

}
