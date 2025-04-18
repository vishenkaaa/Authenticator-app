package com.example.authenticatorapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val SUBSCRIPTION_COLLECTION = "subscription"
        private const val CURRENT_DOCUMENT = "current"

        private const val FIELD_EMAIL = "email"
        private const val FIELD_PLAN = "plan"
        private const val FIELD_NEXT_BILLING = "nextBilling"
        private const val FIELD_HAS_FREE_TRIAL = "hasFreeTrial"
        private const val FIELD_PREMIUM = "premium"
    }

    suspend fun saveSubscriptionForUser(
        uid: String,
        email: String,
        plan: String,
        nextBilling: String,
        hasFreeTrial: Boolean
    ) {
        val data = hashMapOf(
            FIELD_EMAIL to email,
            FIELD_PLAN to plan,
            FIELD_NEXT_BILLING to nextBilling,
            FIELD_HAS_FREE_TRIAL to hasFreeTrial,
            FIELD_PREMIUM to true
        )
        firestore.collection(USERS_COLLECTION)
            .document(uid)
            .collection(SUBSCRIPTION_COLLECTION)
            .document(CURRENT_DOCUMENT)
            .set(data)
            .await()
    }

    suspend fun loadSubscriptionForUser(uid: String): Map<String, Any>? {
        val snapshot = firestore
            .collection(USERS_COLLECTION)
            .document(uid)
            .collection(SUBSCRIPTION_COLLECTION)
            .document(CURRENT_DOCUMENT)
            .get()
            .await()
        return if (snapshot.exists()) snapshot.data else null
    }

    suspend fun cancelSubscription(uid: String) {
        val data = mapOf<String, Any?>(
            FIELD_PLAN to null,
            FIELD_NEXT_BILLING to null,
            FIELD_PREMIUM to false
        )
        firestore.collection(USERS_COLLECTION)
            .document(uid)
            .collection(SUBSCRIPTION_COLLECTION)
            .document(CURRENT_DOCUMENT)
            .update(data)
            .await()
    }
}
