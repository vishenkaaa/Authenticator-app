package com.example.authenticatorapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceName: String,
    val email: String,
    val secret: String,
    val type: String, // "TOTP" або "HOTP"
    val algorithm: String, // "SHA1", "SHA256", "SHA512"
    val digits: Int = 6,
    val counter: Long = 0
)
