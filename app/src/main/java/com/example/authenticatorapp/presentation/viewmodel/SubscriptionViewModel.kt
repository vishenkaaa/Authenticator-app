package com.example.authenticatorapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.authenticatorapp.data.repository.AuthRepository
import com.example.authenticatorapp.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
    private val subscriptionRepository: SubscriptionRepository
) : AndroidViewModel(application) {

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

    init {
        checkAuthStatus()
        monitorAuthChanges()

        _plan.value = prefs.getString("plan", null)
        _nextBilling.value = formatDate(prefs.getString("next_billing", null))
    }

    fun checkAuthStatus() {
        _isAuthenticated.value = authRepository.isUserLoggedIn()
    }

    private fun monitorAuthChanges() {
        authRepository.monitorAuthState { isLoggedIn ->
            _isAuthenticated.value = isLoggedIn
            if (isLoggedIn) loadSubscription()
        }
    }

    fun signOut(context: Context) {
        authRepository.signOut(context)
        clearLocalSubscription()
        checkAuthStatus()
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            val success = authRepository.deleteAccount(context)
            if (!success) {
                Log.d("SubscriptionViewModel", "User account successfully deleted")
            }
        }
    }

    fun saveSubscription(plan: String, hasFreeTrial: Boolean) {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser() ?: return@launch

            val today = LocalDate.now()
            val nextBilling = when {
                plan.contains("Yearly", ignoreCase = true) -> today.plusYears(1)
                hasFreeTrial -> today.plusDays(3).plusWeeks(1)
                else -> today.plusWeeks(1)
            }.toString()

            // Зберігаємо в Firestore
            subscriptionRepository.saveSubscriptionForUser(
                user.id, user.email ?: "", plan, nextBilling, hasFreeTrial
            )

            // Зберігаємо локально
            prefs.edit()
                .putString("plan", plan)
                .putString("next_billing", nextBilling)
                .apply()

            _plan.value = plan
            _nextBilling.value = formatDate(nextBilling)
        }
    }

    fun loadSubscription() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser()?.id ?: return@launch
            try {
                val subscription = subscriptionRepository.loadSubscriptionForUser(uid)

                val plan = subscription?.get("plan") as? String
                val nextBilling = subscription?.get("nextBilling") as? String

                if (plan != null && nextBilling != null) {
                    // Оновлюємо локально
                    prefs.edit()
                        .putString("plan", plan)
                        .putString("next_billing", nextBilling)
                        .apply()

                    _plan.value = plan
                    _nextBilling.value = formatDate(nextBilling)
                }
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Не вдалося завантажити підписку: ${e.message}")
            }
        }
    }

    fun cancelSubscription() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser()?.id ?: return@launch

            subscriptionRepository.cancelSubscription(uid)
            clearLocalSubscription()
        }
    }

    private fun clearLocalSubscription() {
        prefs.edit().clear().apply()
        _plan.value = null
        _nextBilling.value = null
    }

    fun formatDate(isoDate: String?): String {
        if (isoDate == null) return ""
        return try {
            val date = LocalDate.parse(isoDate)
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
            date.format(formatter)
        } catch (e: Exception) {
            ""
        }
    }
}