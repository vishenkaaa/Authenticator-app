package com.example.authenticatorapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.toFirebaseMap
import com.example.authenticatorapp.data.local.preferences.SyncPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(private val accountDao: AccountDao, application: Application){
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val ACCOUNTS_COLLECTION = "accounts"
        private const val FIELD_SYNC = "sync"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val context: Context = application.applicationContext
    private val pref = SyncPreferences(context)

    suspend fun startSynchronize(uid: String) {
        pref.setSyncEnabled(true)

        val data = mapOf(FIELD_SYNC to true)
        firestore.collection(USERS_COLLECTION).document(uid).set(data, SetOptions.merge()).await()

        val accounts = accountDao.getAllAccounts().first()

        val existingAccountsSnapshot = firestore.collection(USERS_COLLECTION)
            .document(uid)
            .collection(ACCOUNTS_COLLECTION)
            .get()
            .await()

        val deleteBatch = firestore.batch()

        existingAccountsSnapshot.documents.forEach { document ->
            deleteBatch.delete(document.reference)
        }

        deleteBatch.commit().await()

        if (accounts.isNotEmpty()) {
            val batch = firestore.batch()

            accounts.forEach { account ->
                val docRef = firestore.collection(USERS_COLLECTION)
                    .document(uid)
                    .collection(ACCOUNTS_COLLECTION)
                    .document(account.id.toString())

                batch.set(docRef, account.toFirebaseMap())
            }

            batch.commit().await()
        }
    }

    suspend fun cancelSynchronize(uid: String) {
        pref.setSyncEnabled(false)

        val data = mapOf(ACCOUNTS_COLLECTION to false)
        firestore.collection(USERS_COLLECTION).document(uid).set(data, SetOptions.merge()).await()
    }

    suspend fun isSynchronizing(uid: String): Boolean {
        pref.getSyncEnabled()?.let { return it }
        return isSynchronizingFromDb(uid)
    }

    suspend fun isSynchronizingFromDb(uid: String): Boolean{
        return try {
            val snapshot = firestore.collection(USERS_COLLECTION).document(uid).get().await()
            val syncStatus = snapshot.getBoolean(ACCOUNTS_COLLECTION) ?: false

            pref.setSyncEnabled(syncStatus)

            syncStatus
        } catch (e: Exception) {
            Log.e("SyncRepository", "Помилка при перевірці статусу синхронізації: ${e.message}")
            false
        }
    }
}