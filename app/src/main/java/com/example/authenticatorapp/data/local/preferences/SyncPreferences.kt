package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SyncPreferences(context: Context) {

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            "sync_prefs",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isSync(): Boolean? {
        if (prefs.contains("isSyncEnabled")) {
            return prefs.getBoolean("isSyncEnabled", false)
        }
        else return null
    }

    fun setSync(isSync: Boolean) {
        prefs.edit() {
            putBoolean("isSyncEnabled", isSync)
        }
    }
}