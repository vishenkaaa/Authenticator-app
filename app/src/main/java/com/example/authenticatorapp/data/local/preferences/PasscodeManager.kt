package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

//FIXME літерали
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
        //FIXME фіксимо варнінги. Використовуємо KTX extension function 
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
        //FIXME використовуємо KTX extension function
        encryptedSharedPreferences.edit().putBoolean("touch_id_enabled", enabled).apply()
    }
}