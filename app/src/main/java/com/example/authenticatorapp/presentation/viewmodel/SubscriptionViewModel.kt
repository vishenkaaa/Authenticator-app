package com.example.authenticatorapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.authenticatorapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor (application: Application, private val authRepository: AuthRepository)
    : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context, "subscription_prefs",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _plan = MutableStateFlow<String?>(null)
    val plan: StateFlow<String?> = _plan

    private val _nextBilling = MutableStateFlow<String?>(null)
    val nextBilling: StateFlow<String?> = _nextBilling

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _deleteAccountStatus = MutableStateFlow<DeleteAccountStatus>(DeleteAccountStatus.Idle)
    val deleteAccountStatus: StateFlow<DeleteAccountStatus> = _deleteAccountStatus

    init {
        checkAuthStatus()
        monitorAuthChanges()
        _plan.value = prefs.getString("plan", null)
        _nextBilling.value = prefs.getString("next_billing", null)
    }

    fun checkAuthStatus() {
        _isAuthenticated.value = authRepository.isUserLoggedIn()
    }

    private fun monitorAuthChanges() {
        authRepository.monitorAuthState { isLoggedIn ->
            _isAuthenticated.value = isLoggedIn
            if (isLoggedIn) {
                loadSubscription()
            }
        }
    }

    fun signOut(context: Context){
        authRepository.signOut(context)
        checkAuthStatus()
        monitorAuthChanges()
    }

    fun deleteUserAccount() {
        _deleteAccountStatus.value = DeleteAccountStatus.InProgress

        viewModelScope.launch {
            val success = authRepository.deleteAccount(context)

            if (success) {
                clearSubscription()
                _deleteAccountStatus.value = DeleteAccountStatus.Success
            } else {
                _deleteAccountStatus.value = DeleteAccountStatus.Error("Помилка видалення акаунта")
            }
        }
    }

    fun saveSubscription(plan: String, hasFreeTrial: Boolean) {
        val today = LocalDate.now()
        val nextBillingDate = when {
            plan.contains("Yearly", ignoreCase = true) -> today.plusYears(1)
            hasFreeTrial -> today.plusDays(3).plusWeeks(1)
            else -> today.plusWeeks(1)
        }

        prefs.edit()
            .putString("plan", plan)
            .putString("next_billing", nextBillingDate.toString())
            .commit()

        loadSubscription()
    }

    fun formatDate(isoDate: String?): String {
        if (isoDate == null) return ""
        val date = LocalDate.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        return date.format(formatter)
    }

    fun loadSubscription() {
        if (_isAuthenticated.value) {
            _plan.value = prefs.getString("plan", null)
            _nextBilling.value = formatDate(prefs.getString("next_billing", null))
        }
    }

    fun clearSubscription() {
        prefs.edit().clear().commit()
        _plan.value = null
        _nextBilling.value = null
    }

    fun resetDeleteAccountStatus() {
        _deleteAccountStatus.value = DeleteAccountStatus.Idle
    }

    // Для відстеження стану операції видалення акаунта
    sealed class DeleteAccountStatus {
        object Idle : DeleteAccountStatus()
        object InProgress : DeleteAccountStatus()
        object Success : DeleteAccountStatus()
        data class Error(val message: String) : DeleteAccountStatus()
    }
}
