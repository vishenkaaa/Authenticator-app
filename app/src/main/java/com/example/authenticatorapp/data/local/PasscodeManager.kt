package com.example.authenticatorapp.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PasscodeManager(private val context: Context) {

    companion object {
        private const val PREFS_FILE_NAME = "encrypted_passcode_prefs"
        private const val PASSCODE_KEY = "passcode"
    }

    private val encryptedSharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun savePasscode(passcode: String) {
        encryptedSharedPreferences.edit().putString(PASSCODE_KEY, passcode).apply()
    }

    fun getPasscode(): String {
        return encryptedSharedPreferences.getString(PASSCODE_KEY, "") ?: ""
    }

    fun isPasscodeSet(): Boolean {
        return getPasscode().isNotEmpty()
    }
}