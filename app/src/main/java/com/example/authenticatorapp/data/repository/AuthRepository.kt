package com.example.authenticatorapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val application: Application,
    private val accountRepository: AccountRepository,
) {
    private val auth: FirebaseAuth = Firebase.auth
    private val syncRepository = SyncRepository(accountDao)
    private val credentialManager by lazy { CredentialManager.create(application) }

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

    // Функція для входу через Google
    suspend fun signInWithGoogle(context: Context, forceNewAccount: Boolean = false): Result<Boolean> {
        return try {
            val nonce = generateNonce()

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(!forceNewAccount)
                .setServerClientId("659133148753-gdeg9eu8k0mffj8nbtptirv2920glm3v.apps.googleusercontent.com")
                .setAutoSelectEnabled(!forceNewAccount)
                .setNonce(nonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(context, request)
                val success = handleCredentialResult(result)
                if (success) Result.success(true) else Result.failure(Exception("Помилка обробки облікових даних"))
            } catch (e: NoCredentialException) {
                signUpWithGoogle(context)
            } catch (e: GetCredentialException) {
                Log.e("GoogleSignIn", "Error: ${e.message}")
                Result.failure(Exception(e.message ?: "Помилка отримання облікових даних"))
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Unexpected error: ${e.message}")
            Result.failure(Exception(e.message ?: "Невідома помилка автентифікації"))
        }
    }

    // Функція для реєстрації через Google
    private suspend fun signUpWithGoogle(context: Context): Result<Boolean> {
        return try {
            val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
                serverClientId = "659133148753-gdeg9eu8k0mffj8nbtptirv2920glm3v.apps.googleusercontent.com"
            )
                .setNonce(generateNonce())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val success = handleCredentialResult(result)

            if (success) Result.success(true) else Result.failure(Exception("Помилка обробки облікових даних"))
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Sign up error: ${e.message}")
            Result.failure(Exception(e.message ?: "Помилка при реєстрації"))
        }
    }

    // Обробка результату автентифікації
    private suspend fun handleCredentialResult(result: GetCredentialResponse): Boolean {
        return when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken

                        authenticateWithFirebase(idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("GoogleSignIn", "Invalid Google ID token: ${e.message}")
                        false
                    }
                } else {
                    Log.e("GoogleSignIn", "Unexpected credential type")
                    false
                }
            }
            else -> {
                Log.e("GoogleSignIn", "Unexpected credential type")
                false
            }
        }
    }

    // Автентифікація через Firebase
    private suspend fun authenticateWithFirebase(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val auth = FirebaseAuth.getInstance()
            auth.signOut()

            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {
                Log.d("GoogleSignIn", "Firebase auth success: ${user.displayName}")

                withContext(Dispatchers.IO) {
                    user.uid.let { uid ->
                        val shouldSync = syncRepository.getShouldSynchronize(uid)
                        if (shouldSync) {
                            accountRepository.syncAccountsFromFirebase(uid)
                        }
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Firebase auth error: ${e.message}")
            false
        }
    }

    // Генерація випадкового nonce для безпеки
    private fun generateNonce(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    suspend fun signOut(context: Context): Boolean {
        return try {
            //FIXME deprecations
            //Done
            val credentialManager = CredentialManager.create(context)

            // Очищення стану облікових даних
            credentialManager.clearCredentialState(ClearCredentialStateRequest())

            val uid = getCurrentUser()?.id
            if (uid != null && syncRepository.getShouldSynchronize(uid)) {
                clearLocalDatabase()
            }

            //FIXME використати signOut замість revokeAccess
            //Done
            // Вихід з Firebase
            Firebase.auth.signOut()

            Log.d("SignOut", "Користувач успішно вийшов")
            true
        } catch (e: Exception) {
            Log.e("SignOut", "Помилка під час виходу: ${e.message}", e)
            false
        }
    }

    fun monitorAuthState(onAuthChanged: (Boolean) -> Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            val loggedIn = firebaseAuth.currentUser != null
            Log.d("AuthState", "Користувач увійшов: $loggedIn")
            onAuthChanged(loggedIn)
        }
    }

    suspend fun deleteAccount(context: Context): Boolean {
        return try {
            val user = FirebaseAuth.getInstance().currentUser ?: return false

            withContext(Dispatchers.IO) {
                clearLocalDatabase()
            }

            // Очищення збережених облікових даних через CredentialManager
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())

            // Видалення акаунта Firebase
            user.delete().await()

            Log.d("AccountDeletion", "Акаунт користувача успішно видалено")
            true
        } catch (e: Exception) {
            Log.e("AccountDeletion", "Помилка видалення акаунта: ${e.message}", e)
            false
        }
    }

    private suspend fun clearLocalDatabase() {
        try {
            accountDao.deleteAllAccounts()
            Log.d("LocalDatabase", "Локальна база даних успішно очистилася")
        } catch (e: Exception) {
            Log.e("LocalDatabase", "Помилка очищення локальної бази даних: ${e.message}", e)
            throw e
        }
    }
}