package com.example.authenticatorapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.toFirebaseMap
import com.example.authenticatorapp.presentation.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository  @Inject constructor(private val accountDao: AccountDao, application: Application){
    private val firestore = FirebaseFirestore.getInstance()

    private val context: Context = application.applicationContext
    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            "sync_prefs",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    suspend fun startSynchronize(uid: String) {
        prefs.edit()
            .putBoolean("isSyncEnabled", true)
            .apply()

        val data = mapOf("sync" to true)
        firestore.collection("users").document(uid).set(data, SetOptions.merge()).await()

        val accounts = accountDao.getAllAccounts().first()

        val existingAccountsSnapshot = firestore.collection("users")
            .document(uid)
            .collection("accounts")
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
                val docRef = firestore.collection("users")
                    .document(uid)
                    .collection("accounts")
                    .document(account.id.toString())

                batch.set(docRef, account.toFirebaseMap())
            }

            batch.commit().await()
        }
    }

    suspend fun cancelSynchronize(uid: String) {
        prefs.edit()
            .putBoolean("isSyncEnabled", false)
            .apply()

        val data = mapOf("sync" to false)
        firestore.collection("users").document(uid).set(data, SetOptions.merge()).await()
    }

    suspend fun isSynchronizing(uid: String): Boolean {
        if (prefs.contains("isSyncEnabled")) {
            return prefs.getBoolean("isSyncEnabled", false)
        }

        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val syncStatus = snapshot.getBoolean("sync") ?: false

            prefs.edit()
                .putBoolean("isSyncEnabled", syncStatus)
                .apply()

            syncStatus
        } catch (e: Exception) {
            Log.e("SyncRepository", "Помилка при перевірці статусу синхронізації: ${e.message}")
            false
        }
    }
}