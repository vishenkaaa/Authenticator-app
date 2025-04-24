package com.example.authenticatorapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.local.preferences.SubscriptionPreferences
import com.example.authenticatorapp.data.model.SubscriptionType
import com.example.authenticatorapp.data.repository.AuthRepository
import com.example.authenticatorapp.data.repository.SubscriptionRepository
import com.example.authenticatorapp.data.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionPreferences: SubscriptionPreferences,
    private val syncRepository: SyncRepository
) : AndroidViewModel(application) {

    private var _currentPlan = MutableStateFlow<SubscriptionType?>(null)
    val currentPlan: StateFlow<SubscriptionType?> = _currentPlan

    private val _nextBilling = MutableStateFlow<String?>(null)
    val nextBilling: StateFlow<String?> = _nextBilling

    private val _selectedSubscriptionType = MutableStateFlow<SubscriptionType?>(null)
    val selectedSubscriptionType: StateFlow<SubscriptionType?> = _selectedSubscriptionType

    private val _hasFreeTrial = MutableStateFlow(false)
    val hasFreeTrial: StateFlow<Boolean> = _hasFreeTrial

    init {
        monitorAuthChanges()
        loadSubscriptionFromPrefs()
        loadSubscription()
    }

    private fun monitorAuthChanges() {
        authRepository.monitorAuthState { isLoggedIn ->
            if (isLoggedIn) loadSubscription()
        }
    }

    fun onSubscriptionSelected(type: SubscriptionType) {
        _selectedSubscriptionType.value = type
        _hasFreeTrial.value = type != SubscriptionType.YEARLY
    }

    fun toggleFreeTrial(enabled: Boolean) {
        _hasFreeTrial.value = enabled
        if (enabled) _selectedSubscriptionType.value = SubscriptionType.WEEKLY
        else _selectedSubscriptionType.value = SubscriptionType.YEARLY
    }

    fun saveSubscription() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser() ?: return@launch

                val today = LocalDate.now()
                val nextBilling = when {
                    _selectedSubscriptionType.value == SubscriptionType.YEARLY -> today.plusYears(1)
                    _hasFreeTrial.value -> today.plusDays(3).plusWeeks(1)
                    else -> today.plusWeeks(1)
                }.toString()

                _currentPlan.value = _selectedSubscriptionType.value
                _nextBilling.value = formatDate(nextBilling)

                subscriptionRepository.saveSubscriptionForUser(
                    user.id,
                    user.email ?: "",
                    _selectedSubscriptionType.value!!.value,
                    nextBilling,
                    _hasFreeTrial.value
                )

                subscriptionPreferences.saveSubscription(_selectedSubscriptionType.value!!.value, nextBilling)
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Помилка збереження підписки: ${e.message}")
            }
        }
    }

    private fun loadSubscriptionFromPrefs() {
        try {
            _currentPlan.value = subscriptionPreferences.getSavedPlan()
                ?.let { SubscriptionType.from(it) }
            _nextBilling.value = formatDate(subscriptionPreferences.getSavedNextBilling())
        } catch (e: Exception) {
            Log.e("SubscriptionViewModel", "Помилка при завантаженні з SharedPreferences: ${e.message}")
        }
    }

    fun loadSubscription() {
        viewModelScope.launch {
            try {
                val uid = authRepository.getCurrentUser()?.id ?: return@launch
                val subscription = subscriptionRepository.loadSubscriptionForUser(uid)

                val plan = subscription?.get("plan") as? String
                val nextBilling = subscription?.get("nextBilling") as? String

                if (plan != null && nextBilling != null) {
                    subscriptionPreferences.saveSubscription(plan, nextBilling)

                    _currentPlan.value = SubscriptionType.from(plan)
                    _nextBilling.value = formatDate(nextBilling)
                }
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Не вдалося завантажити підписку: ${e.message}")
            }
        }
    }

    fun cancelSubscription() {
        viewModelScope.launch {
            try {
                val uid = authRepository.getCurrentUser()?.id ?: return@launch

                syncRepository.setShouldSynchronize(uid, false)
                subscriptionRepository.cancelSubscription(uid)

                subscriptionPreferences.clear()
                _currentPlan.value = null
                _nextBilling.value = null
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Помилка при скасуванні підписки: ${e.message}")
            }
        }
    }

    private fun formatDate(isoDate: String?): String {
        return isoDate?.let {
            try {
                val date = LocalDate.parse(it)
                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
                date.format(formatter)
            } catch (e: Exception) {
                ""
            }
        } ?: ""
    }
}