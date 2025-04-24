package com.example.authenticatorapp.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.authenticatorapp.data.local.preferences.PasscodeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val passcodeManager: PasscodeManager,
) : ViewModel() {

    private val _isPasscode = mutableStateOf(passcodeManager.isPasscodeSet())
    val isPasscode: State<Boolean> = _isPasscode

    private val _isTouchID = mutableStateOf(passcodeManager.isTouchIdEnabled())
    val isTouchID: State<Boolean> = _isTouchID

    fun updatePasscodeEnabled(enabled: Boolean) {
        _isPasscode.value = enabled
    }

    fun updateTouchIDEnabled(enabled: Boolean) {
        passcodeManager.setTouchIdEnabled(enabled)
        _isTouchID.value = enabled
    }
}
