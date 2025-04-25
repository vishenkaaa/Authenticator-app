package com.example.authenticatorapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.repository.AuthRepository
import com.example.authenticatorapp.data.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
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

    private fun checkAuthStatus() {
        _isAuthenticated.value = authRepository.isUserLoggedIn()
    }

    private fun monitorAuthChanges() {
        authRepository.monitorAuthState { isLoggedIn ->
            _isAuthenticated.value = isLoggedIn
        }
    }

    fun signInWithGoogle(context: Context) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            authRepository.signInWithGoogle(context, forceNewAccount = true
            )
                .onSuccess {
                    _authState.value = AuthState.Success
                    checkAuthStatus()
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Помилка автентифікації")
                }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            authRepository.signOut(context)
        }
        _authState.value = AuthState.SignedOut
        checkAuthStatus()
    }

    fun deleteUserAccount(context: Context) {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser() ?: return@launch
                val uid = user.id

                syncRepository.setShouldSynchronize(uid, false)

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