package com.example.authenticatorapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.authenticatorapp.data.local.preferences.PasscodeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val passcodeManager: PasscodeManager
) : ViewModel() {

    private val _isPasscodeEnabled = MutableStateFlow(false)
    val isPasscodeEnabled = _isPasscodeEnabled.asStateFlow()

    init {
        checkPasscodeEnabled()
    }

    private fun checkPasscodeEnabled() {
        _isPasscodeEnabled.value = passcodeManager.isPasscodeSet()
    }

    var isAppLocked by mutableStateOf(false)
        private set

    var wasSplashShown by mutableStateOf(false)
        private set

    private var isIntentionalExternalAction = false

    private var backgroundStartTime: Long = 0

    fun setIntentionalExternalAction() {
        isIntentionalExternalAction = true
    }

    fun markSplashShown() {
        wasSplashShown = true
    }

    fun onAppBackgrounded() {
        backgroundStartTime = System.currentTimeMillis()
    }

    fun onAppResumed() {
        val timeInBackground = System.currentTimeMillis() - backgroundStartTime

        if (isIntentionalExternalAction) {
            isIntentionalExternalAction = false
            isAppLocked = false
        } else {
            if (shouldLockApp(timeInBackground)) {
                isAppLocked = true
            }
        }
    }

    private fun shouldLockApp(timeInBackground: Long): Boolean {
        return timeInBackground > 5_000 && passcodeManager.isPasscodeSet()
    }

    fun isPasscodeSet(): Boolean {
        return passcodeManager.isPasscodeSet()
    }

    fun clearPasscode() {
        passcodeManager.savePasscode("")
    }
}
