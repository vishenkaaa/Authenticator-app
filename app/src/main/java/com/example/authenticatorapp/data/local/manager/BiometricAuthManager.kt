package com.example.authenticatorapp.data.local.manager

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.authenticatorapp.R

class BiometricAuthManager(private val activity: FragmentActivity) {

    private val biometricManager = BiometricManager.from(activity)

    fun isBiometricAvailable(): Boolean {
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL

        val result = biometricManager.canAuthenticate(authenticators)
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun showBiometricPrompt(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onCancel: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                    errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    onCancel()
                } else {
                    onError(errString.toString())
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError(activity.getString(R.string.biometric_authentication_failed))
            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)

        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.fingerprint_login))
            .setSubtitle(activity.getString(R.string.confirm_login_using_your_fingerprint))
            .setNegativeButtonText(activity.getString(R.string.use_passcode))
            .setAllowedAuthenticators(authenticators)
            .build()

        try {
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            onError("Помилка відображення біометричного промпту: ${e.message}")
        }
    }

    companion object {
        fun create(context: Context): BiometricAuthManager? {
            return when (context) {
                is FragmentActivity -> {
                    BiometricAuthManager(context)
                }
                else -> {
                    null
                }
            }
        }
    }
}