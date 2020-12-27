package com.maku.whisblower.firebaseData.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.maku.whisblower.firebaseData.model.Organisations
import com.maku.whisblower.firebaseData.model.Organisations.Companion.toOrganisations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

object OrganisationsService {
    private const val TAG = "OrganisationsService"
    @ExperimentalCoroutinesApi
    fun getOrganisations(): Flow<List<Organisations>?> {
        Timber.d("OrganisationsService runningr")
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection("organisations")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(message = "Error fetching posts",
                            cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val map = querySnapshot?.documents?.mapNotNull {
                        it.toOrganisations()
                    }
                    Timber.d("map " + map)
                    offer(map)
                }
            awaitClose {
                Timber.d("Cancelling posts listener")
                listenerRegistration.remove()
            }
        }

    }
}