package com.example.authenticatorapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.toFirebaseMap
import com.example.authenticatorapp.data.local.preferences.SyncPreferences
import com.example.authenticatorapp.presentation.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

//TODO винести літерали, хендлінг помилок
@Singleton
class SyncRepository @Inject constructor(private val accountDao: AccountDao, application: Application){
    private val firestore = FirebaseFirestore.getInstance()
    //TODO якщо я не помиляюсь, контекст теж можна заінджектити в конструкторі, спробуй, чи працюватиме
    private val context: Context = application.applicationContext
    private val pref = SyncPreferences(context)

    
    //FIXME переписати логіку хендлінгу синхронізації
    //FIXME змінити неймінг. setShouldSynchronize
    suspend fun startSynchronize(uid: String) {
        pref.setSync(true)

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
        pref.setSync(false)

        val data = mapOf("sync" to false)
        firestore.collection("users").document(uid).set(data, SetOptions.merge()).await()
    }

    //FIXME змінити неймінг. getShouldSynchronize
    suspend fun isSynchronizing(uid: String): Boolean {
        pref.isSync()?.let { return it }
        return isSynchronizingFromDb(uid)
    }

    //FIXME single source of truth. Зберігати значення лише в одному місці
    suspend fun isSynchronizingFromDb(uid: String): Boolean{
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val syncStatus = snapshot.getBoolean("sync") ?: false

            pref.setSync(syncStatus)

            syncStatus
        } catch (e: Exception) {
            Log.e("SyncRepository", "Помилка при перевірці статусу синхронізації: ${e.message}")
            false
        }
    }
}