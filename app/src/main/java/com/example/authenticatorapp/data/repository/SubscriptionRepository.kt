package com.example.authenticatorapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

//TODO винести літерали, хендлінг помилок
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
        firestore.collection("users").document(uid).collection("subscription").document("current").set(data).await()
    }

    //FIXME не використовується, чому? Якщо треба, використовуємо, якщо ні, прибираємо
    suspend fun isUserPremium(uid: String): Boolean {
        val doc = firestore.collection("users").document(uid).get().await()
        return doc.getBoolean("premium") ?: false
    }

    suspend fun loadSubscriptionForUser(uid: String): Map<String, Any>? {
        val snapshot = firestore
            .collection("users")
            .document(uid)
            .collection("subscription")
            .document("current")
            .get()
            .await()
        return if (snapshot.exists()) snapshot.data else null
    }

    suspend fun cancelSubscription(uid: String) {
        val data = mapOf<String, Any?>(
            "plan" to null,
            "nextBilling" to null,
            "premium" to false
        )
        firestore.collection("users")
            .document(uid)
            .collection("subscription")
            .document("current")
            .update(data)
            .await()
    }
    
    //FIXME не використовується, чому? Якщо треба, використовуємо, якщо ні, прибираємо
    suspend fun deleteUserData(uid: String) {
        firestore.collection("users").document(uid).delete().await()
    }
}