package com.example.authenticatorapp.data.local.preferences

import android.app.Application
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

//FIXME літерали
//Done
class PasscodeManager @Inject constructor(
    private val application: Application
) {

    companion object {
        private const val PREF_NAME = "passcode_prefs"
        private const val KEY_PASSCODE = "passcode"
        private const val KEY_TOUCH_ID_ENABLED = "touch_id_enabled"
    }

    private val encryptedSharedPreferences by lazy {
        val masterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            application,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun savePasscode(passcode: String) {
        //FIXME фіксимо варнінги. Використовуємо KTX extension function
        //Done
        encryptedSharedPreferences.edit() { putString(KEY_PASSCODE, passcode) }
    }

    fun getPasscode(): String {
        return encryptedSharedPreferences.getString(KEY_PASSCODE, "") ?: ""
    }

    fun isPasscodeSet(): Boolean {
        return getPasscode().isNotEmpty()
    }

    fun isTouchIdEnabled(): Boolean {
        return encryptedSharedPreferences.getBoolean(KEY_TOUCH_ID_ENABLED, false)
    }

    fun setTouchIdEnabled(enabled: Boolean) {
        //FIXME використовуємо KTX extension function
        //Done
        encryptedSharedPreferences.edit() { putBoolean(KEY_TOUCH_ID_ENABLED, enabled) }
    }
}