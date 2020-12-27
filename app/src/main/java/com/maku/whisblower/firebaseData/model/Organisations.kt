package com.maku.whisblower.firebaseData.model

import android.os.Parcelable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import timber.log.Timber

@Parcelize
data class Organisations(
    val image: String,
    val name: String,
    val tel: String
): Parcelable {
    companion object {
        fun DocumentSnapshot.toOrganisations(): Organisations? {
            try {
                val name = getString("name")!!
                val imageUrl = getString("image")!!
                val tel =  getString("tel")!!
                return Organisations(name, imageUrl, tel)
            } catch (e: Exception) {
                Timber.e("Error converting Organisations ${e.message}")
                FirebaseCrashlytics.getInstance().log("Error converting Organisations")
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "Organisations"
    }
}
