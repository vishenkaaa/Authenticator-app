package com.example.authenticatorapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor (application: Application)
    : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            "subscription_prefs",
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

    init {
        _plan.value = prefs.getString("plan", null)
        _nextBilling.value = prefs.getString("next_billing", null)
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
        _plan.value = prefs.getString("plan", null)
        _nextBilling.value = formatDate(prefs.getString("next_billing", null))
    }

    fun clearSubscription() {
        prefs.edit().clear().commit()
        _plan.value = null
        _nextBilling.value = null
    }
}
