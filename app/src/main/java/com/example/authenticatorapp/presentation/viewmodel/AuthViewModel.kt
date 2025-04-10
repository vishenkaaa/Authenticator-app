package com.example.authenticatorapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.authenticatorapp.data.repository.AccountRepository
import com.example.authenticatorapp.data.repository.AuthRepository
import com.example.authenticatorapp.data.repository.SyncRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val syncRepository: SyncRepository
) : AndroidViewModel(application) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        checkAuthStatus()
        monitorAuthChanges()
    }

    fun checkAuthStatus() {
        _isAuthenticated.value = authRepository.isUserLoggedIn()
    }

    private fun monitorAuthChanges() {
        authRepository.monitorAuthState { isLoggedIn ->
            _isAuthenticated.value = isLoggedIn
        }
    }

    fun signInWithGoogle(idToken: String, auth: FirebaseAuth) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signOut()

                val authResult = auth.signInWithCredential(credential).await()
                val user = authResult.user

                if (user != null) {
                    Log.d("GoogleSignIn", "Success: ${user.displayName}")

                    withContext(Dispatchers.IO) {
                        user.uid.let { uid ->
                            if(syncRepository.isSynchronizing(uid)){
                                accountRepository.syncAccountsFromFirebase(uid)
                                //syncRepository.startSynchronize(uid)
                            }
                        }
                    }

                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Помилка автентифікації: обліковий запис не знайдено")
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Error: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Невідома помилка автентифікації")
            }
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                signInWithGoogle(token, FirebaseAuth.getInstance())
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "SignInResult:failed code=" + e.statusCode)
            _authState.value = AuthState.Error("Помилка входу через Google: ${e.statusCode}")
        }
    }

    fun signOut(context: Context) {
        authRepository.signOut(context)
        _authState.value = AuthState.SignedOut
        checkAuthStatus()
    }

    fun deleteUserAccount(context: Context) {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser() ?: return@launch
                val uid = user.id

                syncRepository.cancelSynchronize(uid)

                val success = authRepository.deleteAccount(context)
                if (success) {
                    _authState.value = AuthState.AccountDeleted
                    checkAuthStatus()
                } else {
                    _authState.value = AuthState.Error("Не вдалося видалити обліковий запис")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Помилка видалення облікового запису: ${e.message}")
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        object SignedOut : AuthState()
        object AccountDeleted : AuthState()
        data class Error(val message: String) : AuthState()
    }
}