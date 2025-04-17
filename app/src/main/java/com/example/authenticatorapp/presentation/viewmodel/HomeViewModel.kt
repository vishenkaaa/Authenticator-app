package com.example.authenticatorapp.presentation.viewmodel

import OtpGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.AccountEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val accountDao: AccountDao) : ViewModel() {
    private val _isLoadingAccounts = MutableStateFlow(true)
    val isLoadingAccounts: StateFlow<Boolean> = _isLoadingAccounts

    val accounts: StateFlow<List<AccountEntity>> = accountDao.getAllAccounts()
        .onEach {
            _isLoadingAccounts.value = false
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    suspend fun generateOtp(account: AccountEntity): String {
        return try {
            if (account.type == "TOTP") {
                OtpGenerator.generateTOTP(account.secret, 30, account.digits, account.algorithm)
            } else {
                OtpGenerator.generateHOTP(account.secret, account.counter, account.digits, account.algorithm)
            }
        } catch (e: Exception) {
            "Error"
        }
    }
}
