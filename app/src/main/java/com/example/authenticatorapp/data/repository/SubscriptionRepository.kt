package com.example.authenticatorapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

//TODO винести літерали, хендлінг помилок
//Done
@Singleton
class SubscriptionRepository  @Inject constructor()
{
    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_SUBSCRIPTION = "subscription"
        private const val DOCUMENT_CURRENT = "current"

        private const val FIELD_EMAIL = "email"
        private const val FIELD_PLAN = "plan"
        private const val FIELD_NEXT_BILLING = "nextBilling"
        private const val FIELD_HAS_FREE_TRIAL = "hasFreeTrial"
        private const val FIELD_PREMIUM = "premium"
    }

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveSubscriptionForUser(
        uid: String,
        email: String,
        plan: String,
        nextBilling: String,
        hasFreeTrial: Boolean
    ) {
        try {
            val data = hashMapOf(
                FIELD_EMAIL to email,
                FIELD_PLAN to plan,
                FIELD_NEXT_BILLING to nextBilling,
                FIELD_HAS_FREE_TRIAL to hasFreeTrial,
                FIELD_PREMIUM to true
            )
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .collection(COLLECTION_SUBSCRIPTION)
                .document(DOCUMENT_CURRENT)
                .set(data)
                .await()
        } catch (e: Exception) {
            Log.e("SubscriptionRepository", "Помилка при збереженні підписки: ${e.message}", e)
        }
    }

    //FIXME не використовується, чому? Якщо треба, використовуємо, якщо ні, прибираємо
    //Done

    suspend fun loadSubscriptionForUser(uid: String): Map<String, Any>? {
        return try {
            val snapshot = firestore
                .collection(COLLECTION_USERS)
                .document(uid)
                .collection(COLLECTION_SUBSCRIPTION)
                .document(DOCUMENT_CURRENT)
                .get()
                .await()
            if (snapshot.exists()) snapshot.data else null
        } catch (e: Exception) {
            Log.e("SubscriptionRepository", "Помилка при завантаженні підписки: ${e.message}", e)
            null
        }
    }

    suspend fun cancelSubscription(uid: String) {
        try {
            val data = mapOf<String, Any?>(
                FIELD_PLAN to null,
                FIELD_NEXT_BILLING to null,
                FIELD_PREMIUM to false
            )
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .collection(COLLECTION_SUBSCRIPTION)
                .document(DOCUMENT_CURRENT)
                .update(data)
                .await()
        } catch (e: Exception) {
            Log.e("SubscriptionRepository", "Не вдалося скасувати підписку: ${e.message}", e)
        }
    }

    //FIXME не використовується, чому? Якщо треба, використовуємо, якщо ні, прибираємо
    //Done
}