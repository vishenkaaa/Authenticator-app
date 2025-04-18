package com.example.authenticatorapp.data.repository

import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.data.local.model.toAccountEntity
import com.example.authenticatorapp.data.local.model.toFirebaseMap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

//FIXME летерали
//FIXME взагалі немає хендлінгу помилок
@Singleton
class AccountRepository @Inject constructor(
    private val syncRepository: SyncRepository,
    private val accountDao: AccountDao
) {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllAccounts(uid: String): List<AccountEntity> {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("accounts")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toAccountEntity()
        }
    }

    suspend fun syncAccountsFromFirebase(uid: String) {
        try {
            val firebaseAccounts = getAllAccounts(uid)
            accountDao.deleteAllAccounts()
            firebaseAccounts.forEach { account ->
                accountDao.insertAccount(account)
            }
        } catch (e: Exception) { }
    }

    suspend fun saveAccount(uid: String, account: AccountEntity): Boolean {
        val isSyncing = syncRepository.isSynchronizing(uid)

        if (isSyncing) {
            val docRef = firestore.collection("users")
                .document(uid)
                .collection("accounts")
                .document(account.id.toString())

            docRef.set(account.toFirebaseMap(), SetOptions.merge()).await()
            //FIXME нащо вертати тут true, false? Це action функція яка тільки щось виконує і не віддає ніякого корисного результату, вона може нічого не вертати
            return true
        }
        return false
    }

    suspend fun deleteAccount(uid: String, accountId: Int): Boolean {
        val isSyncing = syncRepository.isSynchronizing(uid)

        if (isSyncing) {
            val docRef = firestore.collection("users")
                .document(uid)
                .collection("accounts")
                .document(accountId.toString())

            docRef.delete().await()
            //FIXME так само тут
            return true
        }
        return false
    }
}