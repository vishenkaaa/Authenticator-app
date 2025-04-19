package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SyncPreferences(context: Context) {

    //FIXME не скорочуємо назви. Краще використай  preferences або взагалі назву класу encryptedSharedPreferences
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

    //FIXME проблема з найменуванням функції, літерали,
    fun isSync(): Boolean? {
        //FIXME тут ця перевірка недоречна. Якщо prefs ще немає isSyncEnabled воно поверне дефолтне значення(false)
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