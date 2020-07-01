package com.maku.whisblower.ui.fragments

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var attackerType = MutableLiveData<String>()
    var spouseNumber = MutableLiveData<String>()
    var message = MutableLiveData<String>()
    var organisation = MutableLiveData<String>()

    private var userMutableLiveData: MutableLiveData<VictimRequest>? = null

    fun getFormData(): MutableLiveData<VictimRequest>? {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData<VictimRequest>()
        }
        return userMutableLiveData
    }

    fun onClick(view: View?) {
//        val sendVictimData = VictimRequest()
//        userMutableLiveData!!.setValue(sendVictimData)
    }

//    fun victimData(victimRequest: VictimRequest?): LiveData<String?>? {
//        val victimRepository = VictimRepository()
//        return victimRepository.loginUser(victimRequest)
//    }

}
