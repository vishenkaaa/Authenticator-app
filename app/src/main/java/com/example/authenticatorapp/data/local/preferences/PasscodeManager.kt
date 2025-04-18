package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class PasscodeManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "passcode_prefs"
        private const val KEY_PASSCODE = "passcode"
        private const val KEY_TOUCH_ID_ENABLED = "touch_id_enabled"
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

    fun savePasscode(passcode: String) {
        encryptedSharedPreferences.edit() { putString(KEY_PASSCODE, passcode) }
    }

    fun getPasscode(): String {
        return encryptedSharedPreferences.getString(KEY_PASSCODE, "") ?: ""
    }

    fun getPasscodeIsSet(): Boolean {
        return getPasscode().isNotEmpty()
    }

    fun getTouchIdEnabled(): Boolean {
        return encryptedSharedPreferences.getBoolean(KEY_TOUCH_ID_ENABLED, false)
    }

    fun setTouchIdEnabled(enabled: Boolean) {
        encryptedSharedPreferences.edit() { putBoolean(KEY_TOUCH_ID_ENABLED, enabled) }
    }
}