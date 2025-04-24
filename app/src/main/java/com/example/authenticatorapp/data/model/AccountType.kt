package com.example.authenticatorapp.data.model

enum class AccountType(val value: String) {
    TOTP("TOTP"),
    HOTP("HOTP");

    companion object {
        fun from(value: String): AccountType? = entries.find { it.value == value }
    }
}