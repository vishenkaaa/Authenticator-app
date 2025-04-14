package com.example.authenticatorapp.data.local.manager

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PasscodeManager(private val context: Context) {

    private val encryptedSharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "encrypted_passcode_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun savePasscode(passcode: String) {
        encryptedSharedPreferences.edit().putString("passcode", passcode).apply()
    }

    fun getPasscode(): String {
        return encryptedSharedPreferences.getString("passcode", "") ?: ""
    }

    fun isPasscodeSet(): Boolean {
        return getPasscode().isNotEmpty()
    }

    fun isTouchIdEnabled(): Boolean {
        return encryptedSharedPreferences.getBoolean("touch_id_enabled", false)
    }

    fun setTouchIdEnabled(enabled: Boolean) {
        encryptedSharedPreferences.edit().putBoolean("touch_id_enabled", enabled).apply()
    }
}