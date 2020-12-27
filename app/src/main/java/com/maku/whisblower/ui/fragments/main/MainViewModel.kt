package com.maku.whisblower.ui.fragments.main

import androidx.lifecycle.*
import com.maku.whisblower.data.db.entity.VictimeRequest
import com.maku.whisblower.data.repository.VictimRepository
import com.maku.whisblower.firebaseData.model.Organisations
import com.maku.whisblower.firebaseData.service.OrganisationsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {

    private val _organisations = MutableLiveData< LiveData<List<Organisations>?>>()
    val organisations: LiveData< LiveData<List<Organisations>?>> = _organisations

    init {
        viewModelScope.launch {
            _organisations.value = OrganisationsService.getOrganisations().asLiveData()
            Timber.d("viewmodel " + OrganisationsService.getOrganisations())
        }
    }

    //1
    val forecasts: LiveData<List<Organisations>?> = OrganisationsService.getOrganisations().asLiveData()


    //DELETE ALL THIS
    //hold spouse or attacker number
    var person: String = ""
    var org: String = ""
    var loc: String = ""

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

    fun getLocation(output: String) {
        Timber.d("location " + output)
        loc = output
    }

     fun onClick() {

         Timber.d("attackerType " + person)
         Timber.d("spouseNumber " + spouseNumber.value)
         Timber.d("message " + message.value)
         Timber.d("organisation " + org)
         Timber.d("location " + message.value)

         //check if location is empty
         if (loc.isEmpty()){
             val sendVictimData = VictimeRequest(person, "location is empty", message.value.toString(), org, spouseNumber.value.toString())
             VictimRepository().sendUserData(sendVictimData)
         } else {
             val sendVictimData = VictimeRequest(person, loc, message.value.toString(), org, spouseNumber.value.toString())
             VictimRepository().sendUserData(sendVictimData)
         }

//         userMutableLiveData!!.value = sendVictimData
    }

   suspend fun victimData(victimRequest: VictimeRequest) {
        val victimRepository = VictimRepository()
        return victimRepository.sendUserData(victimRequest)
    }

}
