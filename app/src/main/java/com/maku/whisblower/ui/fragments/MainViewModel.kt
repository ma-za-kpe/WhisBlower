package com.maku.whisblower.ui.fragments

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maku.whisblower.data.db.entity.VictimeRequest
import com.maku.whisblower.data.repository.VictimRepository
import com.maku.whisblower.utils.showError
import timber.log.Timber

class MainViewModel : ViewModel() {

    //hold spouse or attacker number
    var person: String = ""
    var org: String = ""

    //organisation text
    var popo = MutableLiveData<String>()

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

        /**
         * Onclick spouse
         * */ fun onClickSpouse() {
            person = "spouse"
            Timber.d("spouseNumber ")
            }

    /**
     * Onclick attacker
     * */ fun onClickAttacker() {
        person = "attacker"
        Timber.d("attacker ")
         }

    /**
     * Onclick organisation
     * */ fun onClickPolice() {
        org = "police"
        Timber.d("org " + org)
    }

     fun onClick() {

         Timber.d("attackerType " + person)
         Timber.d("spouseNumber " + spouseNumber.value)
         Timber.d("message " + message.value)
         Timber.d("organisation " + org)
         Timber.d("location " + message.value)

         val sendVictimData = VictimeRequest(person, "homMe", message.value.toString(), org, spouseNumber.value.toString())
         VictimRepository().sendUserData(sendVictimData)
//        userMutableLiveData!!.setValue(sendVictimData)
    }

   suspend fun victimData(victimRequest: VictimeRequest) {
        val victimRepository = VictimRepository()
        return victimRepository.sendUserData(victimRequest)
    }

}
