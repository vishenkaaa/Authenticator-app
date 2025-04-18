package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SyncPreferences(context: Context) {

    companion object {
        private const val PREF_NAME = "sync_prefs"
        private const val KEY_IS_SYNC_ENABLED = "is_sync_enabled"
    }

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getSyncEnabled(): Boolean? {
        if (prefs.contains(KEY_IS_SYNC_ENABLED)) {
            return prefs.getBoolean(KEY_IS_SYNC_ENABLED, false)
        }
        else return null
    }

    fun setSyncEnabled(isSync: Boolean) {
        prefs.edit() {
            putBoolean(KEY_IS_SYNC_ENABLED, isSync)
        }
    }
}