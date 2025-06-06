package com.example.authenticatorapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.local.dao.AccountDao
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.data.repository.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val currentUserId: String?
        get() = auth.currentUser?.uid

    fun addAccount(
        service: String,
        email: String,
        secret: String,
        type: String,
        algorithm: String = "HmacSHA1",
        digits: Int = 6,
        counter: Long = 0
    ) {
        if (service.isBlank() || email.isBlank() || secret.isBlank()) return

        val typeNormalized = when (type.lowercase()) {
            "time-based" -> "TOTP"
            "counter-based" -> "HOTP"
            else -> "TOTP"
        }

        val algorithmNormalized = when (algorithm.uppercase()) {
            "SHA1" -> "HmacSHA1"
            "SHA256" -> "HmacSHA256"
            "SHA512" -> "HmacSHA512"
            else -> "HmacSHA1"
        }

        val account = AccountEntity(
            serviceName = service,
            email = email,
            secret = secret.replace(" ", ""),
            type = typeNormalized,
            algorithm = algorithmNormalized,
            digits = digits,
            counter = counter
        )

        viewModelScope.launch {
            val insertedId = accountDao.insertAccount(account)

            currentUserId?.let { uid ->
                val savedAccount = account.copy(id = insertedId.toInt())
                accountRepository.saveAccount(uid, savedAccount)
            }
        }
    }

    fun updateAccount(
        id: Int,
        service: String,
        email: String,
        secret: String,
        type: String,
        algorithm: String = "HmacSHA1",
        digits: Int = 6,
        counter: Long = 0
    ) {
        if (service.isBlank() || email.isBlank() || secret.isBlank()) return

        val typeNormalized = when (type.lowercase()) {
            "time-based" -> "TOTP"
            "counter-based" -> "HOTP"
            else -> "TOTP"
        }

        val algorithmNormalized = when (algorithm.uppercase()) {
            "SHA1" -> "HmacSHA1"
            "SHA256" -> "HmacSHA256"
            "SHA512" -> "HmacSHA512"
            else -> "HmacSHA1"
        }

        val account = AccountEntity(
            id = id,
            serviceName = service,
            email = email,
            secret = secret.replace(" ", ""),
            type = typeNormalized,
            algorithm = algorithmNormalized,
            digits = digits,
            counter = counter
        )

        viewModelScope.launch {
            accountDao.updateAccount(account)

            currentUserId?.let { uid ->
                accountRepository.saveAccount(uid, account)
            }
        }
    }

    fun deleteAccount(id: Int) {
        viewModelScope.launch {
            accountDao.deleteAccountById(id)

            currentUserId?.let { uid ->
                accountRepository.deleteAccount(uid, id)
            }
        }
    }

    fun incrementCounter(account: AccountEntity) {
        val updatedAccount = account.copy(counter = account.counter + 1)
        viewModelScope.launch {
            accountDao.updateAccount(updatedAccount)

            currentUserId?.let { uid ->
                accountRepository.saveAccount(uid, updatedAccount)
            }
        }
    }

    private val _account = MutableStateFlow<AccountEntity?>(null)
    val account = _account.asStateFlow()

    fun getAccountById(accountId: Int) {
        viewModelScope.launch {
            _account.value = accountDao.getAccountById(accountId)
        }
    }
}
