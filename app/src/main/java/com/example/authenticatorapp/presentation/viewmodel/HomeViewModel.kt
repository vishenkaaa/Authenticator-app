package com.example.authenticatorapp.presentation.viewmodel

import OtpGenerator
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.presentation.utils.extensions.toLocalizedStringRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val accountDao: AccountDao) : ViewModel() {
    private val _isLoadingAccounts = MutableStateFlow(true)
    val isLoadingAccounts: StateFlow<Boolean> = _isLoadingAccounts

    private val _timeTicker = MutableStateFlow(0)
    val timeTicker: StateFlow<Int> = _timeTicker

    init {
        startTicker()
    }

    private fun startTicker() {
        viewModelScope.launch {
            while (isActive) {
                _timeTicker.value = calculateRemainingTime()
                delay(1000)
            }
        }
    }

    val accounts: StateFlow<List<AccountEntity>> = accountDao.getAllAccounts()
        .onEach {
            _isLoadingAccounts.value = false
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun generateOtp(account: AccountEntity): String {
        return try {
            if (account.type == AccountType.TOTP) {
                OtpGenerator.generateTOTP(account.secret, 30, account.digits, account.algorithm)
            } else {
                OtpGenerator.generateHOTP(account.secret, account.counter, account.digits, account.algorithm)
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun calculateRemainingTime(): Int {
        val periodSeconds = 30
        val ntpTime = System.currentTimeMillis() // getNtpTime() ?: System.currentTimeMillis()
        val currentTimeSeconds = ntpTime / 1000
        return periodSeconds - (currentTimeSeconds % periodSeconds).toInt()
    }

    @Composable
    fun filterAccounts(accounts: List<AccountEntity>, query: String, type: AccountType): List<AccountEntity> {
        if (query.isBlank()) {
            return accounts.filter { it.type == type }
        }

        val searchLower = query.lowercase()
        return accounts.filter {
            it.type == type && (it.serviceName.toLocalizedStringRes().lowercase().contains(searchLower) ||
                                    it.email.lowercase().contains(searchLower))
        }
    }
}
