package com.example.authenticatorapp.presentation.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.authenticatorapp.R
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.data.model.AccountType.HOTP
import com.example.authenticatorapp.data.model.AccountType.TOTP

@Composable
fun AccountType.toLocalizedStringRes(): String {
    return when (this) {
        TOTP -> stringResource(R.string.time_based)
        HOTP -> stringResource(R.string.counter_based)
    }
}
