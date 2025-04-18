package com.example.authenticatorapp.data.repository

import android.content.Context
import android.util.Log
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val syncRepository: SyncRepository) {
    private val auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): User? {
        return auth.currentUser?.let { firebaseUser ->
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email
            )
        }
    }

    suspend fun signOut(context: Context) {
        //FIXME deprecations
        val googleSignInClient = GoogleSignIn.getClient(
            context.applicationContext,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("659133148753-gdeg9eu8k0mffj8nbtptirv2920glm3v.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )

        withContext(Dispatchers.IO) {
            val uid = getCurrentUser()?.id
            if (uid != null && syncRepository.isSynchronizing(uid)) {
                clearLocalDatabase()
            }
        }

        auth.signOut()
        //FIXME використати signOut замість revokeAccess
        googleSignInClient.revokeAccess().addOnCompleteListener {
            Log.d("GoogleSignIn", "User fully signed out")
        }
    }

    fun monitorAuthState(onAuthChanged: (Boolean) -> Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            onAuthChanged(firebaseAuth.currentUser != null)
        }
    }

    suspend fun deleteAccount(context: Context): Boolean {
        return try {
            val user = auth.currentUser ?: return false

            withContext(Dispatchers.IO) {
                clearLocalDatabase()
            }

            val googleSignInClient = GoogleSignIn.getClient(
                context.applicationContext,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("659133148753-gdeg9eu8k0mffj8nbtptirv2920glm3v.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            )
            googleSignInClient.revokeAccess().await()

            user.delete().await()

            Log.d("AccountDeletion", "User account successfully deleted")
            true
        } catch (e: Exception) {
            Log.e("AccountDeletion", "Error deleting account: ${e.message}", e)
            false
        }
    }

    private suspend fun clearLocalDatabase() {
        try {
            accountDao.deleteAllAccounts()
            Log.d("LocalDatabase", "Local database successfully cleared")
        } catch (e: Exception) {
            Log.e("LocalDatabase", "Error clearing local database: ${e.message}", e)
            throw e
        }
    }
}