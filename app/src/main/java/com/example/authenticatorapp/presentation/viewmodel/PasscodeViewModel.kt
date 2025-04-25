package com.example.authenticatorapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.local.preferences.PasscodeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val passcodeManager: PasscodeManager,
) : ViewModel() {

    private val _passcode = MutableStateFlow("")
    val passcode = _passcode.asStateFlow()

    private val _confirmPasscode = MutableStateFlow("")
    val confirmPasscode = _confirmPasscode.asStateFlow()

    private val _isConfirmStep = MutableStateFlow(false)
    val isConfirmStep = _isConfirmStep.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _isTouchIdEnabled = MutableStateFlow(passcodeManager.isTouchIdEnabled())
    val isTouchIdEnabled = _isTouchIdEnabled.asStateFlow()

    fun resetState() {
        _passcode.value = ""
        _confirmPasscode.value = ""
        _isConfirmStep.value = false
        _errorMessage.value = ""
    }

    fun addDigit(
        digit: String,
        isCreatingMode: Boolean,
        isEditingMode: Boolean,
        onSuccess: (String, Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        if (_passcode.value.length >= 4 || _isProcessing.value) return

        if (_errorMessage.value.isNotEmpty()) {
            _errorMessage.value = ""
        }

        _passcode.value += digit
        _errorMessage.value = ""

        if (_passcode.value.length == 4) {
            checkPasscode(
                isCreatingMode = isCreatingMode,
                isEditingMode = isEditingMode,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    private fun checkPasscode(
        isCreatingMode: Boolean,
        isEditingMode: Boolean,
        onSuccess: (String, Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isProcessing.value = true
            delay(500)

            val code = _passcode.value

            when {
                isCreatingMode -> {
                    if (!_isConfirmStep.value) {
                        _confirmPasscode.value = code
                        _passcode.value = ""
                        _isConfirmStep.value = true
                    } else {
                        if (code == _confirmPasscode.value) {
                            savePasscode(code)
                            onSuccess(code, true)
                        } else {
                            _errorMessage.value = "Коди не співпадають, спробуйте ще раз"
                            _passcode.value = ""
                        }
                    }
                }

                isEditingMode -> {
                    if (!_isConfirmStep.value) {
                        val savedCode = getSavedPasscode()
                        if (code == savedCode) {
                            _passcode.value = ""
                            _isConfirmStep.value = true
                        } else {
                            _errorMessage.value = "Невірний старий пароль"
                            _passcode.value = ""
                        }
                    } else {
                        savePasscode(code)
                        onSuccess(code, true)
                    }
                }

                else -> {
                    val savedCode = getSavedPasscode()
                    if (code == savedCode) {
                        onSuccess(code, false)
                    } else {
                        _errorMessage.value = "Невірний код"
                        _passcode.value = ""
                    }
                }
            }

            _isProcessing.value = false
        }
    }

    fun removeLastDigit() {
        if (_passcode.value.isNotEmpty() && !_isProcessing.value) {
            _passcode.value = _passcode.value.dropLast(1)
            _errorMessage.value = ""
        }
    }

    fun getSavedPasscode(): String = passcodeManager.getPasscode()
    fun savePasscode(passcode: String) = passcodeManager.savePasscode(passcode)
}

