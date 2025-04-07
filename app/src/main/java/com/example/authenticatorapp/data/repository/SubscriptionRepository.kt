package com.example.authenticatorapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepository  @Inject constructor(){
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveSubscriptionForUser(
        uid: String,
        email: String,
        plan: String,
        nextBilling: String,
        hasFreeTrial: Boolean
    ) {
        val data = hashMapOf(
            "email" to email,
            "plan" to plan,
            "nextBilling" to nextBilling,
            "hasFreeTrial" to hasFreeTrial,
            "premium" to true
        )
        firestore.collection("users").document(uid).set(data).await()
    }

    suspend fun isUserPremium(uid: String): Boolean {
        val doc = firestore.collection("users").document(uid).get().await()
        return doc.getBoolean("premium") ?: false
    }

    suspend fun loadSubscriptionForUser(uid: String): Map<String, Any>? {
        val snapshot = firestore.collection("users").document(uid).get().await()
        return if (snapshot.exists()) snapshot.data else null
    }

    suspend fun cancelSubscription(uid: String) {
        val data = mapOf<String, Any?>(
            "plan" to null,
            "nextBilling" to null,
            "premium" to false
        )
        firestore.collection("users").document(uid).update(data).await()
    }
}