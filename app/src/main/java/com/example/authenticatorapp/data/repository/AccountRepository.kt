package com.example.authenticatorapp.data.repository

import android.util.Log
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.data.local.model.AccountEntity.Companion.toAccountEntity
import com.example.authenticatorapp.data.local.model.AccountEntity.Companion.toFirebaseMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

//FIXME летерали
//FIXME взагалі немає хендлінгу помилок
//Done
@Singleton
class AccountRepository @Inject constructor(
    private val syncRepository: SyncRepository,
    private val accountDao: AccountDao
) {
    private val firestore = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    private suspend fun getAllAccounts(uid: String): List<AccountEntity> {
        return try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .collection("accounts")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toAccountEntity() }
        } catch (e: Exception) {
            Log.e("AccountRepository", "Помилка при отриманні акаунтів: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun syncAccountsFromFirebase(uid: String) {
        try {
            val firebaseAccounts = getAllAccounts(uid)
            accountDao.deleteAllAccounts()
            firebaseAccounts.forEach { account ->
                accountDao.insertAccount(account)
            }
        } catch (e: Exception) {
            Log.e("AccountRepository", "Помилка при синхронізації з Firebase: ${e.message}", e)
        }
    }

    suspend fun getAccountById(accountId: Int): AccountEntity? {
        return accountDao.getAccountById(accountId)
    }

    suspend fun updateAccount(account: AccountEntity) {
        try {
            accountDao.updateAccount(account)
        } catch (e: Exception) {
            Log.e("AccountRepository", "Помилка при оновленні акаунта: ${e.message}", e)
        }
    }

    suspend fun saveAccount(account: AccountEntity) {
        try {
            val insertedId = accountDao.insertAccount(account)
            val savedAccount = account.copy(id = insertedId.toInt())

            saveAccountToFirebase(savedAccount)
        } catch (e: Exception) {
            Log.e("AccountRepository", "Помилка при збереженні акаунта: ${e.message}", e)
        }
    }

    private suspend fun saveAccountToFirebase(account: AccountEntity) {
        try {
            currentUserId?.let { uid ->
                val isSyncing = syncRepository.getShouldSynchronize(uid)

                if (isSyncing) {
                    val docRef = firestore.collection("users")
                        .document(uid)
                        .collection("accounts")
                        .document(account.id.toString())

                    docRef.set(account.toFirebaseMap(), SetOptions.merge()).await()
                    //FIXME нащо вертати тут true, false? Це action функція яка тільки щось виконує і не віддає ніякого корисного результату, вона може нічого не вертати
                    //Done
                }
            }
        } catch (e: Exception) {
            Log.e("AccountRepository", "Помилка при збереженні акаунта: ${e.message}", e)
        }
    }

    suspend fun deleteAccount(accountId: Int) {
        try {
            accountDao.deleteAccountById(accountId)

            currentUserId?.let { uid ->
                val isSyncing = syncRepository.getShouldSynchronize(uid)

                if (isSyncing) {
                    val docRef = firestore.collection("users")
                        .document(uid)
                        .collection("accounts")
                        .document(accountId.toString())

                    docRef.delete().await()
                    //FIXME так само тут
                    //Done
                }
            }
        } catch (e: Exception) {
            Log.e("AccountRepository", "Помилка при видаленні акаунта: ${e.message}", e)
        }
    }
}