package com.example.authenticatorapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticatorapp.data.local.model.AccountEntity
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.data.model.OtpAlgorithm
import com.example.authenticatorapp.data.model.ServiceName
import com.example.authenticatorapp.data.repository.AccountRepository
import com.example.authenticatorapp.presentation.utils.OtpAuthParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//FIXME коментарі тут мають відношення до решти viewModels теж, тому аналізуй і роби правки
//Done
@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _selectedService = MutableStateFlow<ServiceName?>(null)
    val selectedService: StateFlow<ServiceName?> = _selectedService

    private val _selectedTypeOfKey = MutableStateFlow(AccountType.TOTP)
    val selectedTypeOfKey: StateFlow<AccountType> = _selectedTypeOfKey

    private val _accountName = MutableStateFlow("")
    val accountName: StateFlow<String> = _accountName

    private val _secretKey = MutableStateFlow("")
    val secretKey: StateFlow<String> = _secretKey

    private val _account = MutableStateFlow<AccountEntity?>(null)
    val account = _account.asStateFlow()

    fun setSelectedService(service: ServiceName) {
        _selectedService.value = service
    }

    fun setSelectedKeyType(type: AccountType) {
        _selectedTypeOfKey.value = type
    }

    fun setAccountName(name: String) {
        _accountName.value = name
    }

    fun setSecretKey(key: String) {
        _secretKey.value = key
    }

    fun processQrCode(qrCode: String): Boolean {
        val otpData = OtpAuthParser.parseOtpAuthUrl(qrCode)
        return if (otpData != null) {
            addAccount(
                service = otpData.serviceName.displayName,
                email = otpData.email,
                secret = otpData.secret,
                type = otpData.type,
                algorithm = otpData.algorithm,
                digits = otpData.digits,
                counter = otpData.counter
            )
            true
        } else {
            false
        }
    }

    fun addAccount(
        service: String,
        email: String,
        secret: String,
        type: AccountType,
        algorithm: OtpAlgorithm = OtpAlgorithm.SHA1,
        digits: Int = 6,
        counter: Long = 0
    ) {
        if (service.isBlank() || email.isBlank() || secret.isBlank()) return

        //TODO Винести в enum/sealed class
        //Done
        //TODO Винести в enum/sealed class
        //Done

        val account = AccountEntity(
            serviceName = ServiceName.from(service),
            email = email,
            secret = secret.replace(" ", ""),
            type = type ,
            algorithm = algorithm,
            digits = digits,
            counter = counter
        )

        viewModelScope.launch {
            //FIXME ут лише викликаємо метод з репозиторію. Всю цю логіку з базою даних винести туди
            //Done
            accountRepository.saveAccount(account)
        }
    }

    fun updateAccount(
        id: Int,
        service: String,
        email: String,
        secret: String,
        type: AccountType,
        algorithm: OtpAlgorithm = OtpAlgorithm.SHA1,
        digits: Int = 6,
        counter: Long = 0
    ) {
        if (service.isBlank() || email.isBlank() || secret.isBlank()) return

        //FIXME ті ж самі три правки, що в попередньому методі
        //Done

        val account = AccountEntity(
            id = id,
            serviceName = ServiceName.from(service),
            email = email,
            secret = secret.replace(" ", ""),
            type = type,
            algorithm = algorithm,
            digits = digits,
            counter = counter
        )

        viewModelScope.launch {
            accountRepository.updateAccount(account)
        }
    }

    fun deleteAccount(id: Int) {
        viewModelScope.launch {
            accountRepository.deleteAccount(id)
        }
    }

    fun incrementCounter(account: AccountEntity) {
        //FIXME ті ж самі правки
        //Done
        val updatedAccount = account.copy(counter = account.counter + 1)
        viewModelScope.launch {
            accountRepository.updateAccount(updatedAccount)
        }
    }

    //FIXME викликаємо лише метод з репозиторію. Доступу до dao ми тут не маємо мати. Репозиторій опвинен цим керувати
    //Done
    fun getAccountById(accountId: Int) {
        viewModelScope.launch {
            val account = accountRepository.getAccountById(accountId)
            _account.value = account

            account?.let {
                _accountName.value = it.email
                _secretKey.value = it.secret
                _selectedService.value = it.serviceName
                _selectedTypeOfKey.value = it.type
            }
        }
    }
}
