package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SubscriptionPreferences(private val context: Context) {
    companion object {
        private const val PREF_NAME = "subscription_prefs"
        private const val KEY_PLAN = "plan"
        private const val KEY_NEXT_BILLING = "next_billing"
    }

    private val encryptedSharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveSubscription(plan: String, nextBilling: String) {
        encryptedSharedPreferences.edit {
            putString(KEY_PLAN, plan)
            putString(KEY_NEXT_BILLING, nextBilling)
        }
    }

    fun getSavedPlan(): String? = encryptedSharedPreferences.getString(KEY_PLAN, null)

    fun getSavedNextBilling(): String? = encryptedSharedPreferences.getString(KEY_NEXT_BILLING, null)

    fun clear() {
        encryptedSharedPreferences.edit().clear()
    }
}