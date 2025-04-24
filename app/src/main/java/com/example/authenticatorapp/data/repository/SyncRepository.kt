package com.example.authenticatorapp.data.repository

import android.util.Log
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.AccountEntity.Companion.toFirebaseMap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

//TODO винести літерали, хендлінг помилок
//Done
@Singleton
class SyncRepository @Inject constructor(private val accountDao: AccountDao){
    private val firestore = FirebaseFirestore.getInstance()
    //TODO якщо я не помиляюсь, контекст теж можна заінджектити в конструкторі, спробуй, чи працюватиме
    //Done
    
    //FIXME переписати логіку хендлінгу синхронізації
    //FIXME змінити неймінг. setShouldSynchronize
    //Done
    suspend fun setShouldSynchronize(uid: String, status: Boolean){
        try {
            val data = mapOf("sync" to status)
            firestore.collection("users").document(uid).set(data, SetOptions.merge()).await()

            if (status) {
                val accounts = accountDao.getAllAccounts().first()

                if (accounts.isNotEmpty()) {
                    val batch = firestore.batch()
                    accounts.forEach { account ->
                        val docRef = firestore.collection("users")
                            .document(uid)
                            .collection("accounts")
                            .document(account.id.toString())

                        batch.set(docRef, account.toFirebaseMap(), SetOptions.merge())
                    }
                    batch.commit().await()
                }
            }
        }  catch (e: Exception) {
            Log.e("SyncRepository", "Помилка при оновленні статусу синхронізації: ${e.message}", e)
        }
    }

    //FIXME змінити неймінг. getShouldSynchronize
    //Done
    suspend fun getShouldSynchronize(uid: String): Boolean {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val syncStatus = snapshot.getBoolean("sync") ?: false

            syncStatus
        } catch (e: Exception) {
            Log.e("SyncRepository", "Помилка при перевірці статусу синхронізації: ${e.message}")
            false
        }
    }

    //FIXME single source of truth. Зберігати значення лише в одному місці
    //Done
}